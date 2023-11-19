package practice.springmvc.domain.board.recommend;

import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class RecommendRepository {
    private static final Map<Long, Recommend> store = new ConcurrentHashMap<>();
    private static long sequence = 0L;

    public Recommend save(Recommend rec) {
        rec.setId(++sequence);
        store.put(rec.getId(), rec);
        return rec;
    }

    public Recommend findById(Long id) {
        return store.get(id);
    }

    public List<Recommend> findByBoardId(Long boardId) {
        Period p = Period.between(LocalDate.now(), LocalDate.now());
        return new ArrayList<>(store.values()).stream()
                .filter(recommend -> recommend.getBoardId() == boardId
                        && Period.between(recommend.getRegistDate(), LocalDate.now()).getDays() == 0).toList();
    }

    public List<Recommend> findByNickname(String nickname) {
        return new ArrayList<>(store.values()).stream()
                .filter(recommend -> recommend.getMember().getNickname().equals(nickname)
                        && Period.between(recommend.getRegistDate(), LocalDate.now()).getDays() == 0).toList();
    }
}
