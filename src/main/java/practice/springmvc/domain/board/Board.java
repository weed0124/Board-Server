package practice.springmvc.domain.board;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import practice.springmvc.domain.board.comment.Comment;
import practice.springmvc.domain.board.notrecommend.NotRecommend;
import practice.springmvc.domain.board.recommend.Recommend;
import practice.springmvc.domain.entity.BaseTimeEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Board extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_ID")
    private Long id;
    private String title;

    @Lob
    private String content;

    private String nickname;
    private String password;
    private String ip;
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

    public Board(String title, String content, String nickname, String password, String ip) {
        this.title = title;
        this.content = content;
        this.nickname = nickname;
        this.password = password;
        this.ip = ip;
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
