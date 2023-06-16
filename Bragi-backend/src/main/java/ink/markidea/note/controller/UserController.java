package ink.markidea.note.controller;

import ink.markidea.note.entity.dto.EditorConfigDto;
import ink.markidea.note.entity.req.EditorConfigReq;
import ink.markidea.note.entity.req.RemoteRepoRequest;
import ink.markidea.note.entity.req.UserEmailSignupRequest;
import ink.markidea.note.entity.req.UserRequest;
import ink.markidea.note.entity.resp.ServerResponse;
import ink.markidea.note.entity.vo.UserVo;
import ink.markidea.note.service.IAdminService;
import ink.markidea.note.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;


@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private IAdminService adminService;

    @Autowired
    private IUserService userService;

    //验证码
//    private String code;
//    @Autowired
//    HttpServletRequest globalRequest;

    HashMap<String, String> validationCodeByEmail = new HashMap<>();

    @PostMapping("login")
    public ServerResponse<UserVo> login(@RequestBody UserRequest request){
        return userService.login(request.getUsername(), request.getPassword(), request.getFlag());
    }

    @PostMapping("validate")
    public ServerResponse validate(){
        // Validated by interceptor
        return ServerResponse.buildSuccessResponse();
    }

    //    注册
    @PostMapping("register")
    public ServerResponse<String> register(@RequestBody UserRequest req, HttpServletRequest request) {
        //        后端暂存的注册的验证码
        String code1 = validationCodeByEmail.getOrDefault(req.getEmailnumber(), "");
        ServerResponse resp = userService.register(req.getUsername(), req.getPassword(), req.getEmailnumber(), req.getCode(), code1);
        if (resp.getCode() == 0){
            validationCodeByEmail.remove(req.getEmailnumber());
        }
        return resp;
    }

    @PostMapping("/emailsignup")
    public ServerResponse<String> emailsignup(@RequestBody UserEmailSignupRequest req) throws Exception {
        ServerResponse<String> response = userService.sendOutEmail(req.getEmailNo());
        if (response.isSuccess()) {
            String code1 = response.getData();
//            存入map
            validationCodeByEmail.put(req.getEmailNo(), code1);
            return response;
        } else {
            return response;
        }
    }


    //    找回密码时发邮件
    @PostMapping("/retrievepwd")
    public ServerResponse<String> retrievepwd(@RequestBody UserEmailSignupRequest req) throws Exception {
        ServerResponse<String> response = userService.sendOutEmailRetreievePwd(req.getEmailNo());
        if(response.isSuccess()){//如果成功就返回验证码在前端存储
            String code2 = response.getData();
//            存入map
            validationCodeByEmail.put(req.getEmailNo(), code2);
            return response;
        }
        else{
            return response;
        }
    }

//    点击确认找回按钮
    @PostMapping("/confirm_retrieve")
    public ServerResponse<String> confirm_retrieve(@RequestBody UserRequest req) throws Exception {
        //比对code，如果一样改密码
        String codeInput = req.getCode();
        if(codeInput==null){
            return ServerResponse.buildErrorResponse("验证码不能为空！");
        }
//        后端暂存的找回密码的验证码
        String code2 = validationCodeByEmail.getOrDefault(req.getEmailnumber(), "");

        if(codeInput.equals(code2)){
            validationCodeByEmail.remove(req.getEmailnumber());
            //修改密码
            userService.updatePassword(req.getEmailnumber(), req.getPassword());
            return ServerResponse.buildSuccessResponse("修改成功");
        }
        else{
            return ServerResponse.buildErrorResponse("修改失败");
        }
    }

    @PostMapping("/changePass")
    public ServerResponse changePassword(@RequestBody UserRequest request){
        return userService.changePassword(request.getPassword(), request.getNewPassword());
    }

    @PostMapping("/logout")
    public ServerResponse logout(){
        System.out.println("退出登录");
        return userService.logout();
    }

    @PutMapping("remote")
    public ServerResponse setRemoteRepoUrl(@RequestBody RemoteRepoRequest request){
        return adminService.setRemoteRepoUrl(request.getRemoteRepoUrl());
    }

    @GetMapping("remote")
    public ServerResponse getRemoteRepoUrl(){
        return adminService.getRemoteRepoUrl();
    }

    @PostMapping("sshkey")
    public ServerResponse<String> genSshKeys(){
        return adminService.generateSshKey();
    }

    @DeleteMapping("push")
    public ServerResponse stopPushToRemote(){
        return adminService.stopPushToRemoteRepo();
    }

    @GetMapping("push")
    public ServerResponse checkPushStatus(){
        return adminService.checkPushTaskStatus();
    }

    @PostMapping("push")
    public ServerResponse startPushToRemote(){
        return adminService.startPushToRemoteRepo();
    }

    @PostMapping("pull")
    public ServerResponse pullFromRemote(){
        return adminService.pullFromRemote();
    }

    @GetMapping("editorConfig")
    public ServerResponse<EditorConfigDto> getEditorConfig() {
        return ServerResponse.buildSuccessResponse(userService.getEditorConfig());
    }

    @PostMapping("updateEditorConfig")
    public ServerResponse<EditorConfigDto> updateEditorConfig(@RequestBody EditorConfigReq req) {
        return ServerResponse.buildSuccessResponse(userService.updateEditorConfig(req));
    }
}
