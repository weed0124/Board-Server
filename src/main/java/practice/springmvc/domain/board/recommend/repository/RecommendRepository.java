package practice.springmvc.domain.board.recommend.repository;

import practice.springmvc.domain.board.recommend.Recommend;

import java.util.List;

public interface RecommendRepository {
    Recommend save(Recommend recommend);

    Recommend findById(Long id);

//    List<Recommend> findByBoardId(Long boardId);

    List<Recommend> findByNickname(String nickname);
}
