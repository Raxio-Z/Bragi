package ink.markidea.note.entity.req;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

public class FileUploadRequest {
    @Getter @Setter private String file;

    @Getter @Setter private String name;

    @Getter @Setter private String directory;
}
