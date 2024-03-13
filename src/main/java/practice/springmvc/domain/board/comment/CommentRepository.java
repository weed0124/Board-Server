package practice.springmvc.domain.board.comment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentJPARepository {
    Comment findByIdAndPassword(Long id, String password);
}
