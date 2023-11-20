package practice.springmvc.domain.board.repository;

import practice.springmvc.domain.board.Board;

import java.util.List;
import java.util.Optional;

public interface BoardRepository {
    Board save(Board board);

    Optional<Board> findById(Long id);

    List<Board> findAll();

    void update(Long boardId, Board updateParam);
}
