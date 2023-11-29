package practice.springmvc.domain.board.notrecommend.repository.jpa;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import practice.springmvc.domain.board.notrecommend.NotRecommend;
import practice.springmvc.domain.board.notrecommend.repository.NotRecommendRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
//@Repository
@Transactional
public class JpaNotRecommendRepositoryV1 implements NotRecommendRepository {

    private final EntityManager em;

    public JpaNotRecommendRepositoryV1(EntityManager em) {
        this.em = em;
    }

    @Override
    public NotRecommend save(NotRecommend notRecommend) {
        em.persist(notRecommend);
        return notRecommend;
    }

    @Override
    public Optional<NotRecommend> findById(Long id) {
        return Optional.ofNullable(em.find(NotRecommend.class, id));
    }

//    @Override
//    public List<NotRecommend> findByBoardId(Long boardId) {
//        return null;
//    }

    @Override
    public List<NotRecommend> findByNickname(String nickname) {
        return null;
    }
}
