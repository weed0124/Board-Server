package practice.springmvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import practice.springmvc.config.DynamicProxyConfig;
import practice.springmvc.config.ProxyFactoryConfig;
import practice.springmvc.config.advice.LogAdvice;
import practice.springmvc.trace.logtrace.LogTrace;
import practice.springmvc.trace.logtrace.ThreadLocalTrace;

//@Import(MemoryConfig.class)
//@Import(JpaConfig.class)
//@Import(SpringDataJpaConfig.class)
//@Import(DynamicProxyConfig.class)
@Import(ProxyFactoryConfig.class)
@SpringBootApplication(scanBasePackages = {"practice.springmvc.domain", "practice.springmvc.web"})
public class SpringMvcApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringMvcApplication.class, args);
	}

	@Bean
	public LogTrace logTrace() {
		return new ThreadLocalTrace();
	}

	@Bean
	public LogAdvice logAdvice(LogTrace logTrace) {
		return new LogAdvice(logTrace);
	}
}
