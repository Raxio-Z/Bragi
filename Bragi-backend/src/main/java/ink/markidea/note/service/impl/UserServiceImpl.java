package ink.markidea.note.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import ink.markidea.note.constant.RegisterConstant;
import ink.markidea.note.constant.UserConstant;
import ink.markidea.note.dao.UserRepository;
import ink.markidea.note.entity.UserDo;
import ink.markidea.note.entity.dto.EditorConfigDto;
import ink.markidea.note.entity.exception.NoAuthorityException;
import ink.markidea.note.entity.exception.PromptException;
import ink.markidea.note.entity.req.EditorConfigReq;
import ink.markidea.note.entity.resp.ServerResponse;
import ink.markidea.note.entity.vo.UserVo;
import ink.markidea.note.service.IUserService;
import ink.markidea.note.util.EmailUtils;
import ink.markidea.note.util.JsonUtil;
import ink.markidea.note.util.MD5Util;
import ink.markidea.note.util.ThreadLocalUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private Cache<String, UserVo> userCache;

    @Value("${username:admin}")
    private String adminUsername;

    @Value("${password:admin}")
    private String adminPassword;

    @Value("${emailnumber:admin}")
    private String emailNumber;

    /**
     * 注册控制策略
     */
    @Value("${registerStrategy}")
    private volatile Integer registerStrategy;


    @Autowired
    private UserRepository userRepository;

    private static final String TOKEN_PREFIX = "token_";

    //邮件发送
    @Autowired
    private EmailUtils emailUtils;

    @Override
    public ServerResponse<String> sendOutEmail(String email) throws Exception {
        if(userRepository.findByEmailnumber(email) != null){
            System.out.println ("该邮箱已被注册！");
            return ServerResponse.buildErrorResponse("该邮箱已被注册！");//返回错误响应
        }
        else {
            String subject = "您正在注册Bragi云笔记，请收好您的验证码";
            String code = String.valueOf(new Random().nextInt(899999) + 100000);
            String msg = "<h4>欢迎您注册Bragi</h4>" + "<p style='color:#F00'>您的验证码为:" + code + "</p>";
            emailUtils.sendEmail(email, subject, msg);
            return ServerResponse.buildSuccessResponse(code);//返回正确响应，并传回验证码
        }
    }
    @Override
    public ServerResponse<String> sendOutEmailRetreievePwd(String email) throws Exception {
        if(userRepository.findByEmailnumber(email) == null){
            System.out.println ("该邮箱尚未注册！");
            return ServerResponse.buildErrorResponse("该邮箱尚未注册！");//返回错误响应
        }
        else {
            String subject = "您正在Bragi云笔记找回密码，请收好您的验证码";
            String code = String.valueOf(new Random().nextInt(899999) + 100000);
            String msg = "<h4>Bragi找回密码</h4>" + "<p style='color:#F00'>您的验证码为:" + code + "</p>";
            emailUtils.sendEmail(email, subject, msg);
            return ServerResponse.buildSuccessResponse(code);//返回正确响应，并传回验证码
        }
    }

    @PostConstruct
    private void initAdminUser(){
        UserDo userDO = userRepository.findFirstByStatus(0);
        if (userDO == null){
            UserDo adminUser = new UserDo().setUsername(adminUsername)
                    .setPassword(MD5Util.MD5EncodeUtf8(adminPassword))
                    .setEmailnumber(emailNumber)
                    .setStatus(UserConstant.ADMIN_USER);
            userRepository.save(adminUser);
        }
    }

    @Override
    public ServerResponse<UserVo> login(String username, String password, String flag){
        //对密码进行编码处理
        UserVo userVo;
        String encodedPassword = MD5Util.MD5EncodeUtf8(password);
        if(flag.equals("emaillogin")){
            UserDo userDO = userRepository.findByEmailnumberAndPassword(username, encodedPassword);
            if (userDO == null) {
                return ServerResponse.buildErrorResponse("邮箱或密码错误");
            }
            userVo = new UserVo().setToken(TOKEN_PREFIX + UUID.randomUUID().toString()).setUsername(userDO.getUsername()).setType(userDO.getStatus());
            if (StringUtils.isNotBlank(userDO.getEditorConfig())) {
                userVo.setEditorConfig(JsonUtil.stringToObj(userDO.getEditorConfig(), EditorConfigDto.class));
            }
            userCache.put(userVo.getUsername(), userVo);
        }
        else{
            UserDo userDO = userRepository.findByUsernameAndPassword(username, encodedPassword);
            if (userDO == null) {
                return ServerResponse.buildErrorResponse("用户名或密码错误");
            }
            userVo = new UserVo().setToken(TOKEN_PREFIX + UUID.randomUUID().toString()).setUsername(username).setType(userDO.getStatus());
            if (StringUtils.isNotBlank(userDO.getEditorConfig())) {
                userVo.setEditorConfig(JsonUtil.stringToObj(userDO.getEditorConfig(), EditorConfigDto.class));
            }
            userCache.put(userVo.getUsername(), userVo);
        }
        return ServerResponse.buildSuccessResponse(userVo);
    }

    @Override
    public ServerResponse validate(String username, String token){
        UserVo userVo = userCache.getIfPresent(username);
        if (userVo == null || !token.equals(userVo.getToken())){
            return ServerResponse.buildErrorResponse("Invalid token");
        }else {
            return ServerResponse.buildSuccessResponse();
        }
    }


    @Override
    public boolean checkAdminUser(String username, String token) {
        UserVo userVo = userCache.getIfPresent(username);
        if (userVo == null || !token.equals(userVo.getToken())){
            return false;
        }

        return userVo.getType() == UserConstant.ADMIN_USER;
    }

    @Override
    public ServerResponse logout() {
        userCache.invalidate(getUsername());
        return ServerResponse.buildSuccessResponse();
    }

    @Override
    public ServerResponse<String> register(String username, String password, String emailnumber, String code, String codesent){
        if (StringUtils.isAnyBlank(username, password,emailnumber,code)){
            return ServerResponse.buildErrorResponse("参数不能为空");
        }
        //验证新密码格式是否合规
        String regex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=_!])(?!.*\\s).{6,}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);

        //验证码不对
        if(!code.equals(codesent)){
            return ServerResponse.buildErrorResponse("验证码错误");
        }
        else {
            if(userRepository.findByUsername(username) != null){
                return ServerResponse.buildErrorResponse("用户名已存在！");
            }
            else if(!matcher.matches()){//后端二次校验密码格式
                return ServerResponse.buildErrorResponse("密码格式错误！");
            }
            else {
                //存到数据库中
                UserDo userDO = userRepository.save(new UserDo()
                        .setUsername(username)
                        .setPassword(MD5Util.MD5EncodeUtf8(password))
                        .setEmailnumber(emailnumber));//把邮箱也存入数据库中，邮箱也是唯一的
            }
        }
        return ServerResponse.buildSuccessResponse("");
    }

    //无需旧密码，需要账号
    @Override
    public ServerResponse updatePassword(String emailNumber,String newPassword)
    {
        UserDo userDo = userRepository.findByEmailnumber(emailNumber);
        String newEncodedPassword = MD5Util.MD5EncodeUtf8(newPassword);
        userDo.setPassword(newEncodedPassword);
        userRepository.save(userDo);
        return ServerResponse.buildSuccessResponse();
    }

    //需要新密码
    @Override
    @Transactional
    public ServerResponse changePassword(String oldPassword, String newPassword){
        if (StringUtils.isAnyBlank(oldPassword, newPassword)){
            throw new IllegalArgumentException();
        }

        //验证旧密码是否正确
        String oldEncodedPassword = MD5Util.MD5EncodeUtf8(oldPassword);
        UserDo userDO = userRepository.findByUsernameAndPassword(getUsername(), oldEncodedPassword);
        if (userDO == null){
            return ServerResponse.buildErrorResponse("旧密码错误！");
        }

        //验证新密码格式是否合规
        String regex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=_!])(?!.*\\s).{6,}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(newPassword);

        if(!matcher.matches()){ //后端正则表达式匹配
            return ServerResponse.buildErrorResponse("新密码不符合格式要求！");
        }

        String newEncodedPassword = MD5Util.MD5EncodeUtf8(newPassword);
        userDO.setPassword(newEncodedPassword);
        userRepository.save(userDO);
        return ServerResponse.buildSuccessResponse();

    }

    private String getUsername(){
        return ThreadLocalUtil.getUsername();
    }

    @Override
    public void setRegisterStrategy(Integer strategy) {
        this.registerStrategy = strategy;
    }

    @Override
    public EditorConfigDto getEditorConfig() {
        String editorConfigStr = userRepository.findByUsername(getUsername()).getEditorConfig();
        if (StringUtils.isBlank(editorConfigStr)) {
            return new EditorConfigDto();
        } else {
            return JsonUtil.stringToObj(editorConfigStr, EditorConfigDto.class);
        }

    }

    @Override
    public EditorConfigDto updateEditorConfig(EditorConfigReq req) {
        UserDo userDo = userRepository.findByUsername(getUsername());
        userDo.setEditorConfig(JsonUtil.objToString(req));
        userRepository.save(userDo);
        return req;
    }

    @Override
    public Page<UserVo> getUserList(Integer page, Integer size) {
        return userRepository.findAll(PageRequest.of(page, size)).map(userDo -> userDo.setPassword(null))
                .map(userDo -> new UserVo().setUsername(userDo.getUsername()).setType(userDo.getStatus()));
    }

}
