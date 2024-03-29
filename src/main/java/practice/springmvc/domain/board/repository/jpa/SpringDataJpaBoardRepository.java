package practice.springmvc.domain.board.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import practice.springmvc.domain.board.Board;

import java.util.List;

public interface SpringDataJpaBoardRepository extends JpaRepository<Board, Long>, BoardJPARepository {
    List<Board> findByTitleLike(String title);

    @Query("select b from Board b where b.nickname like :nickname")
    List<Board> findByNicknameLike(@Param("nickname") String nickname);

    @Query("select b from Board b where b.title like :title and b.nickname like :nickname")
    List<Board> findBoards(@Param("title") String title, @Param("nickname") String nickname);
}
