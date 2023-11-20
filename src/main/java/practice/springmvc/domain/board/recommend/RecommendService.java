package practice.springmvc.domain.board.recommend;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import practice.springmvc.domain.board.recommend.repository.RecommendRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendService {
    private final RecommendRepository recommendRepository;

    public Recommend save(Recommend recommend) {
        return recommendRepository.save(recommend);
    }

    public Recommend findById(Long id) {
        return recommendRepository.findById(id);
    }

    public List<Recommend> findByBoardId(Long boardId) {
        return recommendRepository.findByBoardId(boardId);
    }

    public List<Recommend> findByNickname(String nickname) {
        return recommendRepository.findByNickname(nickname);
    }

}
