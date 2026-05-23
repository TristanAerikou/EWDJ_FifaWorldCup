package lv.ewdj.fifaworldcup.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/login/**", "/css/**", "/403/**", "/error").permitAll()
                        .requestMatchers("/game/create", "/game/edit/**").hasRole("ADMIN")
                        .anyRequest().hasAnyRole("USER", "ADMIN")

                ).formLogin(form -> form
                        .defaultSuccessUrl("/", false)
                )

                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                )

                .exceptionHandling(handling -> handling
                        .accessDeniedPage("/403")
                )
        ;
        return http.build();
    }
}
