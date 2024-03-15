package practice.springmvc.domain.board.tag.repository;

import practice.springmvc.domain.board.tag.Tag;

import java.util.List;

public interface TagJPARepository {
    List<Tag> getAllTagsByBoard(Long boardId);
}
