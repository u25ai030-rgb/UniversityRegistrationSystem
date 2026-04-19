import models.*;
import java.util.Scanner;

public class Main {
    private static University university = new University();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        initializeData();
        System.out.println("Welcome to the University Course Registration System");

        while (true) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. Enter the Application");
            System.out.println("2. Exit the Application");
            System.out.print("Select an option: ");

            int choice = getIntInput();

            if (choice == 1) {
                appEntryMenu();
            } else if (choice == 2) {
                System.out.println("Exiting... Goodbye!");
                break;
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }

    private static void initializeData() {
        university.courseCatalog.add(new Course("CS101", "Intro to Java", "Dr. Smith", 4, "None", "Mon 10:00 AM", "Room 101"));
        university.courseCatalog.add(new Course("CS102", "Data Structures", "Dr. Jones", 4, "CS101", "Tue 02:00 PM", "Lab A"));
    }

    private static void appEntryMenu() {
        System.out.println("\n1. Login");
        System.out.println("2. Sign Up");
        System.out.print("Select: ");
        int choice = getIntInput();

        if (choice == 1) {
            performLogin();
        } else if (choice == 2) {
            performSignUp();
        }
    }

    private static void performSignUp() {
        System.out.println("\nSign Up as: 1. Student, 2. Professor, 3. Teaching Assistant (TA)");
        int roleChoice = getIntInput();

        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        if (roleChoice == 1) {
            university.students.add(new Student(email, password, 1));
            System.out.println("Student account created!");
        } else if (roleChoice == 2) {
            university.professors.add(new Professor(email, password));
            System.out.println("Professor account created!");
        } else if (roleChoice == 3) {
            university.students.add(new TeachingAssistant(email, password, 1)); 
            System.out.println("TA account created!");
        }
    }

    private static void performLogin() {
        System.out.println("\nLogin as: 1. Student / TA, 2. Professor, 3. Admin");
        int role = getIntInput();

        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        try {
            boolean found = false;
            if (role == 3) {
                if (university.admin.login(email, password)) {
                    found = true;
                    adminInterface();
                }
            } else if (role == 1) {
                for (Student s : university.students) {
                    if (s.login(email, password)) {
                        found = true;
                        if (s instanceof TeachingAssistant) taInterface((TeachingAssistant) s);
                        else studentInterface(s);
                        break;
                    }
                }
            } else if (role == 2) {
                for (Professor p : university.professors) {
                    if (p.login(email, password)) {
                        found = true;
                        professorInterface(p);
                        break;
                    }
                }
            }

            if (!found) {
                // EXCEPTION HANDLING: Throwing custom login error
                throw new InvalidLoginException("Error: Invalid email or password provided.");
            }
        } catch (InvalidLoginException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void studentInterface(Student student) {
        while (true) {
            System.out.println("\n--- STUDENT MODE ---");
            System.out.println("1. View Available Courses\n2. Register for Courses\n3. View Schedule\n4. Track Academic Progress\n5. Drop Course\n6. Submit Complaint\n7. Submit Course Feedback\n8. Logout");
            System.out.print("Select: ");
            int choice = getIntInput();
            if (choice == 8) break;

            if (choice == 1) displayCatalog();
            else if (choice == 2) handleRegistration(student);
            else if (choice == 3) viewStudentSchedule(student);
            else if (choice == 4) { viewGrades(student); student.calculateGPA(); }
            else if (choice == 5) handleDropCourse(student);
            else if (choice == 6) {
                System.out.print("Enter complaint description: ");
                Complaint comp = new Complaint(scanner.nextLine());
                student.getMyComplaints().add(comp);
                university.allComplaints.add(comp);
                System.out.println("Complaint submitted!");
            }
            else if (choice == 7) handleFeedback(student);
        }
    }

    private static void taInterface(TeachingAssistant ta) {
        while (true) {
            System.out.println("\n--- TA MODE (" + ta.getEmail() + ") ---");
            System.out.println("1. View Available Courses\n2. Register for Courses\n3. View Schedule\n4. Track Academic Progress\n5. Drop Course\n6. Submit Complaint\n7. Submit Course Feedback\n8. View Student Grades\n9. Assign Student Grade\n10. Logout");
            System.out.print("Select: ");
            int choice = getIntInput();
            if (choice == 10) break;

            if (choice == 1) displayCatalog();
            else if (choice == 2) handleRegistration(ta);
            else if (choice == 3) viewStudentSchedule(ta);
            else if (choice == 4) { viewGrades(ta); ta.calculateGPA(); }
            else if (choice == 5) handleDropCourse(ta);
            else if (choice == 6) {
                System.out.print("Enter complaint description: ");
                Complaint comp = new Complaint(scanner.nextLine());
                ta.getMyComplaints().add(comp);
                university.allComplaints.add(comp);
                System.out.println("Complaint submitted!");
            }
            else if (choice == 7) handleFeedback(ta);
            else if (choice == 8) {
                System.out.print("Enter Student Email to view grades: ");
                String targetEmail = scanner.nextLine();
                for (Student s : university.students) if (s.getEmail().equalsIgnoreCase(targetEmail)) ta.viewStudentGrades(s);
            }
            else if (choice == 9) {
                System.out.print("Enter Student Email to assign grade: ");
                String targetEmail = scanner.nextLine();
                for (Student target : university.students) {
                    if (target.getEmail().equalsIgnoreCase(targetEmail)) {
                        viewStudentSchedule(target);
                        System.out.print("Enter Course Code: "); String code = scanner.nextLine();
                        System.out.print("Enter Grade: "); String grade = scanner.nextLine();
                        for (Course c : target.getRegisteredCourses()) {
                            if (c.getCourseCode().equalsIgnoreCase(code)) ta.assignGrade(target, c, grade);
                        }
                    }
                }
            }
        }
    }

    private static void handleRegistration(Student student) {
        displayCatalog();
        System.out.print("Select number: ");
        int index = getIntInput() - 1;

        if (index >= 0 && index < university.courseCatalog.size()) {
            Course selected = university.courseCatalog.get(index);
            int currentCredits = 0;
            for (Course c : student.getRegisteredCourses()) currentCredits += c.getCredits();

            try {
                if (currentCredits + selected.getCredits() > 20) {
                    System.out.println("Registration failed: 20 Credit limit exceeded.");
                    return;
                }
                
                // EXCEPTION HANDLING: Try to enroll. Will throw exception if full.
                selected.enrollStudent(); 
                student.getRegisteredCourses().add(selected);
                System.out.println("Successfully registered for " + selected.getTitle());
                
            } catch (CourseFullException e) {
                System.out.println(e.getMessage()); // Catches the crash and prints safely
            }
        } else {
            System.out.println("Invalid selection.");
        }
    }

    private static void handleDropCourse(Student student) {
        System.out.println("\n--- Drop a Course ---");
        viewStudentSchedule(student);
        if (student.getRegisteredCourses().isEmpty()) return;

        System.out.print("Select course number to drop: ");
        int index = getIntInput() - 1;

        if (index >= 0 && index < student.getRegisteredCourses().size()) {
            Course selected = student.getRegisteredCourses().get(index);
            
            try {
                // EXCEPTION HANDLING: Cannot drop if a grade is already assigned
                if (student.getGrades().containsKey(selected)) {
                    throw new DropDeadlinePassedException("Error: Cannot drop " + selected.getTitle() + " because the deadline passed (grade already assigned).");
                }
                
                student.getRegisteredCourses().remove(selected);
                selected.dropStudent();
                System.out.println("Successfully dropped: " + selected.getTitle());
                
            } catch (DropDeadlinePassedException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Invalid selection.");
        }
    }

    // --- Helper methods (Feedback, Admin, Professor, etc. remain unchanged but included for copy-paste) ---

    private static void handleFeedback(Student student) {
        System.out.println("\n--- Submit Feedback ---");
        viewStudentSchedule(student);
        if (student.getRegisteredCourses().isEmpty()) return;
        System.out.print("Select course number to leave feedback for: ");
        int index = getIntInput() - 1;
        if (index >= 0 && index < student.getRegisteredCourses().size()) {
            Course selectedCourse = student.getRegisteredCourses().get(index);
            System.out.println("1. Numeric Rating (1-5)\n2. Text Comment");
            int type = getIntInput();
            if (type == 1) {
                System.out.print("Enter rating: ");
                selectedCourse.addFeedback(new Feedback<Integer>(student.getEmail(), getIntInput()));
                System.out.println("Rating submitted!");
            } else if (type == 2) {
                System.out.print("Enter comment: ");
                selectedCourse.addFeedback(new Feedback<String>(student.getEmail(), scanner.nextLine()));
                System.out.println("Comment submitted!");
            }
        }
    }

    private static void professorInterface(Professor professor) {
        while (true) {
            System.out.println("\n--- PROFESSOR MODE ---\n1. View Enrolled Students\n2. Update Course Details\n3. View Course Feedback\n4. Logout");
            int choice = getIntInput();
            if (choice == 4) break;
            if (choice == 1) {
                for (Student s : university.students) {
                    for (Course c : s.getRegisteredCourses()) System.out.println("Student " + s.getEmail() + " is in " + c.getTitle());
                }
            } else if (choice == 2) {
                displayCatalog();
                System.out.print("Enter Course Code: "); String code = scanner.nextLine();
                for (Course c : university.courseCatalog) {
                    if (c.getCourseCode().equalsIgnoreCase(code)) {
                        System.out.print("New Syllabus: "); c.setSyllabus(scanner.nextLine());
                        System.out.print("New Office Hours: "); c.setOfficeHours(scanner.nextLine());
                    }
                }
            } else if (choice == 3) {
                displayCatalog();
                System.out.print("Enter Course Code: "); String code = scanner.nextLine();
                for (Course c : university.courseCatalog) {
                    if (c.getCourseCode().equalsIgnoreCase(code)) {
                        for (Feedback<?> f : c.getCourseFeedback()) System.out.println("- " + f.toString());
                    }
                }
            }
        }
    }

    private static void adminInterface() {
        while (true) {
            System.out.println("\n--- ADMIN MODE ---\n1. View Catalog\n2. Add Course\n3. Delete Course\n4. Assign Professor\n5. Manage Complaints\n6. Assign Grades\n7. Logout");
            int choice = getIntInput();
            if (choice == 7) break;
            if (choice == 1) displayCatalog();
            else if (choice == 2) {
                System.out.print("Code: "); String code = scanner.nextLine();
                System.out.print("Title: "); String title = scanner.nextLine();
                System.out.print("Credits (2 or 4): "); int credits = getIntInput();
                university.courseCatalog.add(new Course(code, title, "TBD", credits, "None", "TBD", "TBD"));
            } else if (choice == 3) {
                System.out.print("Code to delete: "); String code = scanner.nextLine();
                university.courseCatalog.removeIf(c -> c.getCourseCode().equalsIgnoreCase(code));
            } else if (choice == 4) {
                System.out.print("Professor Email: "); String profEmail = scanner.nextLine();
                System.out.print("Course Code: "); String code = scanner.nextLine();
                for (Professor p : university.professors) {
                    if (p.getEmail().equalsIgnoreCase(profEmail)) {
                        for (Course c : university.courseCatalog) {
                            if (c.getCourseCode().equalsIgnoreCase(code)) { c.setProfessorName(p.getEmail()); p.addCourse(c); }
                        }
                    }
                }
            } else if (choice == 5) {
                for (Complaint c : university.allComplaints) {
                    System.out.println("Desc: " + c.getDescription() + " | Status: " + c.getStatus());
                    if (c.getStatus().equals("Pending")) {
                        System.out.print("Resolve? (y/n): ");
                        if (scanner.nextLine().equalsIgnoreCase("y")) c.setStatus("Resolved");
                    }
                }
            } else if (choice == 6) {
                System.out.print("Student Email: "); String email = scanner.nextLine();
                for (Student s : university.students) {
                    if (s.getEmail().equalsIgnoreCase(email)) {
                        System.out.print("Course Code: "); String code = scanner.nextLine();
                        System.out.print("Grade: "); String grade = scanner.nextLine();
                        for (Course c : s.getRegisteredCourses()) {
                            if (c.getCourseCode().equalsIgnoreCase(code)) s.getGrades().put(c, grade);
                        }
                    }
                }
            }
        }
    }

    private static void displayCatalog() {
        System.out.println("\n--- Course Catalog ---");
        for (int i = 0; i < university.courseCatalog.size(); i++) {
            Course c = university.courseCatalog.get(i);
            System.out.println((i + 1) + ". " + c.getCourseCode() + ": " + c.getTitle() + " (" + c.getCredits() + " Credits)");
        }
    }

    private static void viewStudentSchedule(Student student) {
        System.out.println("\nYour Schedule:");
        for (int i = 0; i < student.getRegisteredCourses().size(); i++) {
            Course c = student.getRegisteredCourses().get(i);
            System.out.println((i + 1) + ". " + c.getTitle() + " at " + c.getTimings());
        }
    }

    private static void viewGrades(Student student) {
        System.out.println("\nYour Grades:");
        for (java.util.Map.Entry<Course, String> entry : student.getGrades().entrySet()) {
            System.out.println("- " + entry.getKey().getTitle() + ": " + entry.getValue());
        }
    }

    private static int getIntInput() {
        int val = scanner.nextInt();
        scanner.nextLine();
        return val;
    }
}