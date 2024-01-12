package practice.springmvc.config;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import practice.springmvc.domain.board.BoardService;
import practice.springmvc.config.dynamicproxy.handler.LogTraceFilterHandler;
import practice.springmvc.domain.board.notrecommend.NotRecommendService;
import practice.springmvc.domain.board.notrecommend.repository.NotRecommendRepository;
import practice.springmvc.domain.board.notrecommend.repository.jpa.JpaNotRecommendRepositoryV1;
import practice.springmvc.domain.board.notrecommend.repository.jpa.JpaNotRecommendRepositoryV2;
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
import practice.springmvc.trace.logtrace.LogTrace;

import java.lang.reflect.Proxy;

@Slf4j
//@Configuration
@RequiredArgsConstructor
public class DynamicProxyConfig {

    private final SpringDataJpaBoardRepository springDataJpaBoardRepository;
    private final SpringDataJpaMemberRepository springDataJpaMemberRepository;
    private final SpringDataJpaRecommendRepository springDataJpaRecommendRepository;
    private final EntityManager em;
    private final static String[] PATTERNS = {"save*", "find*", "update", "add*"};

//    @Bean
//    public BoardService boardService(LogTrace logTrace) {
//        return new BoardService(boardRepository(logTrace),
//                recommendService(logTrace),
//                notRecommendService(),
//                memberService(logTrace));
//    }
//
//    @Bean
//    public BoardRepository boardRepository(LogTrace logTrace) {
//        BoardRepository boardRepository = new JpaBoardRepositoryV3(springDataJpaBoardRepository, em);
//        BoardRepository proxy = (BoardRepository) Proxy.newProxyInstance(BoardRepository.class.getClassLoader(),
//                new Class[]{BoardRepository.class},
//                new LogTraceFilterHandler(boardRepository, logTrace, PATTERNS));
//
//        return proxy;
//    }
//
//    @Bean
//    public RecommendService recommendService(LogTrace logTrace) {
//        return new RecommendService(recommendRepository(logTrace));
//    }
//
//    @Bean
//    public RecommendRepository recommendRepository(LogTrace logTrace) {
//        RecommendRepository recommendRepository = new JpaRecommendRepositoryV2(springDataJpaRecommendRepository);
//        RecommendRepository proxy = (RecommendRepository) Proxy.newProxyInstance(RecommendRepository.class.getClassLoader(),
//                new Class[]{RecommendRepository.class},
//                new LogTraceFilterHandler(recommendRepository, logTrace, PATTERNS));
//
//        return proxy;
//    }
//
//    @Bean
//    public NotRecommendService notRecommendService() {
//        return new NotRecommendService(notRecommendRepository());
//    }
//
//    @Bean
//    public NotRecommendRepository notRecommendRepository() {
//        return new JpaNotRecommendRepositoryV1(em);
//    }
//
//    @Bean
//    public MemberService memberService(LogTrace logTrace) {
//        return new MemberService(memberRepository(logTrace));
//    }
//
//    @Bean
//    public MemberRepository memberRepository(LogTrace logTrace) {
//        MemberRepository memberRepository = new JpaMemberRepositoryV2(springDataJpaMemberRepository);
//        MemberRepository proxy = (MemberRepository) Proxy.newProxyInstance(MemberRepository.class.getClassLoader(),
//                new Class[]{MemberRepository.class},
//                new LogTraceFilterHandler(memberRepository, logTrace, PATTERNS));
//
//        return proxy;
//    }
}
