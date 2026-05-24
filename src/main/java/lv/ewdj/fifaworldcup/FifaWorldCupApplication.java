package lv.ewdj.fifaworldcup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class FifaWorldCupApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(FifaWorldCupApplication.class, args);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "/public");
        registry.addViewController("/403").setViewName("error/403");
        registry.addViewController("/error").setViewName("error/error"); // unused because of GlobalExceptionAdvice
    }

}
