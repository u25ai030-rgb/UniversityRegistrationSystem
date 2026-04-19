package models;

public class Feedback<T> {
    private String studentEmail;
    private T data; 

    public Feedback(String studentEmail, T data) {
        this.studentEmail = studentEmail;
        this.data = data;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public T getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Student " + studentEmail + " said: " + data.toString();
    }
}