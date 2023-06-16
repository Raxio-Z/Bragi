package ink.markidea.note.entity.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;


@Getter
@Setter
@Accessors(chain = true)
public class NoteVo {

    public static final int STATUS_PRIVATE = 0;

    public static final int STATUS_PUBLIC = 1;

    public static final int STATUS_TMP_SAVED = 2;

    private String title;

    private int status =  STATUS_PRIVATE;

    private String content;

    private String previewContent;

    private String lastModifiedTime;

    private String createdTime;

    private String notebookName;

    private int searchCount;

    private Integer articleId;

    private List<String> tags;
    
    private Integer noteId;

    public Integer fileSize;
}
