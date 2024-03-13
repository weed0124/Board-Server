package practice.springmvc.domain.board.comment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import practice.springmvc.domain.board.Board;
import practice.springmvc.domain.entity.BaseTimeEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Comment extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_ID")
    private Long id;

    private String nickname;
    private String password;
    private String ip;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_ID")
    @ToString.Exclude
    private Board board;

    @Lob
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_COMMENT_ID", updatable = false)
    @ToString.Exclude
    @JsonIgnore
    private Comment parent;

    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();

    public Comment(Long id, String nickname, String password, String ip, Board board, String content) {
        this.id = id;
        this.nickname = nickname;
        this.password = password;
        this.ip = ip;
        this.board = board;
        this.content = content;
    }

    public Comment(Long id, String nickname, String password, String ip, Board board, String content, Comment parent) {
        this.id = id;
        this.nickname = nickname;
        this.password = password;
        this.ip = ip;
        this.board = board;
        this.content = content;
        this.parent = parent;
    }

    public void updateParent(Comment parent) {
        this.setParent(parent);
        parent.getChildren().add(this);
    }
}
