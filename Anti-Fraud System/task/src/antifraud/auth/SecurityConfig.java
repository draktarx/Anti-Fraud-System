package antifraud.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        configureCsrf(http);
        configureHeaders(http);
        configureExceptionHandling(http);
        configureSessionManagement(http);
        configureAuthorization(http);
        configureHttpBasic(http);
        return http.build();
    }

    private void configureExceptionHandling(HttpSecurity http) throws Exception {
        http.exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint());
    }

    private void configureSessionManagement(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    private void configureCsrf(HttpSecurity http) throws Exception {
        http.csrf().disable();
    }

    private void configureHeaders(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable();
    }

    private void configureAuthorization(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(config -> config
                // Permit all for actuator shutdown
                .requestMatchers("/actuator/shutdown")
                .permitAll()

                // POST /api/auth/user - Accessible for all roles (Anonymous, MERCHANT, ADMINISTRATOR, SUPPORT)
                .requestMatchers(HttpMethod.POST, "/api/auth/user")
                .permitAll()

                // DELETE /api/auth/user - Only ADMINISTRATOR can delete users
                .requestMatchers(HttpMethod.DELETE, "/api/auth/user/{username}")
                .hasRole("ADMINISTRATOR")

                // GET /api/auth/list - ADMINISTRATOR and SUPPORT have access
                .requestMatchers(HttpMethod.GET, "/api/auth/list")
                .hasAnyRole("ADMINISTRATOR", "SUPPORT")

                // POST /api/antifraud/transaction - Only MERCHANT has access
                .requestMatchers(HttpMethod.POST, "/api/antifraud/transaction")
                .hasRole("MERCHANT")

                // change
                .requestMatchers(HttpMethod.PUT, "/api/auth/access")
                .hasRole("ADMINISTRATOR")
                .requestMatchers(HttpMethod.PUT, "/api/auth/role")
                .hasRole("ADMINISTRATOR")

                // Access control for suspicious IPs (Only SUPPORT can manage)
                .requestMatchers(HttpMethod.POST, "/api/antifraud/suspicious-ip")
                .hasRole("SUPPORT")
                .requestMatchers(HttpMethod.DELETE, "/api/antifraud/suspicious-ip/{ip}")
                .hasRole("SUPPORT")
                .requestMatchers(HttpMethod.GET, "/api/antifraud/suspicious-ip")
                .hasRole("SUPPORT")

                // Access control for stolen cards (Only SUPPORT can manage)
                .requestMatchers(HttpMethod.POST, "/api/antifraud/stolencard")
                .hasRole("SUPPORT")
                .requestMatchers(HttpMethod.DELETE, "/api/antifraud/stolencard/{number}")
                .hasRole("SUPPORT")
                .requestMatchers(HttpMethod.GET, "/api/antifraud/stolencard")
                .hasRole("SUPPORT")

                // GET /api/antifraud/history - Only SUPPORT has access
                .requestMatchers(HttpMethod.GET, "/api/antifraud/history")
                .hasRole("SUPPORT")
                .requestMatchers(HttpMethod.GET, "/api/antifraud/history/{number}")
                .hasRole("SUPPORT")

                // PUT /api/antifraud/transaction - Only SUPPORT has access
                .requestMatchers(HttpMethod.PUT, "/api/antifraud/transaction")
                .hasRole("SUPPORT")

                // Deny all other requests
                .anyRequest()
                .denyAll());
    }

    private void configureHttpBasic(HttpSecurity http) throws Exception {
        http.httpBasic();
    }

}
