package practice.springmvc.domain.member;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Member {
    private String id;
    private String nickname;
    private String password;
    private String address;
    private String ip;

    public Member() {
    }

    public Member(String nickname, String password) {
        this.nickname = nickname;
        this.password = password;
    }

    public Member(String nickname, String password, String ip) {
        this.nickname = nickname;
        this.password = password;
        this.ip = ip;
    }
}