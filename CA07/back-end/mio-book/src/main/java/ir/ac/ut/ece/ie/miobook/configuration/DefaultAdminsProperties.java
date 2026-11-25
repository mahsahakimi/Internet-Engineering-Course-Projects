package ir.ac.ut.ece.ie.miobook.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "app")
@Getter
@Setter
public class DefaultAdminsProperties {

    private List<AdminConfig> defaultAdmins = new ArrayList<>();

    @Getter
    @Setter
    public static class AdminConfig {
        private String username;
        private String password;
        private String email;
    }
}
