package models;

public class TeachingAssistant extends Student {

    public TeachingAssistant(String email, String password, int semester) {
        super(email, password, semester); 
        this.role = "TA"; 
    }

    public void viewStudentGrades(Student student) {
        System.out.println("\nGrades for " + student.getEmail() + ":");
        if (student.getGrades().isEmpty()) {
            System.out.println("No grades assigned yet.");
        } else {
            for (java.util.Map.Entry<Course, String> entry : student.getGrades().entrySet()) {
                System.out.println("- " + entry.getKey().getTitle() + ": " + entry.getValue());
            }
        }
    }

    public void assignGrade(Student student, Course course, String grade) {
        student.getGrades().put(course, grade);
        System.out.println("Grade successfully assigned by TA.");
    }
}