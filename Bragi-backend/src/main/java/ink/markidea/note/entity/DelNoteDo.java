package ink.markidea.note.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "del_note")
@Getter
@Setter
@Accessors(chain = true)
public class DelNoteDo {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String notebook;

    @Column(nullable = false)
    private String content;

    @Column(name = "last_ref", nullable = false)
    private String lastRef;

    @Column(nullable = false)
    private String username;

    @Column(name = "delete_time", nullable = false)
    private String deleteTime;

    @Column(name = "create_time", nullable = false)
    private String createTime;
}
