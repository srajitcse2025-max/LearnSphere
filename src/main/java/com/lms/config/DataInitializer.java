package com.lms.config;

import com.lms.model.*;
import com.lms.repository.*;
import com.lms.service.AcademicService;
import com.lms.service.QuizService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepo;
    private final StudentRepository studentRepo;
    private final FacultyRepository facultyRepo;
    private final CourseRepository courseRepo;
    private final VideoRepository videoRepo;
    private final EnrollmentRepository enrollRepo;
    private final SubjectRepository subjectRepo;
    private final QuizRepository quizRepo;
    private final QuestionRepository questionRepo;
    private final PasswordEncoder encoder;
    private final AcademicService academicService;
    private final QuizService quizService;

    public DataInitializer(UserRepository userRepo, StudentRepository studentRepo, FacultyRepository facultyRepo,
                           CourseRepository courseRepo, VideoRepository videoRepo, EnrollmentRepository enrollRepo,
                           SubjectRepository subjectRepo, QuizRepository quizRepo,
                           QuestionRepository questionRepo, PasswordEncoder encoder,
                           AcademicService academicService, QuizService quizService) {
        this.userRepo = userRepo; this.studentRepo = studentRepo; this.facultyRepo = facultyRepo;
        this.courseRepo = courseRepo; this.videoRepo = videoRepo; this.enrollRepo = enrollRepo;
        this.subjectRepo = subjectRepo; this.quizRepo = quizRepo;
        this.questionRepo = questionRepo; this.encoder = encoder;
        this.academicService = academicService; this.quizService = quizService;
    }

    @Override
    public void run(String... args) {
        if (userRepo.count() > 0) return;

        // ======================== ADMIN ========================
        User admin = makeUser("Admin", "admin@lms.com", "admin123", "ROLE_ADMIN");

        // ======================== FACULTY ========================
        User fUser1 = makeUser("Dr. Ramesh Kumar", "faculty@lms.com", "faculty123", "ROLE_FACULTY");
        Faculty faculty1 = makeFaculty(fUser1, "FAC001", "CSE", "Associate Professor", "Data Structures & Algorithms");

        User fUser2 = makeUser("Dr. Priya Sharma", "priya@lms.com", "faculty123", "ROLE_FACULTY");
        Faculty faculty2 = makeFaculty(fUser2, "FAC002", "CSE", "Assistant Professor", "Database Systems");

        User fUser3 = makeUser("Prof. Arun Nair", "arun@lms.com", "faculty123", "ROLE_FACULTY");
        Faculty faculty3 = makeFaculty(fUser3, "FAC003", "CSE", "Professor & HOD", "Machine Learning");

        // ======================== STUDENTS ========================
        User sUser1 = makeUser("Rajit S", "student@lms.com", "student123", "ROLE_STUDENT");
        Student student1 = makeStudent(sUser1, "21CSE001", "CSE", "A", "Chennai Institute of Technology", 3);

        User sUser2 = makeUser("Ananya Verma", "ananya@lms.com", "student123", "ROLE_STUDENT");
        Student student2 = makeStudent(sUser2, "21CSE002", "CSE", "A", "Chennai Institute of Technology", 3);

        User sUser3 = makeUser("Karthik Raja", "karthik@lms.com", "student123", "ROLE_STUDENT");
        Student student3 = makeStudent(sUser3, "21CSE003", "CSE", "B", "Chennai Institute of Technology", 3);

        User sUser4 = makeUser("Divya Lakshmi", "divya@lms.com", "student123", "ROLE_STUDENT");
        Student student4 = makeStudent(sUser4, "21CSE004", "CSE", "A", "Chennai Institute of Technology", 3);

        // ======================== COURSES ========================
        Course c1 = makeCourse("Data Structures & Algorithms",
                "Master arrays, linked lists, stacks, queues, trees, graphs, sorting and searching algorithms. Includes time & space complexity analysis with real-world problem solving.",
                "CS201", "CSE", faculty1);

        Course c2 = makeCourse("Database Management Systems",
                "Learn SQL, ER modeling, normalization (1NF-BCNF), transactions, concurrency control, indexing, and NoSQL concepts. Hands-on with MySQL and MongoDB.",
                "CS301", "CSE", faculty2);

        Course c3 = makeCourse("Object Oriented Programming with Java",
                "Comprehensive Java programming covering OOP principles, inheritance, polymorphism, abstraction, interfaces, exception handling, collections framework, and design patterns.",
                "CS202", "CSE", faculty1);

        Course c4 = makeCourse("Operating Systems",
                "Process management, CPU scheduling, memory management, virtual memory, file systems, deadlocks, and inter-process communication. Includes Linux kernel internals.",
                "CS303", "CSE", faculty3);

        Course c5 = makeCourse("Machine Learning Fundamentals",
                "Introduction to supervised & unsupervised learning, linear regression, decision trees, SVM, neural networks, clustering, and model evaluation with Python & scikit-learn.",
                "CS401", "CSE", faculty3);

        // ======================== VIDEOS ========================
        // DSA Videos
        saveVideo(c1, "Introduction to Data Structures", "Overview of data structures and why they matter", "https://www.youtube.com/embed/QJNwK2uJyGs", 1);
        saveVideo(c1, "Arrays - Operations & Complexity", "Insert, delete, search operations with Big-O analysis", "https://www.youtube.com/embed/QJNwK2uJyGs", 2);
        saveVideo(c1, "Linked Lists - Singly & Doubly", "Implementation and operations on linked lists", "https://www.youtube.com/embed/R9PTBwOzceo", 3);
        saveVideo(c1, "Stacks and Queues", "LIFO, FIFO principles with real-world applications", "https://www.youtube.com/embed/wjI1WNcIntg", 4);
        saveVideo(c1, "Binary Trees & BST", "Tree traversals - Inorder, Preorder, Postorder", "https://www.youtube.com/embed/oSWTXtMglKE", 5);
        saveVideo(c1, "Graph Algorithms - BFS & DFS", "Breadth-first and depth-first search explained", "https://www.youtube.com/embed/tWVWeAqZ0WU", 6);
        saveVideo(c1, "Sorting Algorithms Comparison", "Bubble, Selection, Merge, Quick sort with analysis", "https://www.youtube.com/embed/kPRA0W1kECg", 7);

        // DBMS Videos
        saveVideo(c2, "Introduction to DBMS", "What is a database? DBMS vs File System", "https://www.youtube.com/embed/HXV3zeQKqGY", 1);
        saveVideo(c2, "ER Diagrams & Modeling", "Entity-Relationship diagrams for database design", "https://www.youtube.com/embed/QpdhBUYk7Kk", 2);
        saveVideo(c2, "SQL Fundamentals", "SELECT, INSERT, UPDATE, DELETE with examples", "https://www.youtube.com/embed/HXV3zeQKqGY", 3);
        saveVideo(c2, "Joins in SQL", "INNER, LEFT, RIGHT, FULL joins explained", "https://www.youtube.com/embed/9yeOJ0ZMUYw", 4);
        saveVideo(c2, "Normalization - 1NF to BCNF", "Database normalization with examples", "https://www.youtube.com/embed/UrYLYV7WSHM", 5);
        saveVideo(c2, "Transactions & ACID Properties", "Atomicity, Consistency, Isolation, Durability", "https://www.youtube.com/embed/eYQwKi7P8MM", 6);

        // OOP Java Videos
        saveVideo(c3, "Java Introduction & Setup", "JDK installation, first Java program", "https://www.youtube.com/embed/eIrMbAQSU34", 1);
        saveVideo(c3, "Classes, Objects & Constructors", "OOP basics in Java with examples", "https://www.youtube.com/embed/IUqKuGNasdM", 2);
        saveVideo(c3, "Inheritance & Polymorphism", "Method overriding, super keyword, dynamic dispatch", "https://www.youtube.com/embed/Zs342ePFvRI", 3);
        saveVideo(c3, "Abstract Classes & Interfaces", "Abstraction in Java with real-world examples", "https://www.youtube.com/embed/rgHZa7-Dibg", 4);
        saveVideo(c3, "Exception Handling", "try-catch, throw, throws, custom exceptions", "https://www.youtube.com/embed/1XAfapkBQjk", 5);

        // OS Videos
        saveVideo(c4, "Introduction to Operating Systems", "What is an OS? Types and functions", "https://www.youtube.com/embed/26QPDBe-NB8", 1);
        saveVideo(c4, "Process Management", "Process states, PCB, context switching", "https://www.youtube.com/embed/OrM7nZcxXZU", 2);
        saveVideo(c4, "CPU Scheduling Algorithms", "FCFS, SJF, Priority, Round Robin", "https://www.youtube.com/embed/Jkmy2YLUbUY", 3);
        saveVideo(c4, "Deadlocks", "Conditions, prevention, avoidance, detection", "https://www.youtube.com/embed/UVo9mGARkhQ", 4);

        // ML Videos
        saveVideo(c5, "What is Machine Learning?", "Types of ML - supervised, unsupervised, reinforcement", "https://www.youtube.com/embed/ukzFI9rgwfU", 1);
        saveVideo(c5, "Linear Regression", "Simple and multiple linear regression with Python", "https://www.youtube.com/embed/nk2CQITm_eo", 2);
        saveVideo(c5, "Decision Trees & Random Forests", "Classification and regression trees", "https://www.youtube.com/embed/7VeUPuFGJHk", 3);

        // ======================== ENROLLMENTS ========================
        // Student 1 - enrolled in all 5 courses
        makeEnrollment(student1, c1, 75); makeEnrollment(student1, c2, 60);
        makeEnrollment(student1, c3, 85); makeEnrollment(student1, c4, 40);
        makeEnrollment(student1, c5, 20);

        // Student 2 - enrolled in 3 courses
        makeEnrollment(student2, c1, 50); makeEnrollment(student2, c2, 70);
        makeEnrollment(student2, c3, 30);

        // Student 3 - enrolled in 4 courses
        makeEnrollment(student3, c1, 90); makeEnrollment(student3, c2, 45);
        makeEnrollment(student3, c4, 60); makeEnrollment(student3, c5, 55);

        // Student 4 - enrolled in 3 courses
        makeEnrollment(student4, c1, 65); makeEnrollment(student4, c3, 80);
        makeEnrollment(student4, c4, 35);

        // ======================== QUIZZES ========================
        // Quiz 1 - DSA Fundamentals
        Quiz quiz1 = makeQuiz(c1, "DSA Fundamentals Quiz", "Arrays & Linked Lists", 15, 10);
        saveQuestion(quiz1, "What is the time complexity of accessing an element in an array by index?", "O(1)", "O(n)", "O(log n)", "O(n²)", "A", "Array access by index is constant time O(1) as it uses direct memory addressing.");
        saveQuestion(quiz1, "Which data structure uses LIFO (Last In First Out) principle?", "Queue", "Stack", "Array", "Linked List", "B", "Stack follows Last In First Out - the most recently added element is removed first.");
        saveQuestion(quiz1, "What is the worst-case time complexity of insertion at the beginning of a singly linked list?", "O(n)", "O(n²)", "O(1)", "O(log n)", "C", "Insertion at beginning is O(1) - just create a new node and update the head pointer.");
        saveQuestion(quiz1, "Which traversal of a binary tree visits the root node first?", "Inorder", "Postorder", "Preorder", "Level Order", "C", "Preorder traversal visits nodes in Root → Left → Right order.");
        saveQuestion(quiz1, "What is the space complexity of an adjacency matrix for a graph with V vertices?", "O(V)", "O(V²)", "O(E)", "O(V+E)", "B", "Adjacency matrix requires a V × V 2D array, hence O(V²) space.");
        saveQuestion(quiz1, "Which sorting algorithm has the best average-case time complexity?", "Bubble Sort - O(n²)", "Merge Sort - O(n log n)", "Selection Sort - O(n²)", "Insertion Sort - O(n²)", "B", "Merge Sort consistently achieves O(n log n) in all cases.");
        saveQuestion(quiz1, "In a doubly linked list, each node contains how many pointers?", "1", "2", "3", "0", "B", "Each node in a doubly linked list has two pointers: one to the next node and one to the previous node.");
        saveQuestion(quiz1, "What data structure is used for BFS (Breadth-First Search)?", "Stack", "Queue", "Priority Queue", "Array", "B", "BFS uses a Queue to process nodes level by level.");
        saveQuestion(quiz1, "What is the maximum number of nodes at level 'k' in a binary tree?", "k", "2k", "2^k", "2^(k+1)", "C", "At level k, a binary tree can have at most 2^k nodes (root is level 0).");
        saveQuestion(quiz1, "Which data structure is best for implementing undo functionality?", "Queue", "Stack", "Array", "Linked List", "B", "Stack's LIFO property makes it ideal for undo - the last action is undone first.");

        // Quiz 2 - DBMS
        Quiz quiz2 = makeQuiz(c2, "SQL & Normalization Quiz", "SQL and Database Design", 20, 8);
        saveQuestion(quiz2, "Which SQL command is used to retrieve data from a database?", "GET", "SELECT", "FETCH", "RETRIEVE", "B", "SELECT is the standard SQL command for querying and retrieving data from tables.");
        saveQuestion(quiz2, "What does ACID stand for in database transactions?", "Atomicity, Consistency, Isolation, Durability", "Addition, Consistency, Isolation, Data", "Atomicity, Correctness, Isolation, Durability", "Atomicity, Consistency, Integration, Durability", "A", "ACID ensures reliable database transactions: Atomicity, Consistency, Isolation, Durability.");
        saveQuestion(quiz2, "Which normal form eliminates transitive dependencies?", "1NF", "2NF", "3NF", "BCNF", "C", "Third Normal Form (3NF) eliminates transitive dependencies - non-key attributes must depend only on the primary key.");
        saveQuestion(quiz2, "What type of JOIN returns all rows from the left table?", "INNER JOIN", "LEFT JOIN", "RIGHT JOIN", "CROSS JOIN", "B", "LEFT JOIN returns all rows from the left table and matched rows from the right table; unmatched rows get NULL.");
        saveQuestion(quiz2, "Which SQL clause is used to filter grouped results?", "WHERE", "HAVING", "GROUP BY", "ORDER BY", "B", "HAVING filters groups after GROUP BY, while WHERE filters individual rows before grouping.");
        saveQuestion(quiz2, "What is a foreign key?", "Primary key of another table", "A key that references primary key of another table", "A unique identifier", "An auto-increment field", "B", "A foreign key is a column that references the primary key of another table, establishing a relationship.");
        saveQuestion(quiz2, "Which command is used to remove all rows from a table without logging individual row deletions?", "DELETE", "DROP", "TRUNCATE", "REMOVE", "C", "TRUNCATE removes all rows quickly without logging individual deletions, but keeps the table structure.");
        saveQuestion(quiz2, "What is the purpose of an INDEX in a database?", "Store data", "Improve query performance", "Create backups", "Encrypt data", "B", "Indexes improve query performance by creating a fast lookup structure, similar to a book's index.");

        // Quiz 3 - OOP Java
        Quiz quiz3 = makeQuiz(c3, "Java OOP Concepts Quiz", "OOP Principles", 15, 6);
        saveQuestion(quiz3, "Which OOP principle allows a class to inherit properties from another class?", "Encapsulation", "Polymorphism", "Inheritance", "Abstraction", "C", "Inheritance allows a child class to acquire properties and methods of a parent class using 'extends'.");
        saveQuestion(quiz3, "What keyword is used to prevent a class from being inherited in Java?", "static", "final", "abstract", "private", "B", "The 'final' keyword on a class prevents other classes from extending it.");
        saveQuestion(quiz3, "Which access modifier makes a member accessible only within the same class?", "public", "protected", "default", "private", "D", "The 'private' access modifier restricts access to only within the declaring class.");
        saveQuestion(quiz3, "What is method overriding?", "Same method name with different parameters in same class", "Redefining a parent class method in a child class", "Calling a method multiple times", "Creating multiple constructors", "B", "Method overriding is when a subclass provides a specific implementation of a method defined in its superclass.");
        saveQuestion(quiz3, "Which keyword is used to refer to the current object in Java?", "self", "this", "current", "me", "B", "The 'this' keyword refers to the current instance of the class.");
        saveQuestion(quiz3, "What is an interface in Java?", "A class with all private methods", "A blueprint with abstract methods that a class must implement", "A type of variable", "A design pattern", "B", "An interface defines a contract of abstract methods that implementing classes must provide.");

        // Quiz 4 - OS
        Quiz quiz4 = makeQuiz(c4, "Operating Systems Quiz", "Process & Memory Management", 20, 5);
        saveQuestion(quiz4, "What is a process in an operating system?", "A program stored on disk", "A program in execution", "A system file", "A hardware component", "B", "A process is a program that is currently being executed, including its code, data, and system resources.");
        saveQuestion(quiz4, "Which CPU scheduling algorithm can cause starvation?", "Round Robin", "FCFS", "Priority Scheduling", "SJF", "C", "Priority Scheduling can cause starvation where low-priority processes never get CPU time.");
        saveQuestion(quiz4, "What is a deadlock?", "A crashed process", "Two or more processes waiting indefinitely for each other's resources", "A memory leak", "A buffer overflow", "B", "Deadlock occurs when processes hold resources and wait for resources held by others, creating a circular wait.");
        saveQuestion(quiz4, "Which page replacement algorithm is known as the 'optimal' algorithm?", "FIFO", "LRU", "OPT (Belady's)", "Clock", "C", "OPT replaces the page that won't be used for the longest time in the future - optimal but impractical.");
        saveQuestion(quiz4, "What is virtual memory?", "RAM", "A technique using disk space as extended memory", "Cache memory", "ROM", "B", "Virtual memory uses disk space to simulate additional RAM, allowing programs larger than physical memory to run.");

        // ======================== QUIZ ATTEMPTS (Pre-completed) ========================
        // Student 1 attempts
        Map<Long, String> s1q1Answers = new HashMap<>();
        List<Question> q1Questions = questionRepo.findByQuiz(quiz1);
        s1q1Answers.put(q1Questions.get(0).getId(), "A"); // correct
        s1q1Answers.put(q1Questions.get(1).getId(), "B"); // correct
        s1q1Answers.put(q1Questions.get(2).getId(), "C"); // correct
        s1q1Answers.put(q1Questions.get(3).getId(), "C"); // correct
        s1q1Answers.put(q1Questions.get(4).getId(), "B"); // correct
        s1q1Answers.put(q1Questions.get(5).getId(), "B"); // correct
        s1q1Answers.put(q1Questions.get(6).getId(), "B"); // correct
        s1q1Answers.put(q1Questions.get(7).getId(), "A"); // wrong (correct: B)
        s1q1Answers.put(q1Questions.get(8).getId(), "C"); // correct
        s1q1Answers.put(q1Questions.get(9).getId(), "B"); // correct
        quizService.submitQuiz(student1, quiz1, s1q1Answers); // 9/10 = 90%

        Map<Long, String> s1q2Answers = new HashMap<>();
        List<Question> q2Questions = questionRepo.findByQuiz(quiz2);
        s1q2Answers.put(q2Questions.get(0).getId(), "B"); // correct
        s1q2Answers.put(q2Questions.get(1).getId(), "A"); // correct
        s1q2Answers.put(q2Questions.get(2).getId(), "C"); // correct
        s1q2Answers.put(q2Questions.get(3).getId(), "B"); // correct
        s1q2Answers.put(q2Questions.get(4).getId(), "B"); // correct
        s1q2Answers.put(q2Questions.get(5).getId(), "A"); // wrong (correct: B)
        s1q2Answers.put(q2Questions.get(6).getId(), "C"); // correct
        s1q2Answers.put(q2Questions.get(7).getId(), "B"); // correct
        quizService.submitQuiz(student1, quiz2, s1q2Answers); // 7/8 = 87.5%

        Map<Long, String> s1q3Answers = new HashMap<>();
        List<Question> q3Questions = questionRepo.findByQuiz(quiz3);
        s1q3Answers.put(q3Questions.get(0).getId(), "C"); // correct
        s1q3Answers.put(q3Questions.get(1).getId(), "B"); // correct
        s1q3Answers.put(q3Questions.get(2).getId(), "D"); // correct
        s1q3Answers.put(q3Questions.get(3).getId(), "B"); // correct
        s1q3Answers.put(q3Questions.get(4).getId(), "A"); // wrong (correct: B)
        s1q3Answers.put(q3Questions.get(5).getId(), "B"); // correct
        quizService.submitQuiz(student1, quiz3, s1q3Answers); // 5/6 = 83.3%

        // Student 2 attempts
        Map<Long, String> s2q1Answers = new HashMap<>();
        s2q1Answers.put(q1Questions.get(0).getId(), "A");
        s2q1Answers.put(q1Questions.get(1).getId(), "B");
        s2q1Answers.put(q1Questions.get(2).getId(), "A"); // wrong
        s2q1Answers.put(q1Questions.get(3).getId(), "C");
        s2q1Answers.put(q1Questions.get(4).getId(), "A"); // wrong
        s2q1Answers.put(q1Questions.get(5).getId(), "B");
        s2q1Answers.put(q1Questions.get(6).getId(), "B");
        s2q1Answers.put(q1Questions.get(7).getId(), "B");
        s2q1Answers.put(q1Questions.get(8).getId(), "C");
        s2q1Answers.put(q1Questions.get(9).getId(), "A"); // wrong
        quizService.submitQuiz(student2, quiz1, s2q1Answers); // 7/10 = 70%

        // Student 3 attempts
        Map<Long, String> s3q1Answers = new HashMap<>();
        s3q1Answers.put(q1Questions.get(0).getId(), "A");
        s3q1Answers.put(q1Questions.get(1).getId(), "B");
        s3q1Answers.put(q1Questions.get(2).getId(), "C");
        s3q1Answers.put(q1Questions.get(3).getId(), "C");
        s3q1Answers.put(q1Questions.get(4).getId(), "B");
        s3q1Answers.put(q1Questions.get(5).getId(), "B");
        s3q1Answers.put(q1Questions.get(6).getId(), "B");
        s3q1Answers.put(q1Questions.get(7).getId(), "B");
        s3q1Answers.put(q1Questions.get(8).getId(), "C");
        s3q1Answers.put(q1Questions.get(9).getId(), "B");
        quizService.submitQuiz(student3, quiz1, s3q1Answers); // 10/10 = 100%

        // ======================== SUBJECTS ========================
        // Semester 1
        Subject s1sub1 = saveSub("MA101", "Engineering Mathematics I", 1, "CSE", 4);
        Subject s1sub2 = saveSub("PH101", "Engineering Physics", 1, "CSE", 3);
        Subject s1sub3 = saveSub("CS101", "Programming in C", 1, "CSE", 4);
        Subject s1sub4 = saveSub("EE101", "Basic Electrical Engineering", 1, "CSE", 3);
        Subject s1sub5 = saveSub("EN101", "Technical English", 1, "CSE", 2);

        // Semester 2
        Subject s2sub1 = saveSub("MA102", "Engineering Mathematics II", 2, "CSE", 4);
        Subject s2sub2 = saveSub("CH101", "Engineering Chemistry", 2, "CSE", 3);
        Subject s2sub3 = saveSub("CS102", "Python Programming", 2, "CSE", 4);
        Subject s2sub4 = saveSub("ME101", "Engineering Graphics", 2, "CSE", 3);
        Subject s2sub5 = saveSub("HS101", "Communication Skills", 2, "CSE", 2);

        // Semester 3 (current)
        Subject s3sub1 = saveSub("MA201", "Discrete Mathematics", 3, "CSE", 4);
        Subject s3sub2 = saveSub("CS201", "Data Structures & Algorithms", 3, "CSE", 4);
        Subject s3sub3 = saveSub("CS202", "Object Oriented Programming", 3, "CSE", 4);
        Subject s3sub4 = saveSub("CS203", "Computer Organization", 3, "CSE", 3);
        Subject s3sub5 = saveSub("CS204", "Operating Systems", 3, "CSE", 3);

        // ======================== ACADEMIC RESULTS ========================
        // === Student 1 (Rajit S) - Good performer ===
        // Semester 1 (GPA ~8.5)
        saveResult(student1, s1sub1, 35, 50, 1); // 85 - A+
        saveResult(student1, s1sub2, 32, 48, 1); // 80 - A+
        saveResult(student1, s1sub3, 38, 55, 1); // 93 - O
        saveResult(student1, s1sub4, 30, 45, 1); // 75 - A
        saveResult(student1, s1sub5, 36, 52, 1); // 88 - A+

        // Semester 2 (GPA ~8.8)
        saveResult(student1, s2sub1, 36, 52, 2); // 88 - A+
        saveResult(student1, s2sub2, 34, 50, 2); // 84 - A+
        saveResult(student1, s2sub3, 39, 56, 2); // 95 - O
        saveResult(student1, s2sub4, 33, 47, 2); // 80 - A+
        saveResult(student1, s2sub5, 38, 54, 2); // 92 - O

        // Semester 3 (GPA ~8.2)
        saveResult(student1, s3sub1, 33, 48, 3); // 81 - A+
        saveResult(student1, s3sub2, 37, 53, 3); // 90 - O
        saveResult(student1, s3sub3, 35, 50, 3); // 85 - A+
        saveResult(student1, s3sub4, 28, 42, 3); // 70 - A
        saveResult(student1, s3sub5, 32, 46, 3); // 78 - A

        // === Student 2 (Ananya) - Average performer ===
        // Semester 1
        saveResult(student2, s1sub1, 28, 40, 1); // 68 - B+
        saveResult(student2, s1sub2, 30, 42, 1); // 72 - A
        saveResult(student2, s1sub3, 32, 48, 1); // 80 - A+
        saveResult(student2, s1sub4, 25, 38, 1); // 63 - B+
        saveResult(student2, s1sub5, 34, 50, 1); // 84 - A+
        // Semester 2
        saveResult(student2, s2sub1, 30, 44, 2); // 74 - A
        saveResult(student2, s2sub2, 28, 40, 2); // 68 - B+
        saveResult(student2, s2sub3, 35, 52, 2); // 87 - A+
        saveResult(student2, s2sub4, 27, 38, 2); // 65 - B+
        saveResult(student2, s2sub5, 33, 48, 2); // 81 - A+
        // Semester 3
        saveResult(student2, s3sub1, 29, 41, 3); // 70 - A
        saveResult(student2, s3sub2, 34, 50, 3); // 84 - A+
        saveResult(student2, s3sub3, 30, 44, 3); // 74 - A
        saveResult(student2, s3sub4, 26, 36, 3); // 62 - B+
        saveResult(student2, s3sub5, 31, 45, 3); // 76 - A

        // === Student 3 (Karthik) - Top performer ===
        // Semester 1
        saveResult(student3, s1sub1, 38, 55, 1); // 93 - O
        saveResult(student3, s1sub2, 36, 54, 1); // 90 - O
        saveResult(student3, s1sub3, 40, 58, 1); // 98 - O
        saveResult(student3, s1sub4, 35, 52, 1); // 87 - A+
        saveResult(student3, s1sub5, 37, 55, 1); // 92 - O
        // Semester 2
        saveResult(student3, s2sub1, 39, 56, 2); // 95 - O
        saveResult(student3, s2sub2, 37, 54, 2); // 91 - O
        saveResult(student3, s2sub3, 40, 58, 2); // 98 - O
        saveResult(student3, s2sub4, 36, 52, 2); // 88 - A+
        saveResult(student3, s2sub5, 38, 56, 2); // 94 - O
        // Semester 3
        saveResult(student3, s3sub1, 37, 54, 3); // 91 - O
        saveResult(student3, s3sub2, 39, 57, 3); // 96 - O
        saveResult(student3, s3sub3, 38, 55, 3); // 93 - O
        saveResult(student3, s3sub4, 35, 50, 3); // 85 - A+
        saveResult(student3, s3sub5, 36, 53, 3); // 89 - A+

        // === Student 4 (Divya) - Needs improvement ===
        // Semester 1
        saveResult(student4, s1sub1, 22, 30, 1); // 52 - B
        saveResult(student4, s1sub2, 25, 35, 1); // 60 - B+
        saveResult(student4, s1sub3, 28, 40, 1); // 68 - B+
        saveResult(student4, s1sub4, 18, 25, 1); // 43 - C
        saveResult(student4, s1sub5, 30, 42, 1); // 72 - A
        // Semester 2
        saveResult(student4, s2sub1, 24, 32, 2); // 56 - B
        saveResult(student4, s2sub2, 26, 38, 2); // 64 - B+
        saveResult(student4, s2sub3, 30, 44, 2); // 74 - A
        saveResult(student4, s2sub4, 20, 28, 2); // 48 - C
        saveResult(student4, s2sub5, 28, 40, 2); // 68 - B+
        // Semester 3
        saveResult(student4, s3sub1, 25, 35, 3); // 60 - B+
        saveResult(student4, s3sub2, 29, 42, 3); // 71 - A
        saveResult(student4, s3sub3, 27, 38, 3); // 65 - B+
        saveResult(student4, s3sub4, 20, 30, 3); // 50 - B
        saveResult(student4, s3sub5, 23, 33, 3); // 56 - B

        System.out.println("========================================");
        System.out.println("  LearnSphere - Demo Data Loaded!");
        System.out.println("========================================");
        System.out.println("  Students: 4 | Faculty: 3 | Courses: 5");
        System.out.println("  Videos: 27 | Quizzes: 4 | Questions: 29");
        System.out.println("  Quiz Attempts: 5 | Academic Results: 60");
        System.out.println("========================================");
        System.out.println("  Login Credentials:");
        System.out.println("  Student:  student@lms.com / student123");
        System.out.println("  Faculty:  faculty@lms.com / faculty123");
        System.out.println("  Admin:    admin@lms.com   / admin123");
        System.out.println("========================================");
    }

    // ======================== HELPER METHODS ========================
    private User makeUser(String name, String email, String pass, String role) {
        User u = new User(); u.setFullName(name); u.setEmail(email);
        u.setPassword(encoder.encode(pass)); u.setRole(role); u.setActive(true);
        return userRepo.save(u);
    }

    private Faculty makeFaculty(User user, String empId, String dept, String desig, String spec) {
        Faculty f = new Faculty(); f.setUser(user); f.setEmployeeId(empId);
        f.setDepartment(dept); f.setDesignation(desig); f.setSpecialization(spec);
        return facultyRepo.save(f);
    }

    private Student makeStudent(User user, String regNo, String dept, String sec, String college, int sem) {
        Student s = new Student(); s.setUser(user); s.setRegisterNumber(regNo);
        s.setDepartment(dept); s.setSection(sec); s.setCollege(college); s.setSemester(sem);
        return studentRepo.save(s);
    }

    private Course makeCourse(String title, String desc, String code, String dept, Faculty faculty) {
        Course c = new Course(); c.setTitle(title); c.setDescription(desc);
        c.setCourseCode(code); c.setDepartment(dept); c.setFaculty(faculty);
        return courseRepo.save(c);
    }

    private void saveVideo(Course c, String title, String desc, String url, int order) {
        Video v = new Video(); v.setCourse(c); v.setTitle(title); v.setDescription(desc);
        v.setYoutubeUrl(url); v.setOrderIndex(order); videoRepo.save(v);
    }

    private void makeEnrollment(Student s, Course c, double progress) {
        Enrollment e = new Enrollment(); e.setStudent(s); e.setCourse(c);
        e.setProgress(progress); e.setCompleted(progress >= 100);
        enrollRepo.save(e);
    }

    private Quiz makeQuiz(Course c, String title, String topic, int duration, int marks) {
        Quiz q = new Quiz(); q.setCourse(c); q.setTitle(title);
        q.setTopic(topic); q.setDurationMinutes(duration); q.setTotalMarks(marks);
        return quizRepo.save(q);
    }

    private void saveQuestion(Quiz quiz, String text, String a, String b, String c, String d, String correct, String expl) {
        Question q = new Question(); q.setQuiz(quiz); q.setQuestionText(text);
        q.setOptionA(a); q.setOptionB(b); q.setOptionC(c); q.setOptionD(d);
        q.setCorrectAnswer(correct); q.setExplanation(expl); questionRepo.save(q);
    }

    private Subject saveSub(String code, String name, int sem, String dept, int credits) {
        Subject s = new Subject(); s.setSubjectCode(code); s.setSubjectName(name);
        s.setSemester(sem); s.setDepartment(dept); s.setCredits(credits);
        return subjectRepo.save(s);
    }

    private void saveResult(Student student, Subject subject, int internal, int external, int semester) {
        AcademicResult r = new AcademicResult();
        r.setStudent(student); r.setSubject(subject);
        r.setInternalMarks(internal); r.setExternalMarks(external); r.setSemester(semester);
        academicService.saveResult(r);
    }
}
