package practice.springmvc.domain.board.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practice.springmvc.domain.board.Board;
import practice.springmvc.domain.board.repository.jpa.SpringDataJpaBoardRepository;
import practice.springmvc.utils.SHA256Util;

import java.util.List;

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

    public void update(Long commentId, Comment updateParam) {
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        Board board = boardRepository.findById(comment.getBoard().getId()).orElseThrow();
        comment.setNickname(updateParam.getNickname());
        comment.setPassword(updateParam.getPassword());
        comment.setIp(updateParam.getIp());
        comment.setContent(updateParam.getContent());
        comment.setBoard(board);
    }

    public void delete(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    public List<Comment> getAllCommentsByBoard(Long boardId) {
        return commentRepository.getAllCommentsByBoard(boardId);
    }

    public Comment findById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow();
    }
}
