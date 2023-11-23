package practice.springmvc.domain.board.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import practice.springmvc.domain.board.Board;

import java.util.List;

public interface SpringDataJpaBoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByTitleLike(String title);

    @Query(value = "select b.* from Board b, Member m where b.member_id = m.id and m.nickname like :nickname", nativeQuery = true)
    List<Board> findByNicknameLike(@Param("nickname") String nickname);

    @Query(value = "select b.* from Board b, Member m where b.member_id = m.id and b.title like :title and m.nickname like :nickname", nativeQuery = true)
    List<Board> findBoards(@Param("title") String title, @Param("nickname") String nickname);
}
