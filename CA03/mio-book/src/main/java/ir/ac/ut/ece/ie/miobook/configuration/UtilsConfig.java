package ir.ac.ut.ece.ie.miobook.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.ac.ut.ece.ie.miobook.utils.MySimpleDate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UtilsConfig {

    @Bean
    public MySimpleDate mySimpleDate() {
        return new MySimpleDate();
    }

    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }
}