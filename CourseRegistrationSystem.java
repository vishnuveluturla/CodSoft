import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

class Course {
    String courseCode;
    String title;
    String description;
    int capacity;
    String schedule;
    List<Integer> registeredStudents; // List of student IDs registered for this course

    public Course(String courseCode, String title, String description, int capacity, String schedule) {
        this.courseCode = courseCode;
        this.title = title;
        this.description = description;
        this.capacity = capacity;
        this.schedule = schedule;
        this.registeredStudents = new ArrayList<>();
    }

    public String toString() {
        return courseCode + " - " + title + "\nDescription: " + description + "\nCapacity: " + capacity
                + "\nSchedule: " + schedule + "\n";
    }
}

class Student {
    int studentID;
    String name;

    public Student(int studentID, String name) {
        this.studentID = studentID;
        this.name = name;
    }
}

public class CourseRegistrationSystem {
    static List<Course> courseDatabase = new ArrayList<>();
    static List<Student> studentDatabase = new ArrayList<>();
    static Map<String, List<Integer>> courseRegistrations = new HashMap<>(); // Mapping courseCode to list of studentIDs
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        initializeCourses();

        while (true) {
            displayAvailableOptions();

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    displayCourseListing();
                    break;
                case 2:
                    studentRegistration();
                    break;
                case 3:
                    courseRemoval();
                    break;
                case 4:
                    displayStudentsForCourse();
                    break;
                case 5:
                    displayRegisteredStudents();
                    break;
                case 6:
                    System.out.println("Exiting program. Goodbye!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void displayAvailableOptions() {
        System.out.println("\nAvailable Options:");
        System.out.println("1. Display Course Listing");
        System.out.println("2. Student Registration");
        System.out.println("3. Course Removal");
        System.out.println("4. Display Students Registered for a Course");
        System.out.println("5. Display All Registered Students");
        System.out.println("6. Exit");
        System.out.print("Select an option: ");
    }

    private static void initializeCourses() {
        courseDatabase.add(new Course("CS101", "Introduction to Programming", "Fundamentals of programming", 50, "Mon/Wed 10:00 AM - 11:30 AM"));
        courseDatabase.add(new Course("MATH201", "Calculus I", "Limits, derivatives, and integrals", 40, "Tue/Thu 2:00 PM - 3:30 PM"));
        courseDatabase.add(new Course("ENG102", "English Composition", "Writing and grammar skills", 30, "Mon/Fri 1:00 PM - 2:30 PM"));
        courseDatabase.add(new Course("CHEM101", "Introduction to Chemistry", "Basic concepts of chemistry", 35, "Wed/Fri 9:00 AM - 10:30 AM"));
        // Add more courses as needed
    }

    private static void displayCourseListing() {
        System.out.println("\nAvailable Courses:");
        for (Course course : courseDatabase) {
            System.out.println(course);
        }
    }

    private static void studentRegistration() {
        System.out.print("Enter student ID: ");
        int studentID = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        System.out.print("Enter student name: ");
        String studentName = scanner.nextLine();

        Student student = new Student(studentID, studentName);

        // Display available courses
        displayCourseListing();

        System.out.print("Enter the course code to register (or 'exit' to go back): ");
        String courseCode = scanner.nextLine();

        while (!courseCode.equalsIgnoreCase("exit")) {
            Course selectedCourse = findCourse(courseCode);

            if (selectedCourse != null && selectedCourse.capacity > 0) {
                // Register the student for the course
                studentDatabase.add(student);

                // Update course registration information
                List<Integer> registrations = courseRegistrations.getOrDefault(selectedCourse.courseCode, new ArrayList<>());
                registrations.add(student.studentID);
                courseRegistrations.put(selectedCourse.courseCode, registrations);

                selectedCourse.registeredStudents.add(student.studentID);
                selectedCourse.capacity--;

                System.out.println("Registration successful!");
            } else if (selectedCourse != null && selectedCourse.capacity == 0) {
                System.out.println("Sorry, the course is already full. Choose another course.");
            } else {
                System.out.println("Invalid course code. Please enter a valid course code.");
            }

            System.out.print("Enter another course code to register (or 'exit' to go back): ");
            courseCode = scanner.nextLine();
        }
    }

    private static void courseRemoval() {
        System.out.print("Enter student ID for course removal: ");
        int studentID = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        Student student = findStudent(studentID);

        if (student != null) {
            System.out.println("Registered Courses for Student " + studentID + ":");
            for (Course course : courseDatabase) {
                if (course.registeredStudents.contains(studentID)) {
                    System.out.println(course.courseCode);
                }
            }

            System.out.print("Enter the course code to remove (or 'exit' to go back): ");
            String courseCode = scanner.nextLine();

            while (!courseCode.equalsIgnoreCase("exit")) {
                Course selectedCourse = findCourse(courseCode);

                if (selectedCourse != null && selectedCourse.registeredStudents.contains(studentID)) {
                    // Remove the course from the student's registered courses
                    studentDatabase.remove(student);

                    // Update course registration information
                    List<Integer> registrations = courseRegistrations.get(selectedCourse.courseCode);
                    registrations.remove(Integer.valueOf(student.studentID));

                    selectedCourse.registeredStudents.remove(Integer.valueOf(student.studentID));
                    selectedCourse.capacity++;

                    System.out.println("Course removal successful!");
                } else {
                    System.out.println("You are not registered for this course. Please enter a valid course code.");
                }

                System.out.print("Enter another course code to remove (or 'exit' to go back): ");
                courseCode = scanner.nextLine();
            }
        } else {
            System.out.println("Student with ID " + studentID + " not found.");
        }
    }

    private static void displayStudentsForCourse() {
        System.out.print("Enter course code to display registered students: ");
        String courseCode = scanner.nextLine();

        Course selectedCourse = findCourse(courseCode);

        if (selectedCourse != null) {
            System.out.println("Registered Students for Course " + courseCode + ":");
            for (int studentID : selectedCourse.registeredStudents) {
                Student student = findStudent(studentID);
                if (student != null) {
                    System.out.println("Student ID: " + student.studentID + ", Name: " + student.name);
                }
            }
        } else {
            System.out.println("Course with code " + courseCode + " not found.");
        }
    }

    private static void displayRegisteredStudents() {
        System.out.println("\nAll Registered Students:");
        for (Student student : studentDatabase) {
            System.out.println("Student ID: " + student.studentID + ", Name: " + student.name);
        }
    }

    private static Course findCourse(String courseCode) {
        for (Course course : courseDatabase) {
            if (course.courseCode.equalsIgnoreCase(courseCode)) {
                return course;
            }
        }
        return null;
    }

    private static Student findStudent(int studentID) {
        for (Student student : studentDatabase) {
            if (student.studentID == studentID) {
                return student;
            }
        }
        return null;
    }
}
