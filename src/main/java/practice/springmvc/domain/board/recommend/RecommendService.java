package practice.springmvc.domain.board.recommend;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import practice.springmvc.annotation.Trace;
import practice.springmvc.domain.board.Board;
import practice.springmvc.domain.board.recommend.repository.jpa.SpringDataJpaRecommendRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendService {
    private final SpringDataJpaRecommendRepository recommendRepository;

    @Trace
    public Recommend save(Recommend recommend) {
        return recommendRepository.save(recommend);
    }

    @Trace
    public Optional<Recommend> findById(Long id) {
        return recommendRepository.findById(id);
    }

    public List<Recommend> findByBoard(Board board) {
        return recommendRepository.findRecommendsByBoard(board);
    }

}
