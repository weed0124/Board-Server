package practice.springmvc.domain.board;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;
import practice.springmvc.domain.board.notrecommend.NotRecommend;
import practice.springmvc.domain.board.recommend.Recommend;
import practice.springmvc.domain.member.Member;

import java.util.Date;
import java.util.List;

@Component
@Getter @Setter
public class Board {
    private Long id;
    private String title;
    private String content;
    private Member member;
    private Date registDate;
    private Date updateDate;
    private int readCount;
    private List<Recommend> recommends;
    private List<NotRecommend> notRecommends;
    private List<Comment> comments;

    public Board() {
    }

    public Board(String title, String content, Member member) {
        this.title = title;
        this.content = content;
        this.member = member;
    }

    public int recommendsSize() {
        if (recommends != null) {
            return recommends.size();
        }
        return 0;
    }

    public int notRecommendsSize() {
        if (notRecommends != null) {
            return notRecommends.size();
        }
        return 0;
    }
}
