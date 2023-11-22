package practice.springmvc.domain.board.notrecommend.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import practice.springmvc.domain.board.notrecommend.NotRecommend;

public interface SpringDataJpaNotRecommendRepository extends JpaRepository<NotRecommend, Long> {
}
