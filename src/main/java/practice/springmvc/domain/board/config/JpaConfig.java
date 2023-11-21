package practice.springmvc.domain.board.config;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import practice.springmvc.domain.board.BoardService;
import practice.springmvc.domain.board.notrecommend.NotRecommendService;
import practice.springmvc.domain.board.notrecommend.repository.jpa.JpaNotRecommendRepository;
import practice.springmvc.domain.board.notrecommend.repository.NotRecommendRepository;
import practice.springmvc.domain.board.recommend.RecommendService;
import practice.springmvc.domain.board.recommend.repository.RecommendRepository;
import practice.springmvc.domain.board.recommend.repository.jpa.JpaRecommendRepository;
import practice.springmvc.domain.board.repository.BoardRepository;
import practice.springmvc.domain.board.repository.jpa.JpaBoardRepository;
import practice.springmvc.domain.member.MemberService;
import practice.springmvc.domain.member.repository.JpaMemberRepository;
import practice.springmvc.domain.member.repository.MemberRepository;

@Configuration
@RequiredArgsConstructor
public class JpaConfig {

    private final EntityManager em;

    @Bean
    public BoardService boardService() {
        return new BoardService(boardRepository(), recommendService(), notRecommendService(), memberService());
    }

    @Bean
    public BoardRepository boardRepository() {
        return new JpaBoardRepository(em);
    }

    @Bean
    public RecommendService recommendService() {
        return new RecommendService(recommendRepository());
    }

    @Bean
    public RecommendRepository recommendRepository() {
        return new JpaRecommendRepository(em);
    }

    @Bean
    public NotRecommendService notRecommendService() {
        return new NotRecommendService(notRecommendRepository());
    }

    @Bean
    public NotRecommendRepository notRecommendRepository() {
        return new JpaNotRecommendRepository(em);
    }

    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
        return new JpaMemberRepository(em);
    }
}
