package ink.markidea.note.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Entity
@Table(name = "note")
@Getter
@Setter
@Accessors(chain = true)
public class NoteDo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String username;

    // 笔记本名
    @Column(name = "notebook", nullable = false)
    private String notebookName;

    // 笔记名
    @Column(name = "note_title", nullable = false)
    private String noteTitle;
}
