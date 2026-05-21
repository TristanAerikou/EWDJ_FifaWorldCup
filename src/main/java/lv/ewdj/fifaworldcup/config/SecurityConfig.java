package lv.ewdj.fifaworldcup.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
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
                        .requestMatchers("/login**", "/css/**", "/403**").permitAll()
                        .requestMatchers("/game/create").hasRole("ADMIN")
                        .anyRequest().hasAnyRole("USER", "ADMIN")
                ).formLogin(form -> form
                        .defaultSuccessUrl("/home", false)
                )
//                .formLogin()
                .logout(logout -> logout
                        .logoutSuccessUrl("/home/")
                )
                .exceptionHandling(handling -> handling
                        .accessDeniedPage("/403")
                )
        ;
        return http.build();
    }
}
