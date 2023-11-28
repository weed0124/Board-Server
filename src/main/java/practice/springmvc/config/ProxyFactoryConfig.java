package practice.springmvc.config;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import practice.springmvc.config.advice.LogAdvice;
import practice.springmvc.domain.board.BoardService;
import practice.springmvc.domain.board.notrecommend.NotRecommendService;
import practice.springmvc.domain.board.notrecommend.repository.NotRecommendRepository;
import practice.springmvc.domain.board.notrecommend.repository.jpa.JpaNotRecommendRepositoryV1;
import practice.springmvc.domain.board.recommend.RecommendService;
import practice.springmvc.domain.board.recommend.repository.RecommendRepository;
import practice.springmvc.domain.board.recommend.repository.jpa.JpaRecommendRepositoryV2;
import practice.springmvc.domain.board.recommend.repository.jpa.SpringDataJpaRecommendRepository;
import practice.springmvc.domain.board.repository.BoardRepository;
import practice.springmvc.domain.board.repository.jpa.JpaBoardRepositoryV3;
import practice.springmvc.domain.board.repository.jpa.SpringDataJpaBoardRepository;
import practice.springmvc.domain.member.MemberService;
import practice.springmvc.domain.member.repository.MemberRepository;
import practice.springmvc.domain.member.repository.jpa.JpaMemberRepositoryV2;
import practice.springmvc.domain.member.repository.jpa.SpringDataJpaMemberRepository;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ProxyFactoryConfig {

    private final SpringDataJpaBoardRepository springDataJpaBoardRepository;
    private final SpringDataJpaMemberRepository springDataJpaMemberRepository;
    private final SpringDataJpaRecommendRepository springDataJpaRecommendRepository;
    private final EntityManager em;
    private final LogAdvice logAdvice;

    @Bean
    public BoardService boardService() {
        return new BoardService(boardRepository(),
                recommendService(),
                notRecommendService(),
                memberService());
    }

    @Bean
    public BoardRepository boardRepository() {
        BoardRepository boardRepository = new JpaBoardRepositoryV3(springDataJpaBoardRepository, em);
        ProxyFactory proxyFactory = new ProxyFactory(boardRepository);
        proxyFactory.addAdvice(logAdvice);
        BoardRepository proxy = (BoardRepository) proxyFactory.getProxy();

        return proxy;
    }

    @Bean
    public RecommendService recommendService() {
        return new RecommendService(recommendRepository());
    }

    @Bean
    public RecommendRepository recommendRepository() {
        RecommendRepository recommendRepository = new JpaRecommendRepositoryV2(springDataJpaRecommendRepository);
        ProxyFactory proxyFactory = new ProxyFactory(recommendRepository);
        proxyFactory.addAdvice(logAdvice);
        RecommendRepository proxy = (RecommendRepository) proxyFactory.getProxy();

        return proxy;
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
        MemberRepository memberRepository = new JpaMemberRepositoryV2(springDataJpaMemberRepository);
        ProxyFactory proxyFactory = new ProxyFactory(memberRepository);
        proxyFactory.addAdvice(logAdvice);
        MemberRepository proxy = (MemberRepository) proxyFactory.getProxy();

        return proxy;
    }
}
