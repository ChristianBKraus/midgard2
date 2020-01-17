package jupiterpa.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.codec.Base64;

import java.nio.charset.Charset;
import java.util.UUID;

public class UserStore {
	
	private static ThreadLocal<User> user = new ThreadLocal<>();

	public static void setUser(User new_user) {
		user.set(new_user);
	}
	public static User getUser() {
		return user.get();
	}
}
