package practice.springmvc.domain.board;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter @Setter
public class Board {
    private Long id;
    private String title;
    private String content;
    private String writer;
    private String password;
    private Date registDate;
    private Date updateDate;
    private int readCount;
    private int recommendCount;
    private List<Comment> comments;
}
