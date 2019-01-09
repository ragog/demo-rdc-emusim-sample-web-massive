package data;

import util.PropertyHelper;

/**
 * Created by grago on 27.09.17.
 */
public enum Credentials {

    USER_VALID("username.valid", "password.valid"),
    USER_INVALID_PASSWORD("username.valid", "password.invalid"),
    USER_INVALID_USER("username.invalid", "password.valid"),
    USER_INVALID_ALL("username.invalid", "password.invalid"),
    USER_LOCKEDOUT("username.locked", "password.valid"),
    EMPTY_USERNAME("empty", "password.valid"),
    EMPTY_PASSWORD("username.valid", "empty"),
    EMPTY("empty", "empty");

    public String username;
    public String password;

    Credentials(String username, String password) {
        this.username = PropertyHelper.getProperty(username);
        this.password = PropertyHelper.getProperty(password);
    }

}
