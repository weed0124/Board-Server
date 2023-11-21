package practice.springmvc.domain.board.comment;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import practice.springmvc.domain.board.Board;
import practice.springmvc.domain.member.Member;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Comment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn
    private Member member;

    @ManyToOne
    @JoinColumn
    private Board board;

    private String content;
    private LocalDateTime registDate;

    public Comment() {
    }
}
