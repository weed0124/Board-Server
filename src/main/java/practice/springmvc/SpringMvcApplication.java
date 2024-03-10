package practice.springmvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.data.web.PagedResourcesAssembler;
import practice.springmvc.aop.TraceAspect;
import practice.springmvc.config.advice.LogTraceAdvice;
import practice.springmvc.trace.logtrace.LogTrace;
import practice.springmvc.trace.logtrace.ThreadLocalTrace;

//@Import(MemoryConfig.class)
//@Import(JpaConfig.class)
//@Import(SpringDataJpaConfig.class)
//@Import(DynamicProxyConfig.class)
//@Import(ProxyFactoryConfig.class)
@Import(TraceAspect.class)
@EnableJpaAuditing
@EnableCaching
@SpringBootApplication(scanBasePackages = {"practice.springmvc.domain", "practice.springmvc.web", "practice.springmvc.annotation", "practice.springmvc.aop"})
public class SpringMvcApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringMvcApplication.class, args);
	}

	@Bean
	public LogTrace logTrace() {
		return new ThreadLocalTrace();
	}

	@Bean
	public LogTraceAdvice logAdvice(LogTrace logTrace) {
		return new LogTraceAdvice(logTrace);
	}

	@Bean
	public PagedResourcesAssembler pagedResourcesAssembler() {
		return new PagedResourcesAssembler(new HateoasPageableHandlerMethodArgumentResolver(), null);
	}
}
