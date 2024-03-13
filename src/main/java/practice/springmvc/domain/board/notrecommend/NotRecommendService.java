package practice.springmvc.domain.board.notrecommend;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import practice.springmvc.annotation.Trace;
import practice.springmvc.domain.board.Board;
import practice.springmvc.domain.board.notrecommend.repository.jpa.SpringDataJpaNotRecommendRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotRecommendService {
    private final SpringDataJpaNotRecommendRepository notRecommendRepository;

    @Trace
    public NotRecommend save(NotRecommend notRecommend) {
        return notRecommendRepository.save(notRecommend);
    }

    @Trace
    public Optional<NotRecommend> findById(Long id) {
        return notRecommendRepository.findById(id);
    }

    public List<NotRecommend> findByBoard(Board board) {
        return notRecommendRepository.findNotRecommendsByBoard(board);
    }
}
