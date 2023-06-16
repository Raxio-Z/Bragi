package ink.markidea.note.entity.req;

import ink.markidea.note.entity.vo.ArticleVo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class BatchDelArticleReq {


    private List<ArticleVo> articleList;

}
