package ink.markidea.note.entity.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;


@Getter
@Setter
@Accessors(chain = true)
public class NotebookVo {

    private String notebookName;

    private List<NoteVo> noteList;

}
