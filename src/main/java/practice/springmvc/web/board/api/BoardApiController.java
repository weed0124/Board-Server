package practice.springmvc.web.board.api;


import jakarta.servlet.http.HttpServletRequest;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import practice.springmvc.domain.board.Board;
import practice.springmvc.domain.board.BoardSearchCond;
import practice.springmvc.domain.board.BoardService;
import practice.springmvc.domain.member.Member;

import java.net.URI;
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

    @PostMapping("/add")
    public ResponseEntity<Board> saveBoard(@RequestBody BoardWriteApiDTO boardWriteApiDTO, HttpServletRequest request) {
        Board board = boardService.save(new Board(boardWriteApiDTO.getTitle(),
                boardWriteApiDTO.getContent(),
                new Member(boardWriteApiDTO.getNickname(), boardWriteApiDTO.getPassword(), boardWriteApiDTO.getIp())));

        URI location = ServletUriComponentsBuilder.fromContextPath(request)
                .path("/api/board/{id}")
                .buildAndExpand(board.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("{id}")
    public BoardApiDTO readBoard(@PathVariable Long id) {
        return new BoardApiDTO(boardService.findById(id).orElseThrow());
    }

    @Getter
    @Setter
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    @Getter @Setter
    @AllArgsConstructor
    @NoArgsConstructor
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

    @Getter @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    static class BoardWriteApiDTO {
        Long id;
        String title;
        String content;
        String nickname;
        String password;
        String ip;
    }
}
