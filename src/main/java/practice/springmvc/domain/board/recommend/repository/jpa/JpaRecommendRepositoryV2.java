package practice.springmvc.domain.board.recommend.repository.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import practice.springmvc.domain.board.recommend.Recommend;
import practice.springmvc.domain.board.recommend.repository.RecommendRepository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
@RequiredArgsConstructor
public class JpaRecommendRepositoryV2 implements RecommendRepository {

    private final SpringDataJpaRecommendRepository repository;

    @Override
    public Recommend save(Recommend recommend) {
        return repository.save(recommend);
    }

    @Override
    public Optional<Recommend> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Recommend> findByNickname(String nickname) {
        return null;
    }
}
