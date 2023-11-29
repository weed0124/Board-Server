package practice.springmvc.domain.board.recommend.repository.memory;

import org.springframework.stereotype.Repository;
import practice.springmvc.domain.board.recommend.Recommend;
import practice.springmvc.domain.board.recommend.repository.RecommendRepository;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

//@Repository
public class MemoryRecommendRepository implements RecommendRepository {
    private static final Map<Long, Recommend> store = new ConcurrentHashMap<>();
    private static long sequence = 0L;

    @Override
    public Recommend save(Recommend rec) {
        rec.setId(++sequence);
        store.put(rec.getId(), rec);
        return rec;
    }

    @Override
    public Optional<Recommend> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

//    @Override
//    public List<Recommend> findByBoardId(Long boardId) {
//        Period p = Period.between(LocalDate.now(), LocalDate.now());
//        return new ArrayList<>(store.values()).stream()
//                .filter(recommend -> recommend.getBoardId() == boardId
//                        && Period.between(recommend.getRegistDate().toLocalDate(), LocalDate.now()).getDays() == 0).toList();
//    }

    @Override
    public List<Recommend> findByNickname(String nickname) {
        return new ArrayList<>(store.values()).stream()
                .filter(recommend -> recommend.getMember().getNickname().equals(nickname)
                        && Period.between(recommend.getRegistDate().toLocalDate(), LocalDate.now()).getDays() == 0).toList();
    }
}
