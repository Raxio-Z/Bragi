package ink.markidea.note.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Entity
@Table(name = "notetag")
@Getter
@Setter
@Accessors(chain = true)
public class NoteTagDo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "note_id", nullable = false)
    private Integer noteId;

    @Column(name = "tag_name", nullable = false)
    private String tagName;
}
