package models;

import java.util.ArrayList;
import java.util.List;

public class University {
    public List<Course> courseCatalog;
    public List<Student> students;
    public List<Professor> professors;
    public Administrator admin;
    public List<Complaint> allComplaints;

    public University() {
        this.courseCatalog = new ArrayList<>();
        this.students = new ArrayList<>();
        this.professors = new ArrayList<>();
        this.admin = new Administrator("admin@university.com", "admin123");
        this.allComplaints = new ArrayList<>();

        
    }
}