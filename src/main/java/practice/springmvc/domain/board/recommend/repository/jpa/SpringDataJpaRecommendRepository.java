package practice.springmvc.domain.board.recommend.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import practice.springmvc.domain.board.Board;
import practice.springmvc.domain.board.recommend.Recommend;

import java.util.List;

public interface SpringDataJpaRecommendRepository extends JpaRepository<Recommend, Long> {
    List<Recommend> findRecommendsByBoard(Board board);
}
