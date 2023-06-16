package ink.markidea.note.entity.req;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class UserEmailSignupRequest {
    private String emailNo;
}
