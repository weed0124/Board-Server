package practice.springmvc.domain.board.notrecommend.repository.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import practice.springmvc.annotation.Trace;
import practice.springmvc.domain.board.notrecommend.NotRecommend;
import practice.springmvc.domain.board.notrecommend.repository.NotRecommendRepository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
@RequiredArgsConstructor
public class JpaNotRecommendRepositoryV2 implements NotRecommendRepository {

    private final SpringDataJpaNotRecommendRepository repository;

    @Trace
    @Override
    public NotRecommend save(NotRecommend notRecommend) {
        return repository.save(notRecommend);
    }

    @Trace
    @Override
    public Optional<NotRecommend> findById(Long id) {
        return repository.findById(id);
    }

    @Trace
    @Override
    public List<NotRecommend> findByNickname(String nickname) {
        return null;
    }
}
