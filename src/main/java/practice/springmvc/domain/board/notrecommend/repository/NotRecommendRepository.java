package practice.springmvc.domain.board.notrecommend.repository;

import practice.springmvc.domain.board.notrecommend.NotRecommend;

import java.util.List;

public interface NotRecommendRepository {
    NotRecommend save(NotRecommend notRecommend);

    NotRecommend findById(Long id);

    List<NotRecommend> findByBoardId(Long boardId);

    List<NotRecommend> findByNickname(String nickname);
}
