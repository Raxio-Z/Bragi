package ink.markidea.note.entity.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class NotePreviewInfo {

    String previewContent;

    /**
     * 是否为公开笔记
     */
    Integer articleId;

}
