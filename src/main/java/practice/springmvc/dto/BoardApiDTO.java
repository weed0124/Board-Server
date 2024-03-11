package practice.springmvc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import practice.springmvc.domain.board.Board;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardApiDTO {
    Long id;
    String title;
    String content;
    String nickname;
    String ip;
    int recommendCount;
    int notRecommendCount;

    public BoardApiDTO(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.nickname = board.getNickname();
        this.ip = board.getIp();
        this.recommendCount = board.getRecommends().size();
        this.notRecommendCount = board.getNotRecommends().size();
    }

    public BoardApiDTO(BoardDTO boardDTO) {
        this.id = boardDTO.getId();
        this.title = boardDTO.getTitle();
        this.content = boardDTO.getContent();
        this.nickname = boardDTO.getNickname();
        this.ip = boardDTO.getIp();
        this.recommendCount = boardDTO.getRecommendCount();
        this.notRecommendCount = boardDTO.getNotRecommendCount();
    }
}