package ink.markidea.note.entity.req;

import lombok.Getter;
import lombok.Setter;

/**
 * 添加或删除笔记标签的请求体。
 * POST /api/note/tag
 * DELETE /api/note/tag
 */
@Getter
@Setter
public class NoteTagModifyRequest {
    String notebookName;

    String noteName;

    String tagName;
}
