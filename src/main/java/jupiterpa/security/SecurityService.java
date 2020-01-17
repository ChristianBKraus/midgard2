package jupiterpa.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class SecurityService {
    public String getUser() {
        return UserStore.getUser().getUsername();
    }
    public void check(String user) throws Exception {
        if ( (!user.equals(UserStore.getUser().getUsername())) &&
              (UserStore.getUser().getAuthorities().stream().noneMatch( auth -> auth.getAuthority().equals("ADMIN"))) ) {
            throw new Exception("User is not allowed to see Character");
        }
    }
    public boolean allowed(String user) {
        String authUser = UserStore.getUser().getUsername();
        Collection<GrantedAuthority> authorities = UserStore.getUser().getAuthorities();
        boolean admin = authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        return (user.equals(authUser) || authUser.equals("admin") );
    }
}
