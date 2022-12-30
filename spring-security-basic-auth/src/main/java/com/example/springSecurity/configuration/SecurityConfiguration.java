package com.example.springSecurity.configuration;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Arrays;
import java.util.Collection;

import static com.example.springSecurity.bean.enums.ApplicationRole.*;
import static com.example.springSecurity.bean.enums.ApplicationPermission.*;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Login ve logout yok çünkü her requestte authenticate oluyoruz.
        // In memory databasede çalışır ve default username -> user, password her proje ayağa kalkınca yenilenir.
        http
                .authorizeRequests()
                .antMatchers("/", "index", "/css/*", "/js/*")  // Gelen url patternler için auth isteme şeklinde belirttik.
                .permitAll() // Tüm yetkiler için geçerli demek

                // Buradaki örnek role based auth için yapılmıştır.
                // Ayrıca önce işleme giren kuralın geçerli olduğunu gösterir.
                .antMatchers("api/v1/student/create")
                .hasRole(MANAGER.name())
                .antMatchers("api/v1/student/**")
                .hasAnyRole(MANAGER.name(), TEACHER.name())


                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }

    @Bean
    @Override
    protected UserDetailsService userDetailsService() {

        return new InMemoryUserDetailsManager(
                User.builder().username("anna")
                        .password(passwordEncoder.encode("password"))
                        .roles(MANAGER.name())
                        .authorities(MANAGER.getPermissions())
                        .build(),
                User.builder().username("john")
                        .password(passwordEncoder.encode("password"))
                        .roles(TEACHER.name())
                        .authorities(TEACHER.getPermissions())
                        .build(),
                User.builder().username("gerard")
                        .password(passwordEncoder.encode("password"))
                        .roles(TEACHER.name(), MANAGER.name())
                        .authorities((Collection<? extends GrantedAuthority>)Iterables.concat(TEACHER.getPermissions(), MANAGER.getPermissions()))
                        .build(),
                User.builder().username("anna")
                        .password(passwordEncoder.encode("password"))
                        .roles(STUDENT.name())
                        .authorities(STUDENT.getPermissions())
                        .build()
        );
    }
}
