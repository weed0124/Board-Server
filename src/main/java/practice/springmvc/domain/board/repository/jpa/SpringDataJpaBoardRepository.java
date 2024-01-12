package practice.springmvc.domain.board.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import practice.springmvc.domain.board.Board;

import java.util.List;

public interface SpringDataJpaBoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByTitleLike(String title);

    @Query("select b from Board b join b.member m where m.nickname like :nickname")
    List<Board> findByNicknameLike(@Param("nickname") String nickname);

    @Query("select b from Board b join b.member m where b.title like :title and m.nickname like :nickname")
    List<Board> findBoards(@Param("title") String title, @Param("nickname") String nickname);
}
