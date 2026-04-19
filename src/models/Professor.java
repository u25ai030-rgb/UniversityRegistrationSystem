package models;

import java.util.ArrayList;
import java.util.List;

public class Professor extends User {
    private List<Course> managedCourses;

    public Professor(String email, String password) {
        super(email, password, "Professor");
        this.managedCourses = new ArrayList<>();
    }

    public List<Course> getManagedCourses() { return managedCourses; }
    public void addCourse(Course course) { this.managedCourses.add(course); }
}