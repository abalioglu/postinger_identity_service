package com.kafein.intern.postinger_identity_service.config;

import com.kafein.intern.postinger_identity_service.jwt.JwtFilter;
import com.kafein.intern.postinger_identity_service.jwt.JwtTokenProvider;
import com.kafein.intern.postinger_identity_service.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserService userService;
    private final JwtFilter jwtFilter;
    private final JwtTokenProvider jwtTokenProvider;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider, UserService userService, JwtFilter jwtFilter, JwtTokenProvider jwtTokenProvider1) {
        this.userService = userService;
        this.jwtFilter = jwtFilter;
        this.jwtTokenProvider = jwtTokenProvider1;
    }

    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userService);
    }
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
                .csrf().disable()
                .authorizeRequests().antMatchers("/user/login").permitAll()
                .antMatchers("/user/register").permitAll()
                .antMatchers("/role/save-update").permitAll()
                .antMatchers("/authority/save-update").permitAll()
                .antMatchers("/user/get-username").permitAll()
                .antMatchers("/user/increment-follower-count").permitAll()
                .antMatchers("/user/decrement-follower-count").permitAll()
                .antMatchers("/user/increment-followed-count").permitAll()
                .antMatchers("/user/decrement-followed-count").permitAll()
                .antMatchers("/user/get-id").permitAll()
                .anyRequest().authenticated()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .logout()
                .logoutUrl("/user/logout")
                .deleteCookies("JWT_TOKEN") // Delete the JWT token cookie on logout
                .logoutSuccessHandler((request, response, authentication) -> {
                    jwtTokenProvider.invalidateToken(request, response);
                    response.setStatus(HttpServletResponse.SC_OK);
                })
                .permitAll();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
