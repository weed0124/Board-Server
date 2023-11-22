package practice.springmvc.domain.board.recommend.repository.jpa;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import practice.springmvc.domain.board.recommend.Recommend;
import practice.springmvc.domain.board.recommend.repository.RecommendRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@Transactional
public class JpaRecommendRepositoryV1 implements RecommendRepository {

    private final EntityManager em;

    public JpaRecommendRepositoryV1(EntityManager em) {
        this.em = em;
    }

    @Override
    public Recommend save(Recommend recommend) {
        em.persist(recommend);
        return recommend;
    }

    @Override
    public Optional<Recommend> findById(Long id) {
        return Optional.ofNullable(em.find(Recommend.class, id));
    }

    @Override
    public List<Recommend> findByNickname(String nickname) {
        return null;
    }
}
