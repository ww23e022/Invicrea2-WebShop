package at.technikum.Invicrea2WebShopbackend.security;

import at.technikum.Invicrea2WebShopbackend.security.jwt.JwtAuthenticationFilter;
import at.technikum.Invicrea2WebShopbackend.security.user.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfiguration(JwtAuthenticationFilter jwtAuthenticationFilter,
                                 CustomUserDetailsService userDetailsService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.addFilterBefore(jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class);
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable);

        httpSecurity.sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );
        httpSecurity
                .authorizeHttpRequests(
                        registry -> registry
                                .requestMatchers("/").permitAll()
                                .requestMatchers("/auth/**").permitAll()
                                .requestMatchers("/users/register").permitAll()
                                .requestMatchers("/players").permitAll()
                                .requestMatchers("/players/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/players/{id}").permitAll()
                                .requestMatchers(HttpMethod.DELETE,"/players/{id}").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT,"/players/{id}").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/items").permitAll()
                                .requestMatchers(HttpMethod.GET, "/orders/status=sold").permitAll()
                                .requestMatchers("/swagger-ui/**").permitAll()
                                .requestMatchers("/shopping-cart/**").authenticated()
                                .requestMatchers("/error").permitAll()
                                .anyRequest().authenticated());
        httpSecurity.httpBasic(basic -> {});
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            HttpSecurity httpSecurity,
            UserDetailsService userDetailsService) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());

        return authenticationManagerBuilder.build();
    }
}
