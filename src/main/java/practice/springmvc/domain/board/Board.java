package practice.springmvc.domain.board;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import practice.springmvc.domain.board.comment.Comment;
import practice.springmvc.domain.board.notrecommend.NotRecommend;
import practice.springmvc.domain.board.recommend.Recommend;
import practice.springmvc.domain.member.Member;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Component
@Getter @Setter
public class Board {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;

    @OneToOne(cascade = CascadeType.ALL)
    private Member member;
    private LocalDateTime registDate;
    private LocalDateTime updateDate;
    private int readCount;

    @OneToMany(mappedBy = "board")
    private List<Recommend> recommends;

    @OneToMany(mappedBy = "board")
    private List<NotRecommend> notRecommends;

    @OneToMany(mappedBy = "board")
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
