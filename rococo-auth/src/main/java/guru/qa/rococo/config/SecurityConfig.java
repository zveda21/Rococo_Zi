package guru.qa.rococo.config;

import guru.qa.rococo.service.cors.CookieCsrfFilter;
import guru.qa.rococo.service.cors.CorsCustomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
public class SecurityConfig {

    private final CorsCustomizer corsCustomizer;

    @Autowired
    public SecurityConfig(CorsCustomizer corsCustomizer) {
        this.corsCustomizer = corsCustomizer;
    }

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        corsCustomizer.corsCustomizer(http);

        return http.authorizeHttpRequests(customizer -> customizer
                        .requestMatchers(
                                antMatcher("/register"),
                                antMatcher("/images/**"),
                                antMatcher("/styles/**"),
                                antMatcher("/fonts/**")
                        ).permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        // https://stackoverflow.com/a/74521360/65681
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                )
                .addFilterAfter(new CookieCsrfFilter(), BasicAuthenticationFilter.class)
                .formLogin(login -> login
                        .loginPage("/login")
                        .permitAll())
                .logout(logout -> logout
                        .logoutRequestMatcher(antMatcher("/logout"))
                        // https://github.com/spring-projects/spring-authorization-server/issues/266
                        .deleteCookies("JSESSIONID", "XSRF-TOKEN")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))
                )
                .sessionManagement(sm -> sm.invalidSessionUrl("/login"))
                .build();
    }
}
