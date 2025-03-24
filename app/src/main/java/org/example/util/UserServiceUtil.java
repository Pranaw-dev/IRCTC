package org.example.util;
import org.mindrot.jbcrypt.BCrypt;

import java.util.*;

public class UserServiceUtil
{
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
