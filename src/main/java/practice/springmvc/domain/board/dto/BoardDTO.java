package practice.springmvc.domain.board.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import practice.springmvc.domain.member.Member;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardDTO {
    private Long id;
    private String title;
    private Member member;
    private LocalDateTime registDate;
    private LocalDateTime updateDate;
    private int readCount;
    private int recommendCount;
    private int notRecommendCount;
    private int commentCount;

    @QueryProjection
    public BoardDTO(Long id, String title, Member member, LocalDateTime registDate, LocalDateTime updateDate, int readCount, int recommendCount, int notRecommendCount, int commentCount) {
        this.id = id;
        this.title = title;
        this.member = member;
        this.registDate = registDate;
        this.updateDate = updateDate;
        this.readCount = readCount;
        this.recommendCount = recommendCount;
        this.notRecommendCount = notRecommendCount;
        this.commentCount = commentCount;
    }
}
