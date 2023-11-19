package practice.springmvc.domain.board.comment;

import lombok.Getter;
import lombok.Setter;
import practice.springmvc.domain.member.Member;

import java.time.LocalDate;

@Getter @Setter
public class Comment {
    private Long id;
    private Long boardId;
    private Member member;
    /*
    private String register;
    private String password;
    */
    private String content;
    private LocalDate registDate;
}
