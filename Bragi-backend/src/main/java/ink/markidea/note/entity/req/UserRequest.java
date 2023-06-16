package ink.markidea.note.entity.req;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;


@Getter
@Setter
@Accessors(chain = true)
public class UserRequest {

    private String username;

    private String password;

    private String token;

    private String newPassword;

    private String emailnumber;
    //验证码
    private String code;
    //用户登录方式 usernamelogin为用户名 emaillogin是邮箱
    private String flag;
}
