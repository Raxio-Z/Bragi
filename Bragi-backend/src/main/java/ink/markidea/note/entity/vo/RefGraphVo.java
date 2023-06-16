package ink.markidea.note.entity.vo;


//import javafx.util.Pair;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
public class RefGraphVo {

    List<Node> nodes;

    List<Line> lines;


    @Getter
    @Setter
    @Accessors(chain = true)
    public static class Node{
        String id;
        String text;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    public static class Line{
        String from;
        String to;
    }


}
