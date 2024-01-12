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
    @Column(name = "BOARD_ID")
    private Long id;
    private String title;

    @Lob
    private String content;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;
    private LocalDateTime registDate;
    private LocalDateTime updateDate;
    private int readCount;

    @OneToMany(mappedBy = "board", orphanRemoval = true)
    private List<Recommend> recommends = new ArrayList<>();

    public void addRecommend(Recommend rec) {
        recommends.add(rec);
        rec.setBoard(this);
    }

    @OneToMany(mappedBy = "board", orphanRemoval = true)
    private List<NotRecommend> notRecommends = new ArrayList<>();

    public void addNotRecommend(NotRecommend notRec) {
        notRecommends.add(notRec);
        notRec.setBoard(this);
    }

    @OneToMany(mappedBy = "board", orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setBoard(this);
    }

    public Board() {
    }

    public Board(String title, String content, Member member) {
        this.title = title;
        this.content = content;
        this.member = member;
    }

    public int recommendsSize() {
        return recommends.size();
    }

    public int notRecommendsSize() {
        return notRecommends.size();
    }

    public int commentsSize() {
        return comments.size();
    }
}
