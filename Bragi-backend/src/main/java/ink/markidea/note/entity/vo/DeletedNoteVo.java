package ink.markidea.note.entity.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;


@Getter
@Setter
@Accessors(chain = true)
public class DeletedNoteVo {

    private Integer id;

    private String title;

    private String notebook;

    private String lastRef;

    private String username;

    private String content;

    private String deleteTime;

    private String createTime;

    private Integer fileSize;
}
