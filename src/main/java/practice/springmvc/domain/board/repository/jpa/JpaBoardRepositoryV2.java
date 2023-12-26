package practice.springmvc.domain.board.repository.jpa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import practice.springmvc.domain.board.Board;
import practice.springmvc.domain.board.BoardSearchCond;
import practice.springmvc.domain.board.repository.BoardRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
//@Repository
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
    public List<Board> findAll(BoardSearchCond cond) {
        String nickname = cond.getNickname();
        String title = cond.getTitle();
        log.info("v2 nickname={}", nickname);
        log.info("v2 title={}", title);

        if (StringUtils.hasText(nickname) && StringUtils.hasText(title)) {
            return repository.findBoards("%" + title + "%", "%" + nickname + "%");
        }
        else if (StringUtils.hasText(nickname)) {
            return repository.findByNicknameLike("%" + nickname + "%");
        }
        else if (StringUtils.hasText(title)) {
            return repository.findByTitleLike("%" + title + "%");
        } else {
            return repository.findAll();
        }
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
