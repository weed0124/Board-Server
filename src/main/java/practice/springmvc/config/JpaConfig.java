package practice.springmvc.config;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import practice.springmvc.domain.board.BoardService;
import practice.springmvc.domain.board.notrecommend.NotRecommendService;
import practice.springmvc.domain.board.notrecommend.repository.jpa.JpaNotRecommendRepositoryV1;
import practice.springmvc.domain.board.notrecommend.repository.NotRecommendRepository;
import practice.springmvc.domain.board.recommend.RecommendService;
import practice.springmvc.domain.board.recommend.repository.RecommendRepository;
import practice.springmvc.domain.board.recommend.repository.jpa.JpaRecommendRepositoryV1;
import practice.springmvc.domain.board.repository.BoardRepository;
import practice.springmvc.domain.board.repository.jpa.JpaBoardRepositoryV1;
import practice.springmvc.domain.member.MemberService;
import practice.springmvc.domain.member.repository.jpa.JpaMemberRepositoryV1;
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
        return new JpaBoardRepositoryV1(em);
    }

    @Bean
    public RecommendService recommendService() {
        return new RecommendService(recommendRepository());
    }

    @Bean
    public RecommendRepository recommendRepository() {
        return new JpaRecommendRepositoryV1(em);
    }

    @Bean
    public NotRecommendService notRecommendService() {
        return new NotRecommendService(notRecommendRepository());
    }

    @Bean
    public NotRecommendRepository notRecommendRepository() {
        return new JpaNotRecommendRepositoryV1(em);
    }

    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
        return new JpaMemberRepositoryV1(em);
    }
}
