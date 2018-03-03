package user;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import java.util.Objects;

/**
 * A user of Amazon Videos.
 */
@DynamoDBTable(tableName = "...")
public class User {

    @DynamoDBHashKey
    private String login;
    private String name;
    private String phoneNumber;

    public User() {}

    public User(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof User
                && Objects.equals(login, ((User) other).login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login);
    }
}
