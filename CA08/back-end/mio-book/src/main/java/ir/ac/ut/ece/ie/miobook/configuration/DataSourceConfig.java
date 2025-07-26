package ir.ac.ut.ece.ie.miobook.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.nio.file.Files;
import java.nio.file.Paths;

@Configuration
public class DataSourceConfig {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    private static final String SECRET_PATH = "/run/secrets/mysql_root_password";

    @Bean
    public DataSource dataSource() throws Exception {
        String password = new String(Files.readAllBytes(Paths.get(SECRET_PATH))).trim();

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(dbUsername);
        dataSource.setPassword(password);

        return dataSource;
    }
}