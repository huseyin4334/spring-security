package com.example.springSecurity.configuration;

import com.example.springSecurity.service.ApplicationUserService;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.springSecurity.bean.enums.ApplicationRole.*;
import static com.example.springSecurity.bean.enums.ApplicationPermission.*;

@Slf4j
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true) // preAuthorize için kullanılıyor
@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ApplicationUserService applicationUserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Login ve logout ayarlanır. Proje giriş yapıldığında session id ataması yapar.
        // Logout olunana kadar session id ile doğrulamayı gerçekleştirir.
        // session doğrulamasını istersek db ile istersekte in memory ile yapabiliriz.
        // JSESSIONID -> session id adı
        http

                .csrf().disable()
                //.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                //.and()

                .authorizeRequests()
                .antMatchers("/", "index", "/css/*", "/js/*").permitAll()
                .antMatchers("api/v1/student/create").hasRole(MANAGER.name())
                .antMatchers(HttpMethod.DELETE,"api/v1/student/**").hasAuthority(STUDENT_WRITE.getPermission())
                .antMatchers(HttpMethod.PUT,"api/v1/student/**").hasAuthority(STUDENT_WRITE.getPermission())
                .antMatchers("api/v1/student/**").hasAnyRole(MANAGER.name(), TEACHER.name())
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                    // default bu şekilde çalışır.
                    .loginPage("/login").permitAll()
                    .defaultSuccessUrl("/courses", true) // redirect here if auth ok
                    .usernameParameter("username")
                    .passwordParameter("password")
                .and()
                // Sessionın ne kadar süreli açık kalmasını istediğimi belirtiyoruz.
                // remember-me default cookie name for token
                .rememberMe()
                    .rememberMeParameter("remember-me") // formun üzerindeki parametrenin adı
                    .rememberMeCookieName("remember-me") // default
                    .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21))
                    .key("verySecured")
                .and()
                .logout()
                    .logoutUrl("/logout")
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET")) // Yalnızca get methodu ile logout ol.
                    .clearAuthentication(true)
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID", "remember-me")
                    .logoutSuccessUrl("/login");
    }

    @Override
    public void configure(AuthenticationManagerBuilder authentication) throws Exception {
        authentication.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(applicationUserService);
        return provider;
    }
}
