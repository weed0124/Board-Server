package practice.springmvc.domain.board;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import practice.springmvc.domain.board.notrecommend.NotRecommendService;
import practice.springmvc.domain.board.notrecommend.repository.NotRecommendRepository;
import practice.springmvc.domain.board.notrecommend.repository.memory.MemoryNotRecommendRepository;
import practice.springmvc.domain.board.recommend.RecommendService;
import practice.springmvc.domain.board.recommend.repository.RecommendRepository;
import practice.springmvc.domain.board.recommend.repository.memory.MemoryRecommendRepository;
import practice.springmvc.domain.board.repository.BoardRepository;
import practice.springmvc.domain.board.repository.memory.MemoryBoardRepository;

@Configuration
public class MemoryConfig {

    @Bean
    public BoardService boardService() {
        return new BoardService(boardRepository(), recommendService(), notRecommendService());
    }

    @Bean
    public BoardRepository boardRepository() {
        return new MemoryBoardRepository();
    }

    @Bean
    public RecommendService recommendService() {
        return new RecommendService(recommendRepository());
    }

    @Bean
    public RecommendRepository recommendRepository() {
        return new MemoryRecommendRepository();
    }

    @Bean
    public NotRecommendService notRecommendService() {
        return new NotRecommendService(notRecommendRepository());
    }

    @Bean
    public NotRecommendRepository notRecommendRepository() {
        return new MemoryNotRecommendRepository();
    }
}
