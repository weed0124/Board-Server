package practice.springmvc.domain.board.repository.memory;

import org.springframework.stereotype.Repository;
import practice.springmvc.domain.board.Board;
import practice.springmvc.domain.board.repository.BoardRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class MemoryBoardRepository implements BoardRepository {

    private static final Map<Long, Board> store = new ConcurrentHashMap<>();
    private static long sequence = 0L;

    @Override
    public Board save(Board board) {
        board.setId(++sequence);
        store.put(board.getId(), board);
        return board;
    }

    @Override
    public Board findById(Long id) {
        return store.get(id);
    }

    @Override
    public List<Board> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void update(Long boardId, Board updateParam) {
        Board findBoard = findById(boardId);
        findBoard.setTitle(updateParam.getTitle());
        findBoard.setContent(updateParam.getContent());
        findBoard.setUpdateDate(new Date());
    }

    public void clearStore() {
        store.clear();
    }
}
