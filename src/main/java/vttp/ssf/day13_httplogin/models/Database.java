package vttp.ssf.day13_httplogin.models;

import java.util.Arrays;
import java.util.List;

public class Database {
    
    private static final List<User> dataBase = Arrays.asList(
        new User("frediscool", "abcdefgh"),
        new User("amyisnice", "ijklmnop"),
        new User("johnistall", "qrstuvwx"));

    public static boolean isUsernameValid(String username) {
        for (User user : dataBase) {
            if (username.equals(user.getUsername())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isPasswordValid(String password) {
        for (User user : dataBase) {
            if (password.equals(user.getPassword())) {
                return true;
            }
        }
        return false;
    }

}
