package numberlink.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final OauthLoginSuccessHandler oauthLoginSuccessHandler;
    private final OauthLoginFailureHandler oauthLoginFailureHandler;
    private final SessionEpochFilter sessionEpochFilter;
    private final UserSessionFilter userSessionFilter;

    public SecurityConfig(
            OauthLoginSuccessHandler oauthLoginSuccessHandler,
            OauthLoginFailureHandler oauthLoginFailureHandler,
            SessionEpochFilter sessionEpochFilter,
            UserSessionFilter userSessionFilter
    ) {
        this.oauthLoginSuccessHandler = oauthLoginSuccessHandler;
        this.oauthLoginFailureHandler = oauthLoginFailureHandler;
        this.sessionEpochFilter = sessionEpochFilter;
        this.userSessionFilter = userSessionFilter;
    }

    @Bean
    public CorsConfigurationSource corsConfiguration() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:7000"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(CORS -> CORS.configurationSource(corsConfiguration()))
                .csrf(CSRF -> CSRF.disable())
                .exceptionHandling(ex -> ex
                        // SPA/API: return 401 instead of redirecting to OAuth login HTML
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )
                .authorizeHttpRequests(auth -> auth
                        // Auth / OAuth
                        .requestMatchers(
                                "/api/auth/**",
                                "/api/register",
                                "/api/login",
                                "/api/login/2fa",
                                "/api/logout",
                                "/api/profile",
                                "/api/me",
                                "/api/mail/send",
                                "/api/resend-verification",
                                "/api/verify-email",
                                "/api/forgot-password",
                                "/api/reset-password",
                                "/api/email-change/cancel",
                                "/uploads/**",
                                "/login/**",
                                "/oauth2/**",
                                "/error",
                                "/actuator/health",
                                "/v3/api-docs",
                                "/v3/api-docs/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**"
                        ).permitAll()

                        // Main game page APIs (guest OK)
                        .requestMatchers(HttpMethod.GET,
                                "/api/create-map",
                                "/api/width",
                                "/api/height",
                                "/api/generate-name"
                        ).permitAll()
                        .requestMatchers(HttpMethod.POST,
                                "/api/map-check",
                                "/api/hint-check",
                                "/api/set-username"
                        ).permitAll()

                        // Public community reads
                        .requestMatchers(HttpMethod.GET,
                                "/api/comment",
                                "/api/rating/avg",
                                "/api/rating/amount",
                                "/api/rating/percentage",
                                "/api/score/sort"
                        ).permitAll()

                        // Writes + personal rating/score require login
                        .requestMatchers(
                                "/api/comment",
                                "/api/comment/**",
                                "/api/rating",
                                "/api/rating/**",
                                "/api/score",
                                "/api/score/**"
                        ).authenticated()

                        // Everything else also requires login
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(oauthLoginSuccessHandler)
                        .failureHandler(oauthLoginFailureHandler)
                )
                .addFilterAfter(sessionEpochFilter, SecurityContextHolderFilter.class)
                .addFilterAfter(userSessionFilter, SessionEpochFilter.class);

        return http.build();
    }
}
