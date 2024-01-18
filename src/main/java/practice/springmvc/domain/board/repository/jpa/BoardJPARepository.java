package practice.springmvc.domain.board.repository.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import practice.springmvc.domain.board.Board;
import practice.springmvc.domain.board.BoardSearchCond;
import practice.springmvc.domain.board.dto.BoardDTO;

import java.util.List;

public interface BoardJPARepository {
    List<Board> findBoardList(BoardSearchCond cond);

    Page<BoardDTO> findPagingBoardList(BoardSearchCond cond, Pageable pageable);

    Board findBoardById(Long id);
}
