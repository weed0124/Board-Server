package practice.springmvc.web.board.api;


import jakarta.servlet.http.HttpServletRequest;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import practice.springmvc.domain.PageCustom;
import practice.springmvc.domain.board.Board;
import practice.springmvc.domain.board.BoardSearchCond;
import practice.springmvc.domain.board.BoardService;
import practice.springmvc.domain.board.dto.BoardDTO;
import practice.springmvc.domain.member.Member;
import practice.springmvc.exception.PasswordInvalidException;
import practice.springmvc.web.board.form.BoardUpdateForm;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardApiController {

    private final BoardService boardService;

    @GetMapping("/best")
    public ResponseEntity<Result> bestBoards() {
        List<BoardApiDTO> list = boardService.findAll(new BoardSearchCond()).stream()
                .filter(board -> board.getRecommends().size() >= 1)
                .map(BoardApiDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new Result(list));
    }

    @GetMapping("/worst")
    public ResponseEntity<Result> worstBoards() {
        List<BoardApiDTO> list = boardService.findAll(new BoardSearchCond()).stream()
                .filter(board -> board.getNotRecommends().size() >= 1)
                .map(BoardApiDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new Result(list));
    }

    @PostMapping("/add")
    public ResponseEntity<Result> saveBoard(@RequestBody BoardWriteApiDTO boardWriteApiDTO, HttpServletRequest request) {
        Board board = boardService.save(new Board(boardWriteApiDTO.getTitle(),
                boardWriteApiDTO.getContent(),
                new Member(boardWriteApiDTO.getNickname(), boardWriteApiDTO.getPassword(), boardWriteApiDTO.getIp())));

        URI location = ServletUriComponentsBuilder.fromContextPath(request)
                .path("/api/board/{id}")
                .buildAndExpand(board.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping
    public ResponseEntity<Result> listBoard(@PageableDefault(size = 5) Pageable pageable) {
        PageCustom<BoardDTO> boardList = boardService.findPagingAll(new BoardSearchCond(), pageable);
        return ResponseEntity.ok(new Result(boardList.getContent().stream().map(BoardApiDTO::new).toList()));
    }

    @GetMapping("{id}")
    public ResponseEntity<Result> readBoard(@PathVariable Long id) {
        return ResponseEntity.ok(new Result(new BoardApiDTO(boardService.findById(id).orElseThrow())));
    }

    @PutMapping("{id}")
    public ResponseEntity<Result> editBoard(@PathVariable Long id, @RequestBody BoardUpdateForm form) {
        Board board = boardService.findById(id).orElseThrow();

        String password = form.getMember().getPassword();
        if (!board.getMember().getPassword().equals(password)) {
            throw new PasswordInvalidException("비밀번호가 맞지 않습니다.");
        }

        board.setTitle(form.getTitle());
        board.setContent(form.getContent());
        board.setMember(form.getMember());

        boardService.update(board.getId(), board);
        return ResponseEntity.ok(new Result(new BoardApiDTO(board)));
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
