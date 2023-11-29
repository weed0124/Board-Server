package practice.springmvc.config;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import practice.springmvc.config.advice.LogTraceAdvice;
import practice.springmvc.domain.board.BoardService;
import practice.springmvc.domain.board.notrecommend.NotRecommendService;
import practice.springmvc.domain.board.notrecommend.repository.NotRecommendRepository;
import practice.springmvc.domain.board.notrecommend.repository.jpa.JpaNotRecommendRepositoryV2;
import practice.springmvc.domain.board.notrecommend.repository.jpa.SpringDataJpaNotRecommendRepository;
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
    private final SpringDataJpaNotRecommendRepository springDataJpaNotRecommendRepository;
    private final EntityManager em;
    private final LogTraceAdvice logTraceAdvice;

    @Bean
    public BoardService boardService() {
        return getLogAdviceProxy(new BoardService(boardRepository(),
                recommendService(),
                notRecommendService(),
                memberService()));
    }

    @Bean
    public BoardRepository boardRepository() {
        return new JpaBoardRepositoryV3(springDataJpaBoardRepository, em);
//        return getLogAdviceProxy((BoardRepository) new JpaBoardRepositoryV3(springDataJpaBoardRepository, em));
    }

    @Bean
    public RecommendService recommendService() {
        return getLogAdviceProxy(new RecommendService(recommendRepository()));
    }

    @Bean
    public RecommendRepository recommendRepository() {
        return getLogAdviceProxy((RecommendRepository) new JpaRecommendRepositoryV2(springDataJpaRecommendRepository));
    }

    @Bean
    public NotRecommendService notRecommendService() {
        return getLogAdviceProxy(new NotRecommendService(notRecommendRepository()));
    }

    @Bean
    public NotRecommendRepository notRecommendRepository() {
        return getLogAdviceProxy((NotRecommendRepository) new JpaNotRecommendRepositoryV2(springDataJpaNotRecommendRepository));
    }

    @Bean
    public MemberService memberService() {
        return getLogAdviceProxy(new MemberService(memberRepository()));
    }

    @Bean
    public MemberRepository memberRepository() {
        return getLogAdviceProxy((MemberRepository) new JpaMemberRepositoryV2(springDataJpaMemberRepository));
    }

    /**
     * 메소드 실행시간 및 로그 추가 시 사용 Proxy
     * @param proxy
     * @return
     * @param <T>
     */
    private <T> T getLogAdviceProxy(T proxy) {
        ProxyFactory proxyFactory = new ProxyFactory(proxy);
        proxyFactory.addAdvisor(getAdvisor());
        T result = (T) proxyFactory.getProxy();
        return result;
    }

    /**
     * Pointcut(Filter역할) + Advice(부가기능) = Advisor(어떤 클래스, 메소드에 어떤 부가기능을 지정할지 알고있음)
     * @return
     */
    private Advisor getAdvisor() {
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedNames("save*", "find*", "update", "add*");
        return new DefaultPointcutAdvisor(pointcut, logTraceAdvice);
    }
}
