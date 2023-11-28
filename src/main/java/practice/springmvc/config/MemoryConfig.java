package practice.springmvc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import practice.springmvc.domain.board.BoardService;
import practice.springmvc.domain.board.notrecommend.NotRecommendService;
import practice.springmvc.domain.board.notrecommend.repository.NotRecommendRepository;
import practice.springmvc.domain.board.notrecommend.repository.memory.MemoryNotRecommendRepository;
import practice.springmvc.domain.board.recommend.RecommendService;
import practice.springmvc.domain.board.recommend.repository.RecommendRepository;
import practice.springmvc.domain.board.recommend.repository.memory.MemoryRecommendRepository;
import practice.springmvc.domain.board.repository.BoardRepository;
import practice.springmvc.domain.board.repository.memory.MemoryBoardRepository;
import practice.springmvc.domain.member.MemberService;
import practice.springmvc.domain.member.repository.MemberRepository;
import practice.springmvc.domain.member.repository.memory.MemoryMemberRepository;

@Configuration
public class MemoryConfig {

    @Bean
    public BoardService boardService() {
        return new BoardService(boardRepository(), recommendService(), notRecommendService(), memberService());
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

    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }
}
