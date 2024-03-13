package practice.springmvc.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardDTO implements Serializable {
    private Long id;
    private String title;
    private String content;
    private String nickname;
    private String ip;
    private LocalDateTime createdDate;
    private int readCount;
    private int recommendCount;
    private int notRecommendCount;

    @QueryProjection
    public BoardDTO(Long id, String title, String nickname, String ip, LocalDateTime createdDate, int readCount) {
        this.id = id;
        this.title = title;
        this.nickname = nickname;
        this.ip = ip;
        this.createdDate = createdDate;
        this.readCount = readCount;
    }

    public BoardDTO(Long id, String title, String content, String nickname, String ip, LocalDateTime createdDate, int readCount, int recommendCount, int notRecommendCount) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.nickname = nickname;
        this.ip = ip;
        this.createdDate = createdDate;
        this.readCount = readCount;
        this.recommendCount = recommendCount;
        this.notRecommendCount = notRecommendCount;
    }

    @QueryProjection
    public BoardDTO(Long id, String title, String nickname, String ip, LocalDateTime createdDate, int readCount, int recommendCount, int notRecommendCount) {
        this.id = id;
        this.title = title;
        this.nickname = nickname;
        this.ip = ip;
        this.createdDate = createdDate;
        this.readCount = readCount;
        this.recommendCount = recommendCount;
        this.notRecommendCount = notRecommendCount;
    }
}
