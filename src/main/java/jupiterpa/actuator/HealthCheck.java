package jupiterpa.actuator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.boot.actuate.health.Health.Builder;

@Component
public class HealthCheck implements HealthIndicator, Health {
    private static final Marker TECHNICAL = MarkerFactory.getMarker("TECHNICAL");
	private static final Logger logger = LoggerFactory.getLogger(HealthCheck.class);
	
	final Map<String,HealthInfo> health = new HashMap<>();
  
    @Override
    public org.springframework.boot.actuate.health.Health health() {
    	boolean h = true;
    	if (health.size() == 0) {
    		return org.springframework.boot.actuate.health.Health.down().withDetail("service", "down").build();
    	} else {
    		for( HealthInfo info: health.values()) {
				if (info.error) {
					h = false;
					break;
				}
    		}
    		Builder b;
    		if (h)
    			b = org.springframework.boot.actuate.health.Health.up();
    		else
    			b = org.springframework.boot.actuate.health.Health.down();
    		for ( HealthInfo info: health.values()) {
    			b = b.withDetail(info.name, info.message);
    		}
    		return b.build();
    	}
    } 
	@Override
	public void setHealth(HealthInfo info) {
		logger.info(TECHNICAL, "Health of {} set to {}",info.getName(), info.getMessage());
		health.put(info.getName(),info);
	}
}