package jupiterpa.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data @AllArgsConstructor @NoArgsConstructor
@ConfigurationProperties(prefix="application")
public class ApplicationConfig {
	private String name;
	private String description;
	private String version;
	private String adminPassword;
	private String userPassword;
}
