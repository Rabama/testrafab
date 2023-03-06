package es.tatanca.test;


import es.tatanca.test.Entities.Cargo.Cargo;
import es.tatanca.test.Entities.Driver.Driver;
import es.tatanca.test.Entities.Truck.Truck;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Configuration
public class TestConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Component
    public static class DriverStatusConverter implements Converter<String, Driver.EnumStatus> {
        @Override
        public Driver.EnumStatus convert(String source) {
            try {
                return Driver.EnumStatus.valueOf(source);
            } catch(Exception e) {
                return null; // or SortEnum.asc
            }
        }
    } // CargoStatusConverter
}