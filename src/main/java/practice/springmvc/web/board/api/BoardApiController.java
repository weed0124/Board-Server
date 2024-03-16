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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import practice.springmvc.annotation.LoginCheck;
import practice.springmvc.annotation.Trace;
import practice.springmvc.domain.PagedModelUtil;
import practice.springmvc.domain.board.Board;
import practice.springmvc.domain.board.BoardSearchCond;
import practice.springmvc.domain.board.BoardService;
import practice.springmvc.domain.board.comment.Comment;
import practice.springmvc.domain.board.tag.Tag;
import practice.springmvc.dto.BoardDTO;
import practice.springmvc.dto.request.CommentRequest;
import practice.springmvc.dto.request.TagRequest;
import practice.springmvc.dto.response.CommentResponse;
import practice.springmvc.dto.response.ResultResponse;
import practice.springmvc.dto.response.TagResponse;
import practice.springmvc.exception.PasswordInvalidException;
import practice.springmvc.utils.SHA256Util;
import practice.springmvc.web.HomeController;
import practice.springmvc.web.board.form.BoardUpdateForm;
import practice.springmvc.dto.BoardWriteApiDTO;
import practice.springmvc.dto.BoardApiDTO;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static practice.springmvc.utils.SHA256Util.*;

@Slf4j
@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardApiController {

    private final ResponseEntity<ResultResponse> FAIL_RESPONSE = new ResponseEntity<ResultResponse>(HttpStatus.BAD_REQUEST);
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
        Page<BoardDTO> boardList = boardService.findPagingAll(cond, pageable);

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

    @PostMapping("/comments")
    public ResponseEntity saveComment(@RequestBody CommentRequest commentRequest, HttpServletRequest request) {
        Comment savedComment = null;

        if (commentRequest.getParentId() != null) {
            savedComment = boardService.saveComment(new Comment(commentRequest.getId(),
                    commentRequest.getNickname(),
                    commentRequest.getPassword(),
                    boardService.getRemoteIp(request),
                    boardService.findById(commentRequest.getBoardId()).orElseThrow(),
                    commentRequest.getContent(),
                    boardService.findByCommentId(commentRequest.getParentId())
            ));
        } else {
            savedComment = boardService.saveComment(new Comment(commentRequest.getId(),
                    commentRequest.getNickname(),
                    commentRequest.getPassword(),
                    boardService.getRemoteIp(request),
                    boardService.findById(commentRequest.getBoardId()).orElseThrow(),
                    commentRequest.getContent()
            ));
        }

        URI location = ServletUriComponentsBuilder.fromContextPath(request)
                .path("/api/board/{id}")
                .buildAndExpand(savedComment.getBoard().getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @Trace
    @GetMapping("{id}/comments")
    public ResponseEntity<ResultResponse> commentsByBoard(@PathVariable Long id) {
        List<CommentResponse> result = new ArrayList<>();
        Map<Long, CommentResponse> map = new HashMap<>();

        boardService.listComment(id).stream().forEach(c -> {
            c.setPassword(encryptSHA256(c.getPassword()));
            CommentResponse dto = new CommentResponse(c);
            map.put(dto.getId(), dto);
            if (c.getParent() != null) {
                map.get(c.getParent().getId()).getChildren().add(c);
            } else {
                result.add(dto);
            }
        });

        return ResponseEntity.ok(new ResultResponse(result));
    }

    @PutMapping("/comments/{id}")
    public ResponseEntity<ResultResponse> editComment(@PathVariable Long id, @RequestBody CommentRequest commentRequest, HttpServletRequest request) {
        Comment comment = boardService.findByCommentId(id);
        if (!comment.getPassword().equals(encryptSHA256(commentRequest.getPassword()))) {
            throw new RuntimeException("패스워드가 일치하지 않습니다.");
        }
        Comment updatedComment = boardService.editComment(comment, commentRequest, request);
        return ResponseEntity.ok(new ResultResponse(new CommentResponse(updatedComment)));
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<ResultResponse> deleteComment(@PathVariable Long id, @RequestBody CommentRequest commentRequest) {
        ResponseEntity<ResultResponse> result = null;

        try {
            boardService.deleteComment(id, commentRequest.getPassword());
            result = ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            log.info("delete Comment FAIL");
            result = FAIL_RESPONSE;
        }
        return result;
    }

    @PostMapping("/tags")
    public ResponseEntity saveTag(@RequestBody TagRequest tagRequest, HttpServletRequest request) {
        Tag tag = boardService.saveTag(new Tag(tagRequest.getId(),
                tagRequest.getName(),
                tagRequest.getUrl(),
                boardService.findById(tagRequest.getBoardId()).orElseThrow()
        ));

        URI location = ServletUriComponentsBuilder.fromContextPath(request)
                .path("/api/board/{id}")
                .buildAndExpand(tag.getBoard().getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("{id}/tags")
    public ResponseEntity<ResultResponse> listTag(@PathVariable Long id) {
        return ResponseEntity.ok(new ResultResponse(boardService.listTags(id)));
    }

    @PutMapping("/tags/{id}")
    public ResponseEntity<ResultResponse> editTag(@PathVariable Long id, @RequestBody TagRequest tagRequest) {
        Tag updatedTag = boardService.editTag(boardService.findTagById(id).orElseThrow(), tagRequest);
        return ResponseEntity.ok(new ResultResponse(new TagResponse(updatedTag)));
    }

    @DeleteMapping("/tags/{id}")
    public ResponseEntity<ResultResponse> deleteTag(@PathVariable Long id) {
        ResponseEntity<ResultResponse> result = null;
        try {
            boardService.deleteTag(id);
            result = ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            log.info("delete Tag FAIL");
            result = FAIL_RESPONSE;
        }

        return result;
    }
}
