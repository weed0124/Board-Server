package practice.springmvc.domain.board.notrecommend.repository.memory;

import org.springframework.stereotype.Repository;
import practice.springmvc.domain.board.notrecommend.NotRecommend;
import practice.springmvc.domain.board.notrecommend.repository.NotRecommendRepository;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class MemoryNotRecommendRepository implements NotRecommendRepository {
    private static final Map<Long, NotRecommend> store = new ConcurrentHashMap<>();
    private static long sequence = 0L;

    @Override
    public NotRecommend save(NotRecommend rec) {
        rec.setId(++sequence);
        store.put(rec.getId(), rec);
        return rec;
    }

    @Override
    public Optional<NotRecommend> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

//    @Override
//    public List<NotRecommend> findByBoardId(Long boardId) {
//        Period p = Period.between(LocalDate.now(), LocalDate.now());
//        return new ArrayList<>(store.values()).stream()
//                .filter(nrec -> nrec.getBoardId() == boardId
//                        && Period.between(nrec.getRegistDate().toLocalDate(), LocalDate.now()).getDays() == 0).toList();
//    }

    @Override
    public List<NotRecommend> findByNickname(String nickname) {
        return new ArrayList<>(store.values()).stream()
                .filter(nrec -> nrec.getMember().getNickname().equals(nickname)
                        && Period.between(nrec.getRegistDate().toLocalDate(), LocalDate.now()).getDays() == 0).toList();
    }
}
