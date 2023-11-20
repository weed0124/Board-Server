package practice.springmvc.domain.board.repository;

import practice.springmvc.domain.board.Board;

import java.util.List;

public interface BoardRepository {
    Board save(Board board);

    Board findById(Long id);

    List<Board> findAll();

    void update(Long boardId, Board updateParam);
}
