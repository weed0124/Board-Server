package practice.springmvc.web.board.api;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.bridge.Message;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import practice.springmvc.domain.board.Board;
import practice.springmvc.domain.board.BoardSearchCond;
import practice.springmvc.domain.board.BoardService;

import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardApiController {

    private final BoardService boardService;

    @GetMapping("/boards/best")
    public ResponseEntity<Result> bestBoards() {
        List<BoardApiDTO> list = boardService.findAll(new BoardSearchCond()).stream()
                .filter(board -> board.getRecommends().size() >= 1)
                .map(BoardApiDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new Result(list));
    }

    @GetMapping("/boards/worst")
    public ResponseEntity<Result> worstBoards() {
        List<BoardApiDTO> list = boardService.findAll(new BoardSearchCond()).stream()
                .filter(board -> board.getNotRecommends().size() >= 1)
                .map(BoardApiDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new Result(list));
    }

    @Getter @Setter
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    @Getter @Setter
    @AllArgsConstructor
    static class BoardApiDTO {
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
            this.nickname = board.getMember().getNickname();
            this.ip = board.getMember().getIp();
            this.recommendCount = board.getRecommends().size();
            this.notRecommendCount = board.getNotRecommends().size();
        }
    }
}
