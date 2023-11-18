package practice.springmvc.web.board.form;

import lombok.Getter;
import lombok.Setter;
import practice.springmvc.domain.member.Member;

@Getter @Setter
public class BoardUpdateForm {
    private String title;
    private String content;
    private Member member;
}
