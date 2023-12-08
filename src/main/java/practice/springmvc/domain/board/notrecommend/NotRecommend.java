package practice.springmvc.domain.board.notrecommend;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import practice.springmvc.domain.board.Board;
import practice.springmvc.domain.member.Member;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class NotRecommend {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NOT_RECOMMEND_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;
    private LocalDateTime registDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_ID")
    private Board board;


    public NotRecommend() {
    }

    public NotRecommend(Board board, Member member, LocalDateTime registDate) {
        this.board = board;
        this.member = member;
        this.registDate = registDate;
    }
}
