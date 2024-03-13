package practice.springmvc.domain.board.notrecommend.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import practice.springmvc.domain.board.Board;
import practice.springmvc.domain.board.notrecommend.NotRecommend;

import java.util.List;

public interface SpringDataJpaNotRecommendRepository extends JpaRepository<NotRecommend, Long> {
    List<NotRecommend> findNotRecommendsByBoard(Board board);
}
