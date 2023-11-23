package practice.springmvc.domain.board;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BoardSearchCond {

    private String title;
    private String nickname;

    public BoardSearchCond() {
    }

    public BoardSearchCond(String title, String nickname) {
        this.title = title;
        this.nickname = nickname;
    }
}
