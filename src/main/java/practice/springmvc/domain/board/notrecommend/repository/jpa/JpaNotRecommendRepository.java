package practice.springmvc.domain.board.notrecommend.repository.jpa;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import practice.springmvc.domain.board.notrecommend.NotRecommend;
import practice.springmvc.domain.board.notrecommend.repository.NotRecommendRepository;

import java.util.List;

@Slf4j
@Repository
@Transactional
public class JpaNotRecommendRepository implements NotRecommendRepository {

    private final EntityManager em;

    public JpaNotRecommendRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public NotRecommend save(NotRecommend notRecommend) {
        em.persist(notRecommend);
        return notRecommend;
    }

    @Override
    public NotRecommend findById(Long id) {
        NotRecommend notRecommend = em.find(NotRecommend.class, id);
        return notRecommend;
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
