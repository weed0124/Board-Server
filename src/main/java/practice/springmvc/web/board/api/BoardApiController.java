package practice.springmvc.web.board.api;


import jakarta.servlet.http.HttpServletRequest;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import practice.springmvc.domain.PagedModelUtil;
import practice.springmvc.domain.board.Board;
import practice.springmvc.domain.board.BoardSearchCond;
import practice.springmvc.domain.board.BoardService;
import practice.springmvc.dto.BoardDTO;
import practice.springmvc.dto.response.Result;
import practice.springmvc.exception.PasswordInvalidException;
import practice.springmvc.web.HomeController;
import practice.springmvc.web.board.form.BoardUpdateForm;
import practice.springmvc.dto.BoardWriteApiDTO;
import practice.springmvc.dto.BoardApiDTO;

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
    private final PagedResourcesAssembler assembler;

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
                boardWriteApiDTO.getNickname(), boardWriteApiDTO.getPassword(), boardWriteApiDTO.getIp()));

        URI location = ServletUriComponentsBuilder.fromContextPath(request)
                .path("/api/board/{id}")
                .buildAndExpand(board.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<BoardDTO>>> listBoard(@RequestBody BoardSearchCond cond, @PageableDefault(size = 5) Pageable pageable, HttpServletRequest request) {
        Page<BoardDTO> boardList = boardService.findPagingAllV2(cond, pageable);

        PagedModel<EntityModel<BoardDTO>> entityModels = PagedModelUtil.getEntityModels(assembler,
                boardList,
                linkTo(methodOn(this.getClass()).listBoard(cond, null, request)),
                BoardDTO::getId);
        return ResponseEntity.ok().body(entityModels);
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

        String password = form.getPassword();
        if (!board.getPassword().equals(password)) {
            throw new PasswordInvalidException("비밀번호가 맞지 않습니다.");
        }

        board.setTitle(form.getTitle());
        board.setContent(form.getContent());

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
}
