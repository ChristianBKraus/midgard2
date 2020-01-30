package jupiterpa.security;

import jupiterpa.service.UserException;

public interface SecurityService  {
    String getUser();
    void check(String user) throws UserException;
    boolean allowed(String user);
}
