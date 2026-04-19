package models;

public class Administrator extends User {
    public Administrator(String email, String password) {
        super(email, password, "Administrator");
    }
}