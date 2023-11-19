package practice.springmvc.domain.board.notrecommend;

import lombok.Getter;
import lombok.Setter;
import practice.springmvc.domain.member.Member;

import java.time.LocalDate;

@Getter @Setter
public class NotRecommend {
    private Long id;
    private Long boardId;
    private Member member;
    private LocalDate registDate;

    public NotRecommend() {
    }

    public NotRecommend(Long boardId, Member member, LocalDate registDate) {
        this.boardId = boardId;
        this.member = member;
        this.registDate = registDate;
    }
}
