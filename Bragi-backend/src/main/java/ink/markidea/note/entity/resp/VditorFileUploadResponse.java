package ink.markidea.note.entity.resp;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;



@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VditorFileUploadResponse {

    private String msg;

    private int code;

    private Map<String, Object> data;

}
