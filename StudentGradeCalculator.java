import java.util.Scanner;

public class StudentGradeCalculator {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Input: Take marks obtained in each subject
        System.out.println("Enter marks obtained in each subject (out of 100):");
        int subjects = 0;
        int totalMarks = 0;

        while (true) {
            System.out.print("Enter marks for subject " + (subjects + 1) + " (or -1 to stop): ");
            int marks = scanner.nextInt();

            if (marks == -1) {
                break;
            }

            if (marks < 0 || marks > 100) {
                System.out.println("Invalid input. Marks should be between 0 and 100.");
                continue;
            }

            totalMarks += marks;
            subjects++;
        }

        // Calculate Total Marks
        double averagePercentage = 0.0;

        if (subjects > 0) {
            // Calculate Average Percentage
            averagePercentage = (double) totalMarks / subjects;

            // Grade Calculation
            char grade = calculateGrade(averagePercentage);

            // Display Results
            System.out.println("\nResults:");
            System.out.println("Total Marks: " + totalMarks);
            System.out.println("Average Percentage: " + averagePercentage + "%");
            System.out.println("Grade: " + grade);
        } else {
            System.out.println("No valid subjects entered. Exiting program.");
        }

        scanner.close();
    }

    // Grade Calculation Method
    private static char calculateGrade(double averagePercentage) {
        char grade;

        if (averagePercentage >= 90) {
            grade = 'A';
        } else if (averagePercentage >= 80) {
            grade = 'B';
        } else if (averagePercentage >= 70) {
            grade = 'C';
        } else if (averagePercentage >= 60) {
            grade = 'D';
        } else {
            grade = 'F';
        }

        return grade;
    }
}
