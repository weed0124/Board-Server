package practice.springmvc.domain.member;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "MEMBER_ID")
    private String id;
    private String nickname;
    private String password;
    private String address;

    public Member() {
    }

    public Member(String nickname) {
        this.nickname = nickname;
    }

    public Member(String nickname, String password) {
        this.nickname = nickname;
        this.password = password;
    }
}