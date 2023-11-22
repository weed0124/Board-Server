package practice.springmvc.domain.board.repository.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import practice.springmvc.domain.board.Board;
import practice.springmvc.domain.board.repository.BoardRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
@RequiredArgsConstructor
public class JpaBoardRepositoryV2 implements BoardRepository {

    private final SpringDataJpaBoardRepository repository;

    @Override
    public Board save(Board board) {
        return repository.save(board);
    }

    @Override
    public Optional<Board> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Board> findAll() {
        return repository.findAll();
    }

    @Override
    public void update(Long boardId, Board updateParam) {
        Board findBoard = repository.findById(boardId).orElseThrow();
        findBoard.setTitle(updateParam.getTitle());
        findBoard.setContent(updateParam.getContent());
        findBoard.setUpdateDate(LocalDateTime.now());
    }

    @Override
    public void addReadCount(Long boardId) {
        Board findBoard = repository.findById(boardId).orElseThrow();
        findBoard.setReadCount(findBoard.getReadCount() + 1);
    }
}
