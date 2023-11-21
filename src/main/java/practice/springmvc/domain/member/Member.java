package practice.springmvc.domain.member;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import practice.springmvc.domain.board.comment.Comment;
import practice.springmvc.domain.board.notrecommend.NotRecommend;
import practice.springmvc.domain.board.recommend.Recommend;

import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String nickname;
    private String password;
    private String address;
    private String ip;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "member")
    private List<Recommend> recommends;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "member")
    private List<NotRecommend> notRecommends;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "member")
    private List<Comment> comments;

    public Member() {
    }

    public Member(String ip) {
        this.ip = ip;
    }

    public Member(String nickname, String ip) {
        this.nickname = nickname;
        this.ip = ip;
    }

    public Member(String nickname, String password, String ip) {
        this.nickname = nickname;
        this.password = password;
        this.ip = ip;
    }
}