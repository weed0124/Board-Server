package practice.springmvc.domain.board;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import practice.springmvc.domain.member.Member;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class BoardRepositoryTest {

    BoardRepository boardRepository = new BoardRepository();

    @AfterEach
    void afterEach() {
        boardRepository.clearStore();
    }

    @Test
    public void save() throws Exception {
        // given
        Board board = new Board("board", "content", new Member("익명", "test"));

        // when
        Board saveBoard = boardRepository.save(board);

        // then
        Board findBoard = boardRepository.findById(saveBoard.getId());
        assertThat(findBoard).isEqualTo(saveBoard);

    }

    @Test
    public void findAll() throws Exception {
        // given
        Board board1 = new Board("board1", "content1", new Member("익명1", "test"));
        Board board2 = new Board("board2", "content2", new Member("익명2", "test2"));

        boardRepository.save(board1);
        boardRepository.save(board2);

        // when
        List<Board> boardList = boardRepository.findAll();

        // then
        assertThat(boardList.size()).isEqualTo(2);
        assertThat(boardList).contains(board1, board2);
    }

    @Test
    public void update() throws Exception {
        // given
        Board board = new Board("board", "content", new Member("익명", "test"));

        Board saveBoard = boardRepository.save(board);
        Long id = saveBoard.getId();

        // when
        Board updateParam = new Board("board_edit", "content_edit", new Member("익명", "test"));
        boardRepository.update(id, updateParam);

        Board findBoard = boardRepository.findById(id);

        // then
        assertThat(findBoard.getTitle()).isEqualTo(updateParam.getTitle());
        assertThat(findBoard.getContent()).isEqualTo(updateParam.getContent());
    }
}