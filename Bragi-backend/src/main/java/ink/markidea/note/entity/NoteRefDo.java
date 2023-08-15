package ink.markidea.note.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Entity
@Table(name = "noteref")
@Getter
@Setter
@Accessors(chain = true)
public class NoteRefDo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String username;

    @Column(name = "note_id", nullable = false)
    private Integer noteId;

    @Column(name = "refnote_id", nullable = false)
    private Integer refNoteId;
}
