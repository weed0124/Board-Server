package practice.springmvc.domain.board.repository.jpa;

import practice.springmvc.domain.board.Board;
import practice.springmvc.domain.board.BoardSearchCond;

import java.util.List;

public interface BoardJPARepository {
    List<Board> findBoardList(BoardSearchCond cond);
}
