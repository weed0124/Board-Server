package practice.springmvc.domain.board;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import practice.springmvc.annotation.Trace;
import practice.springmvc.domain.PageCustom;
import practice.springmvc.domain.board.comment.Comment;
import practice.springmvc.domain.board.comment.CommentService;
import practice.springmvc.domain.board.tag.Tag;
import practice.springmvc.domain.board.tag.TagService;
import practice.springmvc.dto.BoardDTO;
import practice.springmvc.domain.board.notrecommend.NotRecommend;
import practice.springmvc.domain.board.notrecommend.NotRecommendService;
import practice.springmvc.domain.board.recommend.Recommend;
import practice.springmvc.domain.board.recommend.RecommendService;
import practice.springmvc.domain.board.repository.jpa.SpringDataJpaBoardRepository;
import practice.springmvc.dto.request.CommentRequest;
import practice.springmvc.dto.request.TagRequest;
import practice.springmvc.exception.BoardNotFoundException;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final SpringDataJpaBoardRepository boardRepository;
    private final RecommendService recommendService;
    private final NotRecommendService notRecommendService;
    private final CommentService commentService;
    private final TagService tagService;

    @Trace
    public Board save(Board board) {
        return boardRepository.save(board);
    }

    @Trace
    public Optional<Board> findById(Long id) {
        return boardRepository.findById(id);
    }

    @Trace
    public List<Board> findAll(BoardSearchCond cond) {
        return boardRepository.findBoardList(cond);
    }

    @Trace
    @Cacheable(value = "findPagingAll", key = "'findPagingAll' + #cond.getTitle()")
    public PageCustom<BoardDTO> findPagingAll(BoardSearchCond cond, Pageable pageable) {
        Page<BoardDTO> page = boardRepository.findPagingBoardList(cond, pageable);
        return new PageCustom<BoardDTO>(page.getContent(), page.getPageable(), page.getTotalElements());
    }

    @Trace
    public void update(Long boardId, Board updateParam) {
        Board findBoard = findById(boardId).orElseThrow();
        findBoard.setTitle(updateParam.getTitle());
        findBoard.setContent(updateParam.getContent());
        findBoard.setNickname(updateParam.getNickname());
        findBoard.setPassword(updateParam.getPassword());
        findBoard.setIp(updateParam.getIp());
    }

    @Trace
    public void delete(Long boardId) {
        boardRepository.deleteById(boardId);
    }


    // 작성자 IP와 같지 않은 경우 조회수 증가
    public Board addReadCount(Board board, HttpServletRequest request) {
        boolean ownIp = isOwnIp(board, request);
        if (!ownIp) {
            addReadCount(board.getId());
        }

        return board;
    }

    private void addReadCount(Long boardId) {
        Board findBoard = boardRepository.findById(boardId).orElseThrow();
        findBoard.setReadCount(findBoard.getReadCount() + 1);
    }

    // 작성자 IP와 같지 않은 경우 추천수 증가
    @Trace
    public Board recommend(Board board, HttpServletRequest request) {
        boolean ownIp = isOwnIp(board, request);
        Period p = Period.between(LocalDate.now(), LocalDate.now());
        if (!ownIp) {
            List<Recommend> recommends = Optional.ofNullable(board.getRecommends()).orElseGet(ArrayList::new);
            List<Recommend> oneDayRecommend = recommends.stream()
                    .filter(rec -> Period.between(rec.getCreatedDate().toLocalDate(), LocalDate.now()).getDays() == 0)
                    .toList();
            if (oneDayRecommend.isEmpty()) {
                Recommend recommend = new Recommend(board, getRemoteIp(request));
                recommendService.save(recommend);
                board.addRecommend(recommend);
            }
        }
        return board;
    }

    // 작성자 IP와 같지 않은 경우 비추천수 증가
    @Trace
    public Board notRecommend(Board board, HttpServletRequest request) {
        boolean ownIp = isOwnIp(board, request);
        if (!ownIp) {
            List<NotRecommend> notRecommends = Optional.ofNullable(board.getNotRecommends()).orElseGet(ArrayList::new);
            List<NotRecommend> oneDayNotRecommend = notRecommends.stream()
                    .filter(notRec -> Period.between(notRec.getCreatedDate().toLocalDate(), LocalDate.now()).getDays() == 0)
                    .toList();
            if (oneDayNotRecommend.isEmpty()) {
                NotRecommend notRecommend = new NotRecommend(board, getRemoteIp(request));
                notRecommendService.save(notRecommend);
                board.addNotRecommend(notRecommend);
            }
        }

        return board;
    }

    @Trace
    public Comment saveComment(Comment comment) {
        Optional<Board> board = boardRepository.findById(comment.getBoard().getId());
        if (board.isEmpty()) {
            throw new BoardNotFoundException();
        }

        Comment parent = comment.getParent();
        // 대댓글
        if (parent != null) {
            if (parent.getBoard().getId() != comment.getBoard().getId()) {
                throw new RuntimeException("댓글과 대댓글의 게시글 번호가 일치하지 않습니다.");
            }
            comment.updateParent(parent);
        }

        Comment savedComment = commentService.save(comment);

        return savedComment;
    }

    @Trace
    public List<Comment> listComment(Long boardId) {
        Optional<Board> board = boardRepository.findById(boardId);
        if (board.isEmpty()) {
            throw new BoardNotFoundException();
        }
        return commentService.getAllCommentsByBoard(boardId);
    }

    @Trace
    public Comment editComment(Comment comment, CommentRequest update, HttpServletRequest request) {
        String remoteIp = getRemoteIp(request);
        comment.setIp(remoteIp);
        return commentService.update(comment, update);
    }

    @Trace
    public void deleteComment(Long commentId, String password) {
        commentService.delete(commentId, password);
    }

    @Trace
    public Comment findByCommentId(Long commentId) {
        return commentService.findById(commentId);
    }

    @Trace
    public Tag saveTag(Tag tag) {
        Optional<Board> board = boardRepository.findById(tag.getBoard().getId());
        if (board.isEmpty()) {
            throw new BoardNotFoundException();
        }
        return tagService.save(tag);
    }

    public Optional<Tag> findTagById(Long id) {
        return tagService.findById(id);
    }

    @Trace
    public List<Tag> listTags(Long boardId) {
        Optional<Board> board = boardRepository.findById(boardId);
        if (board.isEmpty()) {
            throw new BoardNotFoundException();
        }

        return tagService.listTags(boardId);
    }

    @Trace
    public Tag editTag(Tag tag, TagRequest update) {
        return tagService.update(tag, update);
    }

    @Trace
    public void deleteTag(Long tagId) {
        tagService.delete(tagId);
    }

    private boolean isOwnIp(Board board, HttpServletRequest request) {
        String remoteIp = getRemoteIp(request);
        return board.getIp().equals(remoteIp);
    }

    // IP 조회
    public String getRemoteIp(HttpServletRequest request) {
        String ip = request.getHeader("X-FORWARDED-FOR");

        //proxy 환경일 경우
        ip = Optional.ofNullable(ip).orElseGet(() -> request.getHeader("Proxy-Client-IP"));
        //웹로직 서버일 경우
        ip = Optional.ofNullable(ip).orElseGet(() -> request.getHeader("WL-Proxy-Client-IP"));

        ip = Optional.ofNullable(ip).orElseGet(request::getRemoteAddr);

        return ip;
    }
}
