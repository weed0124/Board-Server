package practice.springmvc.domain.board.recommend;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import practice.springmvc.domain.board.Board;
import practice.springmvc.domain.member.Member;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Recommend {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn
    private Member member;

    @ManyToOne
    @JoinColumn
    private Board board;

    private LocalDateTime registDate;

    public Recommend() {
    }

    public Recommend(Board board, Member member, LocalDateTime registDate) {
        this.board = board;
        this.member = member;
        this.registDate = registDate;
    }
}
