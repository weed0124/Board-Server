package practice.springmvc.domain.board.notrecommend.repository;

import practice.springmvc.domain.board.notrecommend.NotRecommend;

import java.util.List;
import java.util.Optional;

public interface NotRecommendRepository {
    NotRecommend save(NotRecommend notRecommend);

    Optional<NotRecommend> findById(Long id);

//    List<NotRecommend> findByBoardId(Long boardId);

    List<NotRecommend> findByNickname(String nickname);
}
