package ink.markidea.note.entity.req;

import lombok.Getter;
import lombok.Setter;

/**
 * 根据笔记获取标签列表的请求体。
 * GET /api/note/tag
 */
@Getter @Setter
public class NoteTagFetchRequest {
    String notebookName;

    String noteName;
}
