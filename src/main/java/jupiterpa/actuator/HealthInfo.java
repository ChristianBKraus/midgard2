package jupiterpa.actuator;

import lombok.*;

@Data @AllArgsConstructor @NoArgsConstructor
public class HealthInfo {
	String name;
	boolean error;
	String message;
}
