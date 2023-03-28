package es.tatanca.logistics;

import es.tatanca.logistics.entities.Credentials.Credentials;
import es.tatanca.logistics.entities.Credentials.CredentialsServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.NullRequestCache;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Collections;
import java.util.List;
import java.util.Random;


// https://docs.spring.io/spring-security/reference/servlet/authorization/authorize-http-requests.html

@EnableWebSecurity
@Configuration
@AllArgsConstructor
@Slf4j
public class SecurityConfig {

//    @Autowired
    private final CredentialsServiceImpl userServiceImpl;

//    @Autowired
    private final UserDetailsService uds;

//    @Autowired
    private final BCryptPasswordEncoder encoder;


    @Bean
    public void checkAdminPassword() {
        long count = userServiceImpl.count();
        if (count == 0) {

            List<String> roles = Collections.singletonList("Admin");

            int leftLimit = 97;   // letter 'a'
            int rightLimit = 122; // letter 'z'
            int targetStringLength = 10;
            Random random = new Random();

            String generatedString = random.ints(leftLimit, rightLimit + 1)
                    .limit(targetStringLength)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();


            Credentials user = new Credentials();
            user.setUsername("Admin");
            user.setPassword(generatedString);
            user.setRoles(roles);
            Long id = userServiceImpl.saveUser(user);

            log.debug("Create Admin user with password "+generatedString);

        }
    }


    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(uds);
        authenticationProvider.setPasswordEncoder(encoder);
        return authenticationProvider;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.
                authorizeHttpRequests()

//                    .anyRequest().permitAll()


                    .requestMatchers("/accessDenied.html").permitAll()
                    .requestMatchers("/css/*").permitAll()
                    .requestMatchers("/js/*").permitAll()


//                    .requestMatchers(HttpMethod.POST,"/entities/user/save/").hasAnyRole("Employee", "Admin")
                    .requestMatchers(HttpMethod.POST,"/entities/user/save/").hasAnyAuthority("Employee", "Admin")



//                    // ResponseBody
//                    .requestMatchers(HttpMethod.GET, "/cargo/**").hasAnyAuthority("Employeee", "Admin")
//                    .requestMatchers(HttpMethod.POST, "/cargo/**").hasAnyAuthority("Employeee", "Admin")
//                    .requestMatchers(HttpMethod.GET, "/city/**").hasAnyAuthority("Employeee", "Admin")
//                    .requestMatchers(HttpMethod.POST, "/city/**").hasAnyAuthority("Employeee", "Admin")
//                    .requestMatchers(HttpMethod.GET, "/driver/**").hasAnyAuthority("Employeee", "Admin")
//                    .requestMatchers(HttpMethod.POST, "/driver/**").hasAnyAuthority("Employeee", "Admin")
//                    .requestMatchers(HttpMethod.GET, "/driverhasorder/**").hasAnyAuthority("Employeee", "Admin")
//                    .requestMatchers(HttpMethod.POST, "/driverhasorder/**").hasAnyAuthority("Employeee", "Admin")
//                    .requestMatchers(HttpMethod.GET, "/order/**").hasAnyAuthority("Employeee", "Admin")
//                    .requestMatchers(HttpMethod.POST, "/order/**").hasAnyAuthority("Employeee", "Admin")
//                    .requestMatchers(HttpMethod.GET, "/truck/**").hasAnyAuthority("Employeee", "Admin")
//                    .requestMatchers(HttpMethod.POST, "/truck/**").hasAnyAuthority("Employeee", "Admin")
//                    .requestMatchers(HttpMethod.GET, "/waypoint/**").hasAnyAuthority("Employeee", "Admin")
//                    .requestMatchers(HttpMethod.POST, "/waypoint/**").hasAnyAuthority("Employeee", "Admin")

                    .requestMatchers(HttpMethod.GET, "/user/**").hasAuthority("Admin")
                    .requestMatchers(HttpMethod.POST, "/user/**").hasAuthority("Admin")


                    // Templates
                    .requestMatchers(HttpMethod.GET,  "/entities/**").hasAnyAuthority("Employee", "Admin")
                    .requestMatchers(HttpMethod.POST,  "/entities/**").hasAnyAuthority("Employee", "Admin")
                    .requestMatchers(HttpMethod.GET, "/queries/**").hasAnyAuthority("Employee", "Admin")

                    .requestMatchers(HttpMethod.GET, "/drivers/**").hasAnyAuthority("Driver", "Admin")


//                    .requestMatchers(HttpMethod.GET, "/users/**").hasAuthority("Admin")
//                    .requestMatchers(HttpMethod.GET, "/users/**").authenticated()

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

                exceptionHandling().accessDeniedPage("/accessDenied")
                .and().

                authenticationProvider(authenticationProvider());


        return http.build();
    }


}