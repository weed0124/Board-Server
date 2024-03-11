package practice.springmvc.domain.member;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import practice.springmvc.domain.entity.BaseTimeEntity;

@Entity
@Getter @Setter
@DynamicInsert
public class Member extends BaseTimeEntity {

    public enum Status {
        WORKING, EXPIRED
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;
    private String loginId;
    private String password;
    private String nickname;
    private String address;

    @Column(nullable = false)
    @ColumnDefault("'WORKING'")
    @Enumerated(EnumType.STRING)
    private Status status;

    public Member() {
    }

    public Member(String nickname) {
        this.nickname = nickname;
    }

    public Member(String nickname, String password) {
        this.nickname = nickname;
        this.password = password;
    }

    public Member(String loginId, String password, String nickname, String address, Status status) {
        this.loginId = loginId;
        this.password = password;
        this.nickname = nickname;
        this.address = address;
        this.status = status;
    }
}