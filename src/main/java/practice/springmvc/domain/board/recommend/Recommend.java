package practice.springmvc.domain.board.recommend;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import practice.springmvc.domain.member.Member;

import java.time.LocalDate;

@Getter @Setter
public class Recommend {
    private Long id;
    private Long boardId;
    private Member member;
    private LocalDate registDate;

    public Recommend() {
    }

    public Recommend(Long boardId, Member member, LocalDate registDate) {
        this.boardId = boardId;
        this.member = member;
        this.registDate = registDate;
    }
}
