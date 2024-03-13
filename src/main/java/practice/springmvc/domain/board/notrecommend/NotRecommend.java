package practice.springmvc.domain.board.notrecommend;

import jakarta.persistence.*;
import lombok.*;
import practice.springmvc.domain.board.Board;
import practice.springmvc.domain.entity.BaseTimeEntity;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotRecommend extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NOT_RECOMMEND_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_ID")
    private Board board;

    private String ip;

    public NotRecommend(Board board, String ip) {
        this.board = board;
        this.ip = ip;
    }
}
