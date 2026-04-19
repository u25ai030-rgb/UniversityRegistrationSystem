package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Student extends User {
    private int semester;
    private List<Course> registeredCourses;
    private Map<Course, String> grades;
    private List<Complaint> myComplaints;

    public Student(String email, String password, int semester) {
        super(email, password, "Student");
        this.semester = semester;
        this.registeredCourses = new ArrayList<>();
        this.grades = new HashMap<>();
        this.myComplaints = new ArrayList<>();
    }

    public int getSemester() { return semester; }
    public List<Course> getRegisteredCourses() { return registeredCourses; }
    public Map<Course, String> getGrades() { return grades; }
    public List<Complaint> getMyComplaints() { return myComplaints; }
    
    public void calculateGPA() {
        if (grades.isEmpty()) {
            System.out.println("No grades available to calculate GPA.");
            return;
        }

        double totalPoints = 0;
        int totalCredits = 0;

        for (Map.Entry<Course, String> entry : grades.entrySet()) {
            int credits = entry.getKey().getCredits();
            String grade = entry.getValue();
            int points = 0;

            if (grade.equalsIgnoreCase("A")) points = 10;
            else if (grade.equalsIgnoreCase("B")) points = 8;
            else if (grade.equalsIgnoreCase("C")) points = 6;
            else if (grade.equalsIgnoreCase("D")) points = 4;
            else if (grade.equalsIgnoreCase("F")) points = 0;

            totalPoints += (points * credits);
            totalCredits += credits;
        }

        double gpa = totalPoints / totalCredits;
        System.out.println("Current GPA: " + gpa);
    }
}