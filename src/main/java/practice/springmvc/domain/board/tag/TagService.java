package practice.springmvc.domain.board.tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import practice.springmvc.domain.board.Board;
import practice.springmvc.domain.board.repository.BoardRepository;
import practice.springmvc.domain.board.repository.jpa.SpringDataJpaBoardRepository;
import practice.springmvc.domain.board.tag.repository.TagRepository;
import practice.springmvc.dto.request.TagRequest;
import practice.springmvc.exception.BoardNotFoundException;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TagService {
    private final SpringDataJpaBoardRepository boardRepository;
    private final TagRepository tagRepository;

    public Tag save(Tag tag) {
        return tagRepository.save(tag);
    }

    public Optional<Tag> findById(Long id) {
        return tagRepository.findById(id);
    }

    public List<Tag> listTags(Long boardId) {
        return tagRepository.getAllTagsByBoard(boardId);
    }

    public Tag update(Tag tag, TagRequest update) {
        Optional<Board> board = boardRepository.findById(tag.getBoard().getId());
        if (board.isEmpty()) {
            throw new BoardNotFoundException();
        }
        tag.setName(update.getName());
        tag.setUrl(update.getUrl());
        tag.setBoard(board.get());

        return tag;
    }

    public void delete(Long tagId) {
        tagRepository.deleteById(tagId);
    }
}
