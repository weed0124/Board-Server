package practice.springmvc.web.board.api;


import jakarta.servlet.http.HttpServletRequest;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
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
import practice.springmvc.web.HomeController;
import practice.springmvc.web.board.form.BoardUpdateForm;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardApiController {

    private final BoardService boardService;

    @GetMapping("/best")
    public ResponseEntity<EntityModel<Result>> bestBoards() {
        List<BoardApiDTO> list = boardService.findAll(new BoardSearchCond()).stream()
                .filter(board -> board.getRecommends().size() >= 3)
                .map(BoardApiDTO::new)
                .collect(Collectors.toList());

        EntityModel entityModel = EntityModel.of(new Result(list));
        WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).bestBoards());
        entityModel.add(linkTo.withSelfRel())
                .add(linkTo(HomeController.class).withRel("home"));

        return ResponseEntity.ok().body(entityModel);
    }

    @GetMapping("/worst")
    public ResponseEntity<EntityModel<Result>> worstBoards() {
        List<BoardApiDTO> list = boardService.findAll(new BoardSearchCond()).stream()
                .filter(board -> board.getNotRecommends().size() >= 3)
                .map(BoardApiDTO::new)
                .collect(Collectors.toList());

        EntityModel entityModel = EntityModel.of(new Result(list));
        WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).worstBoards());
        entityModel.add(linkTo.withSelfRel())
                .add(linkTo(HomeController.class).withRel("home"));

        return ResponseEntity.ok().body(entityModel);
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
    public ResponseEntity<EntityModel<Result>> listBoard(@PageableDefault(size = 5) Pageable pageable, HttpServletRequest request) {
        PageCustom<BoardDTO> boardList = boardService.findPagingAll(new BoardSearchCond(), pageable);

        List<EntityModel<BoardDTO>> list = boardList.getContent().stream().map(b -> {
            return EntityModel.of(b).add(linkTo(methodOn(BoardApiController.class).readBoard(b.getId(), request)).withRel("detail"));
        }).toList();

        EntityModel<Result> entityModel = EntityModel.of(new Result<>(list));
        WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).listBoard(pageable, request));
        entityModel.add(linkTo.withSelfRel())
                    .add(linkTo(HomeController.class).withRel("home"));

        return ResponseEntity.ok().body(entityModel);
    }

    @GetMapping("{id}")
    public ResponseEntity<EntityModel<Result>> readBoard(@PathVariable Long id, HttpServletRequest request) {
        EntityModel<Result> entityModel = EntityModel.of(new Result<>(new BoardApiDTO(boardService.findById(id).orElseThrow())));
        WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).readBoard(id, request));
        entityModel.add(linkTo.withSelfRel())
                .add(linkTo(methodOn(this.getClass()).recommend(id, request)).withRel("recommend"))
                .add(linkTo(methodOn(this.getClass()).notRecommend(id, request)).withRel("notRecommend"))
                .add(linkTo(HomeController.class).withRel("home"));

        return ResponseEntity.ok().body(entityModel);
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

    @DeleteMapping("{id}")
    public void deleteBoard(@PathVariable Long id) {
        boardService.delete(id);
    }

    @GetMapping("{id}/recommend")
    public ResponseEntity<Result> recommend(@PathVariable Long id, HttpServletRequest request) {
        return ResponseEntity.ok(new Result(new BoardApiDTO(boardService.recommend(boardService.findById(id).orElseThrow(), request))));
    }

    @GetMapping("{id}/notrecommend")
    public ResponseEntity<Result> notRecommend(@PathVariable Long id, HttpServletRequest request) {
        return ResponseEntity.ok(new Result(new BoardApiDTO(boardService.notRecommend(boardService.findById(id).orElseThrow(), request))));
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
