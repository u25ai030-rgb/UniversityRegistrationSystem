package models;

public abstract class User {
    protected String email;
    protected String password;
    protected String role;

    public User(String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public boolean login(String inputEmail, String inputPassword) {
        return this.email.equals(inputEmail) && this.password.equals(inputPassword);
    }

    public String getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }
}