package ink.markidea.note.service;


import ink.markidea.note.entity.UserDo;
import ink.markidea.note.entity.dto.EditorConfigDto;
import ink.markidea.note.entity.req.EditorConfigReq;
import ink.markidea.note.entity.resp.ServerResponse;
import ink.markidea.note.entity.vo.UserVo;
import org.springframework.data.domain.Page;

/**
 *  
 * @date 2019/12/31
 */
public interface IUserService {

    ServerResponse<UserVo> login(String username, String password, String flag);

    ServerResponse validate(String username, String token);

    boolean checkAdminUser(String username, String token);

    ServerResponse logout();

    ServerResponse<String> register(String username, String password, String emailnumber, String code, String codesent);

    ServerResponse changePassword(String oldPassword, String newPassword);

    ServerResponse updatePassword(String emailNumber, String newPassword);

    void setRegisterStrategy(Integer strategy);

    EditorConfigDto getEditorConfig();

    EditorConfigDto updateEditorConfig(EditorConfigReq req);

    Page<UserVo> getUserList(Integer page, Integer size);

    ServerResponse<String> sendOutEmail(String email) throws Exception;
    ServerResponse<String> sendOutEmailRetreievePwd(String email) throws Exception;

}
