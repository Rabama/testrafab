package es.tatanca.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.NullRequestCache;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


// https://docs.spring.io/spring-security/reference/servlet/authorization/authorize-http-requests.html

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Autowired
    private UserDetailsService uds;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.
                authorizeHttpRequests()

//                .anyRequest().permitAll();

//                    .requestMatchers("/welcome", "/home").permitAll()
                .requestMatchers("/register", "/saveUser").permitAll() // OJO CON ESTO

                .requestMatchers("/css/*").permitAll()
                .requestMatchers("/js/*").permitAll()

                .requestMatchers(HttpMethod.POST, "/city/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/truck/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/cargo/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/order/**").authenticated()

//                    .requestMatchers("/index")authenticated()

                .requestMatchers(HttpMethod.GET, "/entities/**").authenticated()
                .requestMatchers(HttpMethod.GET, "/queries/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/query/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/save/**").authenticated()

                .anyRequest().authenticated()
                .and().

                formLogin()
                .loginPage("/login").permitAll()
                .defaultSuccessUrl("/index",true)
                .and().

                logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .and().

                requestCache().requestCache(new NullRequestCache())
                .and().

//                exceptionHandling().accessDeniedPage("/accessDenied")
//                .and().

        authenticationProvider(authenticationProvider());




//                sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and().

//                .csrf().disable()

//                .authorizeHttpRequests()

//                .anyRequest().permitAll();

/*
                .requestMatchers("/home","/register","/saveUser").permitAll()

                .requestMatchers("/welcome").authenticated()
                .requestMatchers("/admin").hasAuthority("Admin")
                .requestMatchers("/mgr").hasAuthority("Manager")
                .requestMatchers("/emp").hasAuthority("Employee")
                .requestMatchers("/hr").hasAuthority("HR")
                .requestMatchers("/common").hasAnyAuthority("Employeee,Manager,Admin")

                .requestMatchers("/citysave").authenticated()
                .requestMatchers("/city/**").authenticated()
                .requestMatchers("/queries/**").authenticated()

                .anyRequest().authenticated()

                .and()
                .formLogin()
                .defaultSuccessUrl("/welcome",true)

                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))

                .and()
                .exceptionHandling()
                .accessDeniedPage("/accessDenied")

                .and()
                .authenticationProvider(authenticationProvider());
*/
        return http.build();

    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(uds);
        authenticationProvider.setPasswordEncoder(encoder);
        return authenticationProvider;
    }

}