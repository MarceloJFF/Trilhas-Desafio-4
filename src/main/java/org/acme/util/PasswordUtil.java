package org.acme.util;

import jakarta.enterprise.context.ApplicationScoped;
import org.mindrot.jbcrypt.BCrypt;

@ApplicationScoped
public class PasswordUtil {
    public static String hash(String senha) {
        return BCrypt.hashpw(senha, BCrypt.gensalt());
    }

    public static boolean check(String senha, String hash) {
        return BCrypt.checkpw(senha, hash);
    }
}
