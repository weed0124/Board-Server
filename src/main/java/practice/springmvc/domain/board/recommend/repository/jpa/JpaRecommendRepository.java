package practice.springmvc.domain.board.recommend.repository.jpa;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import practice.springmvc.domain.board.recommend.Recommend;
import practice.springmvc.domain.board.recommend.repository.RecommendRepository;

import java.util.List;

@Slf4j
@Repository
@Transactional
public class JpaRecommendRepository implements RecommendRepository {

    private final EntityManager em;

    public JpaRecommendRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Recommend save(Recommend recommend) {
        em.persist(recommend);
        return recommend;
    }

    @Override
    public Recommend findById(Long id) {
        Recommend recommend = em.find(Recommend.class, id);
        return recommend;
    }

    @Override
    public List<Recommend> findByNickname(String nickname) {
        return null;
    }
}
