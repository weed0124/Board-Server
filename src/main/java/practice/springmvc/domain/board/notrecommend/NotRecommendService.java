package practice.springmvc.domain.board.notrecommend;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import practice.springmvc.domain.board.notrecommend.repository.NotRecommendRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotRecommendService {
    private final NotRecommendRepository notRecommendRepository;

    public NotRecommend save(NotRecommend notRecommend) {
        return notRecommendRepository.save(notRecommend);
    }

    public Optional<NotRecommend> findById(Long id) {
        return notRecommendRepository.findById(id);
    }

//    public List<NotRecommend> findByBoardId(Long boardId) {
//        return notRecommendRepository.findByBoardId(boardId);
//    }

    public List<NotRecommend> findByNickname(String nickname) {
        return notRecommendRepository.findByNickname(nickname);
    }
}
