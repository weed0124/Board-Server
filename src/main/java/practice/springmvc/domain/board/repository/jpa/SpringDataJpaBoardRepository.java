package practice.springmvc.domain.board.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import practice.springmvc.domain.board.Board;

public interface SpringDataJpaBoardRepository extends JpaRepository<Board, Long> {

}
