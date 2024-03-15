package practice.springmvc.domain.board.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practice.springmvc.domain.board.Board;
import practice.springmvc.domain.board.repository.jpa.SpringDataJpaBoardRepository;
import practice.springmvc.dto.request.CommentRequest;
import practice.springmvc.exception.BoardNotFoundException;
import practice.springmvc.utils.SHA256Util;

import java.util.List;
import java.util.Optional;

import static practice.springmvc.utils.SHA256Util.*;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class CommentService {
    private final SpringDataJpaBoardRepository boardRepository;
    private final CommentRepository commentRepository;

    public Comment save(Comment comment) {
        comment.setPassword(encryptSHA256(comment.getPassword()));
        return commentRepository.save(comment);
    }

    public Comment update(Comment comment, CommentRequest update) {
        Optional<Board> board = boardRepository.findById(comment.getBoard().getId());
        if (board.isEmpty()) {
            throw new BoardNotFoundException();
        }
        comment.setNickname(update.getNickname());
        comment.setPassword(update.getPassword());
        comment.setContent(update.getContent());
        comment.setBoard(board.get());

        return comment;
    }

    public void delete(Long commentId, String password) {
        String encryptPwd = encryptSHA256(password);
        Comment comment = commentRepository.findByIdAndPassword(commentId, encryptPwd);

        if (comment != null) {
            commentRepository.deleteById(commentId);
        } else {
            log.error("deleteId Error {}", comment);
            throw new RuntimeException("deleteId Error id 삭제 메서드 확인이 필요합니다. info : " + comment);
        }
    }

    public List<Comment> getAllCommentsByBoard(Long boardId) {
        return commentRepository.getAllCommentsByBoard(boardId);
    }

    public Comment findById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow();
    }
}
