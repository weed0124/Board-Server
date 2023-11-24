package practice.springmvc.domain.board.repository;

import practice.springmvc.domain.board.Board;
import practice.springmvc.domain.board.BoardSearchCond;

import java.util.List;
import java.util.Optional;

public interface BoardRepository {
    Board save(Board board);

    Optional<Board> findById(Long id);

    List<Board> findAll();
    List<Board> findAll(BoardSearchCond cond);

    void update(Long boardId, Board updateParam);

    void addReadCount(Long boardId);
}
