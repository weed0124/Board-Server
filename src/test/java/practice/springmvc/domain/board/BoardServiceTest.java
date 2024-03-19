package practice.springmvc.domain.board;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = BoardServiceTest.class)
@Transactional
class BoardServiceTest {
    @Autowired
    BoardService boardService;

    @Test
    public void addReadCount() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("127.127.0.1");

        Board board = new Board("title"
                , "content"
                , "익명", "test", "127.0.0.1");

        Board board2 = new Board("title"
                , "content"
                , "익명", "test", "127.127.0.1");

        Board saveBoard = boardService.save(board);
        Board saveBoard2 = boardService.save(board2);

        Board readBoard = boardService.addReadCount(saveBoard, request);
        Board readBoard2 = boardService.addReadCount(saveBoard2, request);

        assertThat(readBoard.getReadCount()).isEqualTo(1);
        assertThat(readBoard2.getReadCount()).isEqualTo(0);
    }

    @Test
    public void recommend() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("127.127.0.1");

        Board board = new Board("title"
                , "content"
                , "익명", "test", "127.0.0.1");

        Board board2 = new Board("title"
                , "content"
                , "익명", "test", "127.127.0.1");

        Board saveBoard = boardService.save(board);
        Board saveBoard2 = boardService.save(board2);

        Board recommended = boardService.recommend(saveBoard, request);
        Board recommended2 = boardService.recommend(saveBoard2, request);

        assertThat(recommended.recommendsSize()).isEqualTo(1);
        assertThat(recommended2.recommendsSize()).isEqualTo(0);
    }

    @Test
    public void notRecommend() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("127.127.0.1");

        Board board = new Board("title"
                , "content"
                , "익명", "test", "127.0.0.1");

        Board board2 = new Board("title"
                , "content"
                , "익명", "test", "127.127.0.1");

        Board saveBoard = boardService.save(board);
        Board saveBoard2 = boardService.save(board2);

        Board nottedRecommend = boardService.notRecommend(saveBoard, request);
        Board nottedRecommend2 = boardService.notRecommend(saveBoard2, request);

        assertThat(nottedRecommend.notRecommendsSize()).isEqualTo(1);
        assertThat(nottedRecommend2.notRecommendsSize()).isEqualTo(0);
    }
}