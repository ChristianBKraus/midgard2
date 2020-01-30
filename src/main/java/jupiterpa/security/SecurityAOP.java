package jupiterpa.security;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import org.slf4j.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class SecurityAOP
{
	
    private static final Marker TECHNICAL = MarkerFactory.getMarker("TECHNICAL");
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Around("execution(* jupiterpa.controller.*.*(..))")
	public Object log(ProceedingJoinPoint joinPoint) throws Throwable {

        MDC.put("endpoint", joinPoint.getSignature().toString() );
        logger.info(TECHNICAL, " Service {} ({})", joinPoint.getSignature().toString(),  Arrays.toString(joinPoint.getArgs()));
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
        	try {
        	User user = (User) auth.getPrincipal();
        	if (user != null) {
        		logger.info(TECHNICAL, " User: {}", user.getUsername());
        	
        		UserStore.setUser(user);
        		MDC.put("user", user.getUsername());
        	}
        	} catch (ClassCastException exp) {
        		//do nothing
        	}
        }
        
		try {
			Object result = joinPoint.proceed();
			if (result != null) {
				logger.info(TECHNICAL, " -> Result: {}", result.toString());
			} else {
				logger.info(TECHNICAL, " -> Done");
			}
			return result;
		} catch (Throwable e) {
			logger.error(TECHNICAL, "  => " + e ); 
			e.printStackTrace();
			throw e;
		}
	}
}