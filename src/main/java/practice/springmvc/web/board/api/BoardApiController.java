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
import practice.springmvc.annotation.LoginCheck;
import practice.springmvc.domain.PagedModelUtil;
import practice.springmvc.domain.board.Board;
import practice.springmvc.domain.board.BoardSearchCond;
import practice.springmvc.domain.board.BoardService;
import practice.springmvc.dto.BoardDTO;
import practice.springmvc.dto.response.ResultResponse;
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

    @LoginCheck
    @GetMapping("/best")
    public ResponseEntity<EntityModel<ResultResponse>> bestBoards() {
        List<BoardApiDTO> list = boardService.findAll(new BoardSearchCond()).stream()
                .filter(board -> board.getRecommends().size() >= 3)
                .map(BoardApiDTO::new)
                .collect(Collectors.toList());

        EntityModel entityModel = EntityModel.of(new ResultResponse(list));
        WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).bestBoards());
        entityModel.add(linkTo.withSelfRel())
                .add(linkTo(HomeController.class).withRel("home"));

        return ResponseEntity.ok().body(entityModel);
    }

    @LoginCheck
    @GetMapping("/worst")
    public ResponseEntity<EntityModel<ResultResponse>> worstBoards() {
        List<BoardApiDTO> list = boardService.findAll(new BoardSearchCond()).stream()
                .filter(board -> board.getNotRecommends().size() >= 3)
                .map(BoardApiDTO::new)
                .collect(Collectors.toList());

        EntityModel entityModel = EntityModel.of(new ResultResponse(list));
        WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).worstBoards());
        entityModel.add(linkTo.withSelfRel())
                .add(linkTo(HomeController.class).withRel("home"));

        return ResponseEntity.ok().body(entityModel);
    }

    @LoginCheck
    @PostMapping("/add")
    public ResponseEntity<ResultResponse> saveBoard(@RequestBody BoardWriteApiDTO boardWriteApiDTO, HttpServletRequest request) {
        Board board = boardService.save(new Board(boardWriteApiDTO.getTitle(),
                boardWriteApiDTO.getContent(),
                boardWriteApiDTO.getNickname(), boardWriteApiDTO.getPassword(), boardWriteApiDTO.getIp()));

        URI location = ServletUriComponentsBuilder.fromContextPath(request)
                .path("/api/board/{id}")
                .buildAndExpand(board.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @LoginCheck
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<BoardDTO>>> listBoard(@RequestBody BoardSearchCond cond, @PageableDefault(size = 5) Pageable pageable, HttpServletRequest request) {
        Page<BoardDTO> boardList = boardService.findPagingAllV2(cond, pageable);

        PagedModel<EntityModel<BoardDTO>> entityModels = PagedModelUtil.getEntityModels(assembler,
                boardList,
                linkTo(methodOn(this.getClass()).listBoard(cond, null, request)),
                BoardDTO::getId);
        return ResponseEntity.ok().body(entityModels);
    }

    @LoginCheck
    @GetMapping("{id}")
    public ResponseEntity<EntityModel<ResultResponse>> readBoard(@PathVariable Long id, HttpServletRequest request) {
        EntityModel<ResultResponse> entityModel = EntityModel.of(new ResultResponse<>(new BoardApiDTO(boardService.findById(id).orElseThrow())));
        WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).readBoard(id, request));
        entityModel.add(linkTo.withSelfRel())
                .add(linkTo(methodOn(this.getClass()).recommend(id, request)).withRel("recommend"))
                .add(linkTo(methodOn(this.getClass()).notRecommend(id, request)).withRel("notRecommend"))
                .add(linkTo(HomeController.class).withRel("home"));

        return ResponseEntity.ok().body(entityModel);
    }

    @LoginCheck
    @PutMapping("{id}")
    public ResponseEntity<ResultResponse> editBoard(@PathVariable Long id, @RequestBody BoardUpdateForm form) {
        Board board = boardService.findById(id).orElseThrow();

        String password = form.getPassword();
        if (!board.getPassword().equals(password)) {
            throw new PasswordInvalidException("비밀번호가 맞지 않습니다.");
        }

        board.setTitle(form.getTitle());
        board.setContent(form.getContent());

        boardService.update(board.getId(), board);
        return ResponseEntity.ok(new ResultResponse(new BoardApiDTO(board)));
    }

    @LoginCheck
    @DeleteMapping("{id}")
    public void deleteBoard(@PathVariable Long id) {
        boardService.delete(id);
    }

    @LoginCheck
    @GetMapping("{id}/recommend")
    public ResponseEntity<ResultResponse> recommend(@PathVariable Long id, HttpServletRequest request) {
        return ResponseEntity.ok(new ResultResponse(new BoardApiDTO(boardService.recommend(boardService.findById(id).orElseThrow(), request))));
    }

    @LoginCheck
    @GetMapping("{id}/notrecommend")
    public ResponseEntity<ResultResponse> notRecommend(@PathVariable Long id, HttpServletRequest request) {
        return ResponseEntity.ok(new ResultResponse(new BoardApiDTO(boardService.notRecommend(boardService.findById(id).orElseThrow(), request))));
    }
}
