package practice.springmvc.domain.board;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import practice.springmvc.domain.member.Member;

import java.util.Date;
import java.util.List;

@Getter @Setter
public class Board {
    private Long id;

    private String title;
    private String content;
    private Member member;
    private Date registDate;
    private Date updateDate;
    private int readCount;
    private int recommendCount;
    private List<Comment> comments;

    public Board() {
    }

    public Board(String title, String content, Member member) {
        this.title = title;
        this.content = content;
        this.member = member;
    }
}
