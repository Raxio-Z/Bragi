package ink.markidea.note.entity.req;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;


@Getter
@Setter
@Accessors(chain = true)
public class NoteRequest {

    private Integer delNoteId;

    private String noteTitle;

    private String notebookName;

    private String content;

    private String lastRef;

    private String versionRef;


    private String srcNotebook;

    private String srcTitle;

    private Boolean move;

    private boolean tmpSave = false;


}
