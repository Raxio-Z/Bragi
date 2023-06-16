package ink.markidea.note.entity.req;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class BatchFileDeleteRequest {

    private String username;

    private List<String> fileNames;

}
