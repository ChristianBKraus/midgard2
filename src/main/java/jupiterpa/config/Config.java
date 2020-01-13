package jupiterpa.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

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
