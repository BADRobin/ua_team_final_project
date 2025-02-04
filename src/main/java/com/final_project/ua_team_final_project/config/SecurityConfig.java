package com.final_project.ua_team_final_project.config;

import com.final_project.ua_team_final_project.services.CustomUserDetailsService;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {
    private final DataSource dataSource;
    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(DataSource dataSource, CustomUserDetailsService customUserDetailsService) {
        this.dataSource = dataSource;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(auth -> auth
            .requestMatchers("/login", "/about", "/js/**", "/css/**").permitAll()
            .requestMatchers("/organization/**", "/selectedProducts", "/fragments/**").authenticated()
            .requestMatchers("/organization/admin/**").hasRole("ADMIN")
            .requestMatchers("/organization/userpage/**","/organization/**","/organization/editSelectedProducts", "/organization/submitSelection", "/organization/editProducts/**", "/organization/confirmOrder").hasRole("USER")
            .requestMatchers("/organization/pageofhead/**").hasRole("HEAD")
            .requestMatchers("/organization/pageoffinco/**").hasRole("FINCO")
            .requestMatchers("/organization/supply/**").hasRole("SUPPLIER")
            .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                );
//                .csrf(csrf -> csrf.disable());

        httpSecurity.rememberMe(rememberMe -> rememberMe
                        .key(Dotenv.load().get("REMEMBERME_KEY"))
                        .tokenValiditySeconds(7 * 24 * 60 * 60)
                        .tokenRepository(persistentTokenRepository())
                )
                .userDetailsService(customUserDetailsService);

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JdbcTokenRepositoryImpl persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }
}