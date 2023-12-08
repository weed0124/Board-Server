package practice.springmvc.domain.member;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import practice.springmvc.domain.board.Board;
import practice.springmvc.domain.board.comment.Comment;
import practice.springmvc.domain.board.notrecommend.NotRecommend;
import practice.springmvc.domain.board.recommend.Recommend;

import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "MEMBER_ID")
    private String id;
    private String nickname;
    private String password;
    private String address;
    private String ip;

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