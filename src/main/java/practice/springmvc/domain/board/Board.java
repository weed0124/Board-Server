package practice.springmvc.domain.board;

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
    /*
    private String writer;
    private String password;
    private String ip;
    */
    private Date registDate;
    private Date updateDate;
    private int readCount;
    private int recommendCount;
    private List<Comment> comments;
}
