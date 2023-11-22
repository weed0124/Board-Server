package practice.springmvc.domain.board.repository.memory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import practice.springmvc.domain.board.Board;
import practice.springmvc.domain.board.repository.BoardRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
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
    public Optional<Board> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Board> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void update(Long boardId, Board updateParam) {
        Board findBoard = findById(boardId).get();
        findBoard.setTitle(updateParam.getTitle());
        findBoard.setContent(updateParam.getContent());
        findBoard.setUpdateDate(LocalDateTime.now());
    }

    @Override
    public void addReadCount(Long boardId) {
        Board findBoard = findById(boardId).get();
        findBoard.setReadCount(findBoard.getReadCount() + 1);
    }

    public void clearStore() {
        store.clear();
    }
}