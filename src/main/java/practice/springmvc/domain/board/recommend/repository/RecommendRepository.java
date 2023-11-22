package practice.springmvc.domain.board.recommend.repository;

import practice.springmvc.domain.board.recommend.Recommend;

import java.util.List;
import java.util.Optional;

public interface RecommendRepository {
    Recommend save(Recommend recommend);

    Optional<Recommend> findById(Long id);

//    List<Recommend> findByBoardId(Long boardId);

    List<Recommend> findByNickname(String nickname);
}
