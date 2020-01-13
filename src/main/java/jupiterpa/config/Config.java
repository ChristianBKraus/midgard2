package jupiterpa.config;

import jupiterpa.service.SettingsService;
import jupiterpa.service.SettingsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.URISyntaxException;

@Configuration
@ComponentScan(basePackages = {"jupiterpa"})
public class Config {
    /*
    @Bean
    SettingsService getSettingsService() throws IOException, URISyntaxException {
        return new SettingsServiceImpl();
    }
    */

}
