package com.example.springSecurity.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Collection;
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

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Login ve logout yok çünkü her requestte authenticate oluyoruz.
        // In memory databasede çalışır ve default username -> user, password her proje ayağa kalkınca yenilenir.
        http
                /*
                * Bilgi çalınmasını önlemek için csrf token verilir ve bu token ile doğrulama gerçekleşir.
                * Browser gibi cookie tutan yapılar için uygundur.
                * Postman Interceptor (Cookie tutmak için kullanılır.)
                * */
                //.csrf().disable()

                /*
                * Default name X-XSRF-TOKEN dır.
                * Bu değeri header'a setleyerekte çalışılabilir.
                * */
                .csrf().disable()
                //.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                //.and()

                .authorizeRequests()
                .antMatchers("/", "index", "/css/*", "/js/*")  // Gelen url patternler için auth isteme şeklinde belirttik.
                .permitAll() // Tüm yetkiler için geçerli demek

                // Buradaki örnek role based auth ve permission base auth için yapılmıştır.
                // Ayrıca önce işleme giren kuralın gelen istekte geçerli olduğunu gösterir.
                // Eğer yetkiye uymayan bir durum varsa 403 forbidden alınacaktır.
                .antMatchers("api/v1/student/create")
                .hasRole(MANAGER.name())

                .antMatchers(HttpMethod.DELETE,"api/v1/student/**")
                .hasAuthority(STUDENT_WRITE.getPermission())
                .antMatchers(HttpMethod.PUT,"api/v1/student/**")
                .hasAuthority(STUDENT_WRITE.getPermission())
                .antMatchers("api/v1/student/**")
                .hasAnyRole(MANAGER.name(), TEACHER.name())
                .antMatchers(HttpMethod.GET, "api/v1/student/**")
                .hasAuthority(STUDENT_READ.getPermission())


                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }

    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        // ROLE ve permission birbirinden ayrı şeyler. permissionların birlikteliği role'u tanımlar.
        UserDetailsService service = new InMemoryUserDetailsManager(
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
                        .authorities(Stream.of(TEACHER.getPermissions(), MANAGER.getPermissions())
                                .flatMap(Collection::stream).collect(Collectors.toSet()))
                        .build(),
                User.builder().username("samantha")
                        .password(passwordEncoder.encode("password"))
                        .roles(STUDENT.name())
                        .authorities(STUDENT.getPermissions())
                        .build()
        );
        service.loadUserByUsername("anna")
                .getAuthorities()
                .forEach(a -> log.info(a.getAuthority()));
        return service;
    }
}
