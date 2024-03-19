package practice.springmvc.domain.board;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import practice.springmvc.domain.board.repository.BoardRepository;
import practice.springmvc.domain.board.repository.jpa.SpringDataJpaBoardRepository;
import practice.springmvc.domain.board.repository.memory.MemoryBoardRepository;
import practice.springmvc.domain.member.Member;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class BoardRepositoryTest {

    @Autowired
    SpringDataJpaBoardRepository boardRepository;

    @Test
    public void save() throws Exception {
        // given
        Board board = new Board("board", "content", "익명", "test", "127.0.0.1");

        // when
        Board saveBoard = boardRepository.save(board);

        // then
        Board findBoard = boardRepository.findById(saveBoard.getId()).get();
        assertThat(findBoard).isEqualTo(saveBoard);

    }

    @Test
    public void findBoards() throws Exception {
        // given
        Board board1 = new Board("board1", "content1", "JUNIT4", "test", "127.0.0.1");
        Board board2 = new Board("board2", "content2", "JUNIT5", "test2", "127.0.0.1");

        boardRepository.save(board1);
        boardRepository.save(board2);

//        test(null, null, board1, board2);
//        test(null, "", board1, board2);
//        test("", null, board1, board2);

        test("board1", null, board1);
        test("board2", null, board2);

        test(null, "JUNIT", board1, board2);

        test("board1", "JUNIT4", board1);
    }

    void test(String title, String nickname, Board... boards) {
        List<Board> result = boardRepository.findBoardList(new BoardSearchCond(title, nickname));
        assertThat(result).containsExactly(boards);
    }

    @Test
    public void update() throws Exception {
        // given
        Board board = new Board("board", "content", "익명", "test", "127.0.0.1");

        Board saveBoard = boardRepository.save(board);
        Long id = saveBoard.getId();

        // when
        Board updateParam = new Board("board_edit", "content_edit", "익명", "test", "127.0.0.1");
        Board findBoard = boardRepository.findById(id).orElseThrow();
        findBoard.setTitle(updateParam.getTitle());
        findBoard.setContent(updateParam.getContent());

        // then
        assertThat(findBoard.getTitle()).isEqualTo(updateParam.getTitle());
        assertThat(findBoard.getContent()).isEqualTo(updateParam.getContent());
    }
}