package practice.springmvc.domain.board.tag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import practice.springmvc.domain.board.comment.Comment;
import practice.springmvc.domain.board.tag.Tag;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long>, TagJPARepository {

}
