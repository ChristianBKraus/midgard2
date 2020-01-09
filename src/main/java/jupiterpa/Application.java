package jupiterpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {
    private static final Marker TECHNICAL = MarkerFactory.getMarker("TECHNICAL");
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static void main(String args[]){
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... arg0) throws Exception {
        logger.info(TECHNICAL,"Application started");
    }

}