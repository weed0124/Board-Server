package practice.springmvc.domain.board;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import practice.springmvc.domain.board.comment.Comment;
import practice.springmvc.domain.board.notrecommend.NotRecommend;
import practice.springmvc.domain.board.recommend.Recommend;
import practice.springmvc.domain.member.Member;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
        List<Recommend> list = Optional.ofNullable(recommends).orElseGet(() -> new ArrayList<>());
        return list.size();
    }

    public int notRecommendsSize() {
        List<NotRecommend> list = Optional.ofNullable(notRecommends).orElseGet(() -> new ArrayList<>());
        return list.size();
    }

    public int commentsSize() {
        List<Comment> list = Optional.ofNullable(comments).orElseGet(() -> new ArrayList<>());
        return list.size();
    }
}
