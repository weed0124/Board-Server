package practice.springmvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import practice.springmvc.domain.board.config.JpaConfig;
import practice.springmvc.domain.board.config.SpringDataJpaConfig;

//@Import(MemoryConfig.class)
//@Import(JpaConfig.class)
@Import(SpringDataJpaConfig.class)
@SpringBootApplication
public class SpringMvcApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringMvcApplication.class, args);
	}

}
