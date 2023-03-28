package es.tatanca.logistics;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class AppConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Value("${cust.data.hoursSameCity}")
    private Float hoursSameCity;

    @Value("${cust.data.mediaVelocity}")
    private Float mediaVelocity;

    @Bean
    public Float getHoursSameCity() {
        return hoursSameCity;
    }

    @Bean
    public Float getMediaVelocity() {
        return mediaVelocity;
    }



}