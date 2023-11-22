package practice.springmvc.domain.board.recommend.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import practice.springmvc.domain.board.recommend.Recommend;

public interface SpringDataJpaRecommendRepository extends JpaRepository<Recommend, Long> {
}
