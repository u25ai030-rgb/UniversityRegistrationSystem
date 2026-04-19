package models;

import java.util.ArrayList;
import java.util.List;

public class Course {
    private String courseCode;
    private String title;
    private String professorName;
    private int credits;
    private String prerequisites;
    private String timings;
    private String location;
    private String syllabus;
    private String officeHours;
    private List<Feedback<?>> courseFeedback;
    
    private int enrollmentLimit;
    private int currentEnrolled;

    public Course(String courseCode, String title, String professorName, int credits, String prerequisites, String timings, String location) {
        this.courseCode = courseCode;
        this.title = title;
        this.professorName = professorName;
        this.credits = credits;
        this.prerequisites = prerequisites;
        this.timings = timings;
        this.location = location;
        this.syllabus = "TBD";
        this.officeHours = "TBD";
        this.courseFeedback = new ArrayList<>();
        this.enrollmentLimit = 50;
        this.currentEnrolled = 0;
    }

    public String getCourseCode() { return courseCode; }
    public String getTitle() { return title; }
    public int getCredits() { return credits; }
    public String getProfessorName() { return professorName; }
    public String getTimings() { return timings; }
    public String getLocation() { return location; }
    public String getPrerequisites() { return prerequisites; }

    public void setSyllabus(String syllabus) { this.syllabus = syllabus; }
    public void setOfficeHours(String officeHours) { this.officeHours = officeHours; }
    public void setProfessorName(String professorName) { this.professorName = professorName; }

    public void addFeedback(Feedback<?> feedback) { this.courseFeedback.add(feedback); }
    public List<Feedback<?>> getCourseFeedback() { return courseFeedback; }

    public void enrollStudent() throws CourseFullException {
        if (currentEnrolled >= enrollmentLimit) {
            throw new CourseFullException("Registration failed: " + title + " is completely full.");
        }
        currentEnrolled++;
    }
    
    public void dropStudent() {
        if (currentEnrolled > 0) currentEnrolled--;
    }
}