package ink.markidea.note.entity.req;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ArticleCreateReq {

    private String notebookName;

    private String noteTitle;
}
