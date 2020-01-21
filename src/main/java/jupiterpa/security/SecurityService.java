package jupiterpa.security;

import jupiterpa.service.UserException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class SecurityService {
    public String getUser() {
        return UserStore.getUser().getUsername();
    }
    public void check(String user) throws UserException {
        if ( (!user.equals(UserStore.getUser().getUsername())) &&
              (UserStore.getUser().getAuthorities().stream().noneMatch( auth -> auth.getAuthority().equals("ADMIN"))) ) {
            throw new UserException("User is not allowed to see Character");
        }
    }
    public boolean allowed(String user) {
        String authUser = UserStore.getUser().getUsername();
        Collection<GrantedAuthority> authorities = UserStore.getUser().getAuthorities();
        return (user.equals(authUser) || authUser.equals("admin") );
    }
}
