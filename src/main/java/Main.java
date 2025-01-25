package com.mycompany;

import com.mycompany.dao.*;
import com.mycompany.models.*;
import com.mycompany.dao.StudentDAO;
import com.mycompany.models.Student;
import com.mycompany.utils.DatabaseUtil;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.ButtonType; 
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Dialog;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;
import javafx.util.Callback;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;

import java.math.BigDecimal;

import java.sql.Time;
import java.sql.SQLException; 
import java.sql.Connection;
import java.sql.Date;

public class Main extends Application {

    private Connection connection;

    @Override
    public void start(Stage primaryStage) {
        // Инициализация подключения к базе данных
        connection = DatabaseUtil.getConnection();

        // Создание кнопки "Enrollments"
        Button enrollmentsButton = new Button("Enrollments");
        enrollmentsButton.setOnAction(e -> openEnrollmentsWindow());

        // Создание кнопки "Students"
        Button studentsButton = new Button("Students");
        studentsButton.setOnAction(e -> openStudentsWindow());

        // Создание кнопки "Teachers"
        Button teachersButton = new Button("Teachers");
        teachersButton.setOnAction(e -> openTeachersWindow());

        // Создание кнопки "Departments"
        Button departmentsButton = new Button("Departments");
        departmentsButton.setOnAction(e -> openDepartmentsWindow());

        // Создание кнопки "Lessons"
        Button lessonsButton = new Button("Lessons");
        lessonsButton.setOnAction(e -> openLessonsWindow());

        // Создание кнопки "Courses"
        Button coursesButton = new Button("Courses");
        coursesButton.setOnAction(e -> openCoursesWindow());

        Button scheduleButton = new Button("Schedule");
        scheduleButton.setOnAction(e -> openScheduleWindow());

        // Расположение кнопок в вертикальном боксе
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(
            enrollmentsButton,
            studentsButton,
            teachersButton,
            departmentsButton,
            lessonsButton,
            coursesButton,
            scheduleButton
        );

        // Настройка сцены и отображение окна
        Scene scene = new Scene(vbox, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("University Management System");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void openEnrollmentsWindow() {
        Stage enrollmentsStage = new Stage();
        enrollmentsStage.setTitle("Manage Enrollments");

        // Создание формы поиска Enrollment
        Label searchLabel = new Label("Search Enrollments:");
        TextField searchStudentIdField = new TextField();
        searchStudentIdField.setPromptText("Student ID");
        TextField searchCourseIdField = new TextField();
        searchCourseIdField.setPromptText("Course ID");
        Button searchButton = new Button("Search");
        TableView<Enrollment> tableView = new TableView<>();

        // Настройка столбцов таблицы
        TableColumn<Enrollment, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Enrollment, Integer> studentIdCol = new TableColumn<>("Student ID");
        studentIdCol.setCellValueFactory(new PropertyValueFactory<>("studentId"));

        TableColumn<Enrollment, Integer> courseIdCol = new TableColumn<>("Course ID");
        courseIdCol.setCellValueFactory(new PropertyValueFactory<>("courseId"));

        TableColumn<Enrollment, Date> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Enrollment, BigDecimal> gradeCol = new TableColumn<>("Final Grade");
        gradeCol.setCellValueFactory(new PropertyValueFactory<>("finalGrade"));


        // Создание формы добавления нового Enrollment
        Label addLabel = new Label("Add New Enrollment:");
        TextField studentIdField = new TextField();
        studentIdField.setPromptText("Student ID");
        TextField courseIdField = new TextField();
        courseIdField.setPromptText("Course ID");
        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Enrollment Date");
        TextField finalGradeField = new TextField();
        finalGradeField.setPromptText("Final Grade");
        Button addButton = new Button("Add Enrollment");
        addButton.setOnAction(e -> {
            try {
                int studentId = Integer.parseInt(studentIdField.getText());
                int courseId = Integer.parseInt(courseIdField.getText());
                Date enrollmentDate = Date.valueOf(datePicker.getValue());
                BigDecimal finalGrade = new BigDecimal(finalGradeField.getText());
                Enrollment enrollment = new Enrollment(0, studentId, courseId, enrollmentDate, finalGrade);
                EnrollmentDAO enrollmentDAO = new EnrollmentDAO(connection);
                enrollmentDAO.addEnrollment(enrollment);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Enrollment added successfully!");
                alert.showAndWait();
                // Clear fields after successful addition
                studentIdField.clear();
                courseIdField.clear();
                datePicker.setValue(null);
                finalGradeField.clear();
                // Refresh the table view
                refreshEnrollmentsTable(enrollmentDAO, tableView);
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error adding enrollment: " + ex.getMessage());
                alert.showAndWait();
            }
        });
        VBox addBox = new VBox(5, addLabel, studentIdField, courseIdField, datePicker, finalGradeField, addButton);
        addBox.setPadding(new Insets(10));

        tableView.getColumns().addAll(idCol, studentIdCol, courseIdCol, dateCol, gradeCol);

        searchButton.setOnAction(e -> {
            try {
                Integer studentId = searchStudentIdField.getText().isEmpty() ? null : Integer.parseInt(searchStudentIdField.getText());
                Integer courseId = searchCourseIdField.getText().isEmpty() ? null : Integer.parseInt(searchCourseIdField.getText());
                EnrollmentDAO enrollmentDAO = new EnrollmentDAO(connection);
                List<Enrollment> results = enrollmentDAO.searchEnrollments(studentId, courseId);
                tableView.getItems().setAll(results);
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error searching enrollments: " + ex.getMessage());
                alert.showAndWait();
            }
        });

        // Добавление кнопок Delete и Update
        Button deleteButton = new Button("Delete Selected");
        deleteButton.setOnAction(e -> {
            Enrollment selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete the selected enrollment?");
                confirm.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        try {
                            EnrollmentDAO enrollmentDAO = new EnrollmentDAO(connection);
                            enrollmentDAO.deleteEnrollment(selected.getId());
                            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Enrollment deleted successfully!");
                            alert.showAndWait();
                            // Refresh the table view
                            refreshEnrollmentsTable(enrollmentDAO, tableView);
                        } catch (SQLException ex) {
                            Alert alert = new Alert(Alert.AlertType.ERROR, "Error deleting enrollment: " + ex.getMessage());
                            alert.showAndWait();
                        }
                    }
                });
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "No enrollment selected.");
                alert.showAndWait();
            }
        });

        Button updateButton = new Button("Update Selected");
        updateButton.setOnAction(e -> {
            Enrollment selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                // Создание диалогового окна для обновления Enrollment
                Stage updateStage = new Stage();
                updateStage.setTitle("Update Enrollment");

                Label updateLabel = new Label("Update Enrollment:");
                TextField updateStudentIdField = new TextField(String.valueOf(selected.getStudentId()));
                updateStudentIdField.setPromptText("Student ID");
                TextField updateCourseIdField = new TextField(String.valueOf(selected.getCourseId()));
                updateCourseIdField.setPromptText("Course ID");
                DatePicker updateDatePicker = new DatePicker(selected.getDate().toLocalDate());
                updateDatePicker.setPromptText("Enrollment Date");
                TextField updateFinalGradeField = new TextField(selected.getFinalGrade().toString());
                updateFinalGradeField.setPromptText("Final Grade");
                Button saveButton = new Button("Save Changes");
                saveButton.setOnAction(event -> {
                    try {
                        int studentId = Integer.parseInt(updateStudentIdField.getText());
                        int courseId = Integer.parseInt(updateCourseIdField.getText());
                        Date enrollmentDate = Date.valueOf(updateDatePicker.getValue());
                        BigDecimal finalGrade = new BigDecimal(updateFinalGradeField.getText());
                        Enrollment updatedEnrollment = new Enrollment(selected.getId(), studentId, courseId, enrollmentDate, finalGrade);
                        EnrollmentDAO enrollmentDAO = new EnrollmentDAO(connection);
                        enrollmentDAO.updateEnrollment(updatedEnrollment);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Enrollment updated successfully!");
                        alert.showAndWait();
                        updateStage.close();
                        // Refresh the table view
                        refreshEnrollmentsTable(enrollmentDAO, tableView);
                    } catch (Exception ex) {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Error updating enrollment: " + ex.getMessage());
                        alert.showAndWait();
                    }
                });

                VBox updateBox = new VBox(5, updateLabel, updateStudentIdField, updateCourseIdField, updateDatePicker, updateFinalGradeField, saveButton);
                updateBox.setPadding(new Insets(10));

                Scene updateScene = new Scene(updateBox, 300, 250);
                updateStage.setScene(updateScene);
                updateStage.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "No enrollment selected.");
                alert.showAndWait();
            }
        });

        HBox actionButtons = new HBox(10, deleteButton, updateButton);
        actionButtons.setPadding(new Insets(10));

        VBox searchBox = new VBox(5, searchLabel, searchStudentIdField, searchCourseIdField, searchButton, tableView, actionButtons);
        searchBox.setPadding(new Insets(10));

        // Расположение форм в горизонтальном боксе
        HBox hbox = new HBox(20, addBox, searchBox);
        hbox.setPadding(new Insets(10));

        Scene scene = new Scene(hbox, 900, 500);
        enrollmentsStage.setScene(scene);
        enrollmentsStage.show();
    }

    // Метод для обновления таблицы Enrollments
    private void refreshEnrollmentsTable(EnrollmentDAO enrollmentDAO, TableView<Enrollment> tableView) {
        try {
            List<Enrollment> enrollments = enrollmentDAO.getAllEnrollments();
            tableView.getItems().setAll(enrollments);
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error refreshing enrollments: " + ex.getMessage());
            alert.showAndWait();
        }
    }

    private void openStudentsWindow() {
        Stage studentsStage = new Stage();
        studentsStage.setTitle("Manage Students");

        Label searchLabel = new Label("Search Students:");
        TextField searchFirstNameField = new TextField();
        searchFirstNameField.setPromptText("First Name");
        TextField searchLastNameField = new TextField();
        searchLastNameField.setPromptText("Last Name");
        Button searchButton = new Button("Search");
        TableView<Student> tableView = new TableView<>();

        // Set up columns
        TableColumn<Student, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Student, String> firstNameCol = new TableColumn<>("First Name");
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<Student, String> lastNameCol = new TableColumn<>("Last Name");
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<Student, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<Student, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        TableColumn<Student, Date> enrollDateCol = new TableColumn<>("Enrollment Date");
        enrollDateCol.setCellValueFactory(new PropertyValueFactory<>("enrollmentDate"));

        tableView.getColumns().addAll(idCol, firstNameCol, lastNameCol, emailCol, phoneCol, enrollDateCol);

        // Add form
        Label addLabel = new Label("Add New Student:");
        TextField firstNameField = new TextField();
        firstNameField.setPromptText("First Name");
        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Last Name");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone Number");
        DatePicker enrollmentDatePicker = new DatePicker();
        enrollmentDatePicker.setPromptText("Enrollment Date");

        Button addButton = new Button("Add Student");
        addButton.setOnAction(e -> {
            try {
                StudentDAO studentDAO = new StudentDAO(connection);
                Student student = new Student(
                    0,
                    firstNameField.getText(),
                    lastNameField.getText(),
                    emailField.getText(),
                    phoneField.getText(),
                    Date.valueOf(enrollmentDatePicker.getValue())
                );
                studentDAO.addStudent(student);
                new Alert(Alert.AlertType.INFORMATION, "Student added successfully!").showAndWait();
                firstNameField.clear();
                lastNameField.clear();
                emailField.clear();
                phoneField.clear();
                enrollmentDatePicker.setValue(null);
                refreshStudentsTable(studentDAO, tableView);
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, "Error adding student: " + ex.getMessage()).showAndWait();
            }
        });

        // Search button action
        searchButton.setOnAction(e -> {
            try {
                StudentDAO studentDAO = new StudentDAO(connection);
                List<Student> results = studentDAO.searchStudents(
                    searchFirstNameField.getText(),
                    searchLastNameField.getText()
                );
                tableView.getItems().setAll(results);
            } catch (SQLException ex) {
                new Alert(Alert.AlertType.ERROR, "Error searching students: " + ex.getMessage()).showAndWait();
            }
        });

        // Delete button
        Button deleteButton = new Button("Delete Selected");
        deleteButton.setOnAction(e -> {
            Student selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this student?");
                confirm.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        try {
                            StudentDAO studentDAO = new StudentDAO(connection);
                            studentDAO.deleteStudent(selected.getId());
                            new Alert(Alert.AlertType.INFORMATION, "Student deleted successfully!").showAndWait();
                            refreshStudentsTable(studentDAO, tableView);
                        } catch (Exception ex) {
                            new Alert(Alert.AlertType.ERROR, "Error deleting student: " + ex.getMessage()).showAndWait();
                        }
                    }
                });
            } else {
                new Alert(Alert.AlertType.WARNING, "No student selected.").showAndWait();
            }
        });

        // Update button
        Button updateButton = new Button("Update Selected");
        updateButton.setOnAction(e -> {
            Student selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                Stage updateStage = new Stage();
                updateStage.setTitle("Update Student");

                TextField updateFirstName = new TextField(selected.getFirstName());
                TextField updateLastName = new TextField(selected.getLastName());
                TextField updateEmail = new TextField(selected.getEmail());
                TextField updatePhone = new TextField(selected.getPhoneNumber());
                DatePicker updateEnrollDate = new DatePicker(selected.getEnrollmentDate().toLocalDate());

                Button saveButton = new Button("Save Changes");
                saveButton.setOnAction(ev -> {
                    try {
                        StudentDAO studentDAO = new StudentDAO(connection);
                        Student updatedStudent = new Student(
                            selected.getId(),
                            updateFirstName.getText(),
                            updateLastName.getText(),
                            updateEmail.getText(),
                            updatePhone.getText(),
                            Date.valueOf(updateEnrollDate.getValue())
                        );
                        studentDAO.updateStudent(updatedStudent);
                        new Alert(Alert.AlertType.INFORMATION, "Student updated successfully!").showAndWait();
                        updateStage.close();
                        refreshStudentsTable(studentDAO, tableView);
                    } catch (Exception ex) {
                        new Alert(Alert.AlertType.ERROR, "Error updating student: " + ex.getMessage()).showAndWait();
                    }
                });

                VBox updateBox = new VBox(5,
                    new Label("Update Student"),
                    updateFirstName, updateLastName, updateEmail, updatePhone, updateEnrollDate,
                    saveButton
                );
                updateBox.setPadding(new Insets(10));
                Scene updateScene = new Scene(updateBox, 300, 250);
                updateStage.setScene(updateScene);
                updateStage.show();
            } else {
                new Alert(Alert.AlertType.WARNING, "No student selected.").showAndWait();
            }
        });

        VBox addBox = new VBox(5, addLabel, firstNameField, lastNameField, emailField, phoneField, enrollmentDatePicker, addButton);
        addBox.setPadding(new Insets(10));

        HBox actionButtons = new HBox(10, deleteButton, updateButton);
        actionButtons.setPadding(new Insets(10));

        VBox searchBox = new VBox(5, searchLabel, searchFirstNameField, searchLastNameField, searchButton, tableView, actionButtons);
        searchBox.setPadding(new Insets(10));

        HBox root = new HBox(20, addBox, searchBox);
        root.setPadding(new Insets(10));

        studentsStage.setScene(new Scene(root, 900, 500));
        studentsStage.show();
    }

    // Method to refresh the students table
    private void refreshStudentsTable(StudentDAO studentDAO, TableView<Student> tableView) {
        try {
            List<Student> students = studentDAO.getAllStudents();
            tableView.getItems().setAll(students);
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error refreshing students: " + ex.getMessage());
            alert.showAndWait();
        }
    }


    private void openTeachersWindow() {
        Stage teachersStage = new Stage();
        teachersStage.setTitle("Manage Teachers");

        Label searchLabel = new Label("Search Teachers:");
        TextField searchIDField = new TextField();
        searchIDField.setPromptText("ID");
        TextField searchLastNameField = new TextField();
        searchLastNameField.setPromptText("Last Name");
        TextField searchDepartmentIDField = new TextField();
        searchDepartmentIDField.setPromptText("Department ID");
        Button searchButton = new Button("Search");
        TableView<Teacher> tableView = new TableView<>();

        // Set up columns
        TableColumn<Teacher, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Teacher, String> firstNameCol = new TableColumn<>("First Name");
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<Teacher, String> lastNameCol = new TableColumn<>("Last Name");
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<Teacher, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<Teacher, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        TableColumn<Teacher, Integer> departmentIdCol = new TableColumn<>("Department ID");
        departmentIdCol.setCellValueFactory(new PropertyValueFactory<>("departmentId"));

        tableView.getColumns().addAll(idCol, firstNameCol, lastNameCol, emailCol, phoneCol, departmentIdCol);

        // Add form
        Label addLabel = new Label("Add New Teacher:");
        TextField firstNameField = new TextField();
        firstNameField.setPromptText("First Name");
        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Last Name");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone Number");
        TextField departmentIdField = new TextField();
        departmentIdField.setPromptText("Department ID");

        Button addButton = new Button("Add Teacher");
        addButton.setOnAction(e -> {
            try {
                TeacherDAO teacherDAO = new TeacherDAO(connection);
                Integer departmentId = departmentIdField.getText().isEmpty() ? null : Integer.parseInt(departmentIdField.getText());
                Teacher teacher = new Teacher(
                    0,
                    firstNameField.getText(),
                    lastNameField.getText(),
                    emailField.getText(),
                    phoneField.getText(),
                    departmentId
                );
                teacherDAO.addTeacher(teacher);
                new Alert(Alert.AlertType.INFORMATION, "Teacher added successfully!").showAndWait();
                firstNameField.clear();
                lastNameField.clear();
                emailField.clear();
                phoneField.clear();
                departmentIdField.clear();
                refreshTeachersTable(teacherDAO, tableView);
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, "Error adding teacher: " + ex.getMessage()).showAndWait();
            }
        });

        // Search button action
        searchButton.setOnAction(e -> {
            try {
                TeacherDAO teacherDAO = new TeacherDAO(connection);
                Integer teacherId = searchIDField.getText().isEmpty() ? null : Integer.parseInt(searchIDField.getText());
                Integer departmentId = searchDepartmentIDField.getText().isEmpty() ? null : Integer.parseInt(searchDepartmentIDField.getText());
                List<Teacher> results = teacherDAO.searchTeachers(teacherId, searchLastNameField.getText(), departmentId);
                tableView.getItems().setAll(results);
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, "Error searching teachers: " + ex.getMessage()).showAndWait();
            }
        });

        // Delete button
        Button deleteButton = new Button("Delete Selected");
        deleteButton.setOnAction(e -> {
            Teacher selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this teacher?");
                confirm.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        try {
                            TeacherDAO teacherDAO = new TeacherDAO(connection);
                            teacherDAO.deleteTeacher(selected.getId());
                            new Alert(Alert.AlertType.INFORMATION, "Teacher deleted successfully!").showAndWait();
                            refreshTeachersTable(teacherDAO, tableView);
                        } catch (Exception ex) {
                            new Alert(Alert.AlertType.ERROR, "Error deleting teacher: " + ex.getMessage()).showAndWait();
                        }
                    }
                });
            } else {
                new Alert(Alert.AlertType.WARNING, "No teacher selected.").showAndWait();
            }
        });

        // Update button
        Button updateButton = new Button("Update Selected");
        updateButton.setOnAction(e -> {
            Teacher selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                Stage updateStage = new Stage();
                updateStage.setTitle("Update Teacher");

                TextField updateFirstName = new TextField(selected.getFirstName());
                TextField updateLastName = new TextField(selected.getLastName());
                TextField updateEmail = new TextField(selected.getEmail());
                TextField updatePhone = new TextField(selected.getPhoneNumber());
                TextField updateDepartmentId = new TextField(selected.getDepartmentId() != null ? selected.getDepartmentId().toString() : "");

                Button saveButton = new Button("Save Changes");
                saveButton.setOnAction(ev -> {
                    try {
                        TeacherDAO teacherDAO = new TeacherDAO(connection);
                        Integer departmentId = updateDepartmentId.getText().isEmpty() ? null : Integer.parseInt(updateDepartmentId.getText());
                        Teacher updatedTeacher = new Teacher(
                            selected.getId(),
                            updateFirstName.getText(),
                            updateLastName.getText(),
                            updateEmail.getText(),
                            updatePhone.getText(),
                            departmentId
                        );
                        teacherDAO.updateTeacher(updatedTeacher);
                        new Alert(Alert.AlertType.INFORMATION, "Teacher updated successfully!").showAndWait();
                        updateStage.close();
                        refreshTeachersTable(teacherDAO, tableView);
                    } catch (Exception ex) {
                        new Alert(Alert.AlertType.ERROR, "Error updating teacher: " + ex.getMessage()).showAndWait();
                    }
                });

                VBox updateBox = new VBox(5,
                    new Label("Update Teacher"),
                    updateFirstName, updateLastName, updateEmail, updatePhone, updateDepartmentId,
                    saveButton
                );
                updateBox.setPadding(new Insets(10));
                Scene updateScene = new Scene(updateBox, 300, 250);
                updateStage.setScene(updateScene);
                updateStage.show();
            } else {
                new Alert(Alert.AlertType.WARNING, "No teacher selected.").showAndWait();
            }
        });

        VBox addBox = new VBox(5, addLabel, firstNameField, lastNameField, emailField, phoneField, departmentIdField, addButton);
        addBox.setPadding(new Insets(10));

        HBox actionButtons = new HBox(10, deleteButton, updateButton);
        actionButtons.setPadding(new Insets(10));

        VBox searchBox = new VBox(5, searchLabel, searchIDField, searchLastNameField, searchDepartmentIDField, searchButton, tableView, actionButtons);
        searchBox.setPadding(new Insets(10));

        HBox root = new HBox(20, addBox, searchBox);
        root.setPadding(new Insets(10));

        teachersStage.setScene(new Scene(root, 900, 500));
        teachersStage.show();
    }

    // Method to refresh the teachers table
    private void refreshTeachersTable(TeacherDAO teacherDAO, TableView<Teacher> tableView) {
        try {
            List<Teacher> teachers = teacherDAO.getAllTeachers();
            tableView.getItems().setAll(teachers);
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error refreshing teachers: " + ex.getMessage());
            alert.showAndWait();
        }
    }

    private void openDepartmentsWindow() {
        Stage departmentsStage = new Stage();
        departmentsStage.setTitle("Manage Departments");

        // Создание таблицы для отображения департаментов
        TableView<Department> tableView = new TableView<>();
        TableColumn<Department, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Department, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Department, Integer> chiefIdCol = new TableColumn<>("Chief ID");
        chiefIdCol.setCellValueFactory(new PropertyValueFactory<>("chiefId"));

        tableView.getColumns().addAll(idCol, nameCol, chiefIdCol);

        // Создание формы добавления нового департамента
        Label addLabel = new Label("Add New Department:");
        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        TextField chiefIdField = new TextField();
        chiefIdField.setPromptText("Chief ID");
        Button addButton = new Button("Add Department");
        addButton.setOnAction(e -> {
            try {
                DepartmentDAO departmentDAO = new DepartmentDAO(connection);
                Integer chiefId = chiefIdField.getText().isEmpty() ? null : Integer.parseInt(chiefIdField.getText());
                Department department = new Department(0, nameField.getText(), chiefId);
                departmentDAO.addDepartment(department);
                new Alert(Alert.AlertType.INFORMATION, "Department added successfully!").showAndWait();
                nameField.clear();
                chiefIdField.clear();
                refreshDepartmentsTable(departmentDAO, tableView);
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, "Error adding department: " + ex.getMessage()).showAndWait();
            }
        });
        VBox addBox = new VBox(5, addLabel, nameField, chiefIdField, addButton);
        addBox.setPadding(new Insets(10));

        // Удаление создания кнопки "Search"

        // Создание кнопок "Delete" и "Update"
        Button deleteButton = new Button("Delete Selected");
        deleteButton.setOnAction(e -> {
            Department selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this department?");
                confirm.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        try {
                            DepartmentDAO departmentDAO = new DepartmentDAO(connection);
                            departmentDAO.deleteDepartment(selected.getId());
                            new Alert(Alert.AlertType.INFORMATION, "Department deleted successfully!").showAndWait();
                            refreshDepartmentsTable(departmentDAO, tableView);
                        } catch (Exception ex) {
                            new Alert(Alert.AlertType.ERROR, "Error deleting department: " + ex.getMessage()).showAndWait();
                        }
                    }
                });
            } else {
                new Alert(Alert.AlertType.WARNING, "No department selected.").showAndWait();
            }
        });

        Button updateButton = new Button("Update Selected");
        updateButton.setOnAction(e -> {
            Department selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                Stage updateStage = new Stage();
                updateStage.setTitle("Update Department");

                TextField updateNameField = new TextField(selected.getName());
                TextField updateChiefIdField = new TextField(selected.getChiefId() != null ? selected.getChiefId().toString() : "");

                Button saveButton = new Button("Save Changes");
                saveButton.setOnAction(ev -> {
                    try {
                        DepartmentDAO departmentDAO = new DepartmentDAO(connection);
                        Integer chiefId = updateChiefIdField.getText().isEmpty() ? null : Integer.parseInt(updateChiefIdField.getText());
                        Department updatedDepartment = new Department(
                                selected.getId(),
                                updateNameField.getText(),
                                chiefId
                        );
                        departmentDAO.updateDepartment(updatedDepartment);
                        new Alert(Alert.AlertType.INFORMATION, "Department updated successfully!").showAndWait();
                        updateStage.close();
                        refreshDepartmentsTable(departmentDAO, tableView);
                    } catch (Exception ex) {
                        new Alert(Alert.AlertType.ERROR, "Error updating department: " + ex.getMessage()).showAndWait();
                    }
                });

                VBox updateBox = new VBox(5,
                        new Label("Update Department"),
                        updateNameField,
                        updateChiefIdField,
                        saveButton
                );
                updateBox.setPadding(new Insets(10));
                Scene updateScene = new Scene(updateBox, 300, 200);
                updateStage.setScene(updateScene);
                updateStage.show();
            } else {
                new Alert(Alert.AlertType.WARNING, "No department selected.").showAndWait();
            }
        });

        HBox actionButtons = new HBox(10, deleteButton, updateButton);
        actionButtons.setPadding(new Insets(10));

        // Расположение элементов в вертикальном боксе
        VBox searchBox = new VBox(5, tableView, actionButtons);
        searchBox.setPadding(new Insets(10));

        // Расположение форм в горизонтальном боксе
        HBox root = new HBox(20, addBox, searchBox);
        root.setPadding(new Insets(10));

        // Автоматическая загрузка данных при открытии окна
        DepartmentDAO departmentDAO = new DepartmentDAO(connection);
        refreshDepartmentsTable(departmentDAO, tableView);

        Scene scene = new Scene(root, 900, 500);
        departmentsStage.setScene(scene);
        departmentsStage.show();
    }

    // Метод для обновления таблицы департаментов
    private void refreshDepartmentsTable(DepartmentDAO departmentDAO, TableView<Department> tableView) {
        try {
            List<Department> departments = departmentDAO.getAllDepartments();
            tableView.getItems().setAll(departments);
        } catch (SQLException ex) {
            new Alert(Alert.AlertType.ERROR, "Error refreshing departments: " + ex.getMessage()).showAndWait();
        }
    }

    private void openLessonsWindow() {
        Stage lessonsStage = new Stage();
        lessonsStage.setTitle("Manage Lessons");

        // Создание формы добавления нового занятия
        Label addLabel = new Label("Add New Lesson:");
        TextField lessonNumberField = new TextField();
        lessonNumberField.setPromptText("Lesson Number");
        TextField startTimeField = new TextField();
        startTimeField.setPromptText("Start Time (HH:MM:SS)");
        TextField endTimeField = new TextField();
        endTimeField.setPromptText("End Time (HH:MM:SS)");
        Button addButton = new Button("Add Lesson");

        // Настройка таблицы для отображения занятий
        TableView<Lesson> tableView = new TableView<>();

        TableColumn<Lesson, Integer> lessonNumberCol = new TableColumn<>("Lesson Number");
        lessonNumberCol.setCellValueFactory(new PropertyValueFactory<>("lessonNumber"));

        TableColumn<Lesson, Time> startTimeCol = new TableColumn<>("Start Time");
        startTimeCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));

        TableColumn<Lesson, Time> endTimeCol = new TableColumn<>("End Time");
        endTimeCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));

        tableView.getColumns().addAll(lessonNumberCol, startTimeCol, endTimeCol);

        // Кнопка "Search" для отображения всех занятий
        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> {
            try {
                LessonDAO lessonDAO = new LessonDAO(connection);
                List<Lesson> lessons = lessonDAO.getAllLessons();
                tableView.getItems().setAll(lessons);
            } catch (SQLException ex) {
                new Alert(Alert.AlertType.ERROR, "Error fetching lessons: " + ex.getMessage()).showAndWait();
            }
        });

        // Действие кнопки "Add Lesson"
        addButton.setOnAction(e -> {
            try {
                int lessonNumber = Integer.parseInt(lessonNumberField.getText());
                Time startTime = Time.valueOf(startTimeField.getText());
                Time endTime = Time.valueOf(endTimeField.getText());

                Lesson lesson = new Lesson(lessonNumber, startTime, endTime);
                LessonDAO lessonDAO = new LessonDAO(connection);
                lessonDAO.addLesson(lesson);
                new Alert(Alert.AlertType.INFORMATION, "Lesson added successfully!").showAndWait();
                lessonNumberField.clear();
                startTimeField.clear();
                endTimeField.clear();
                refreshLessonsTable(lessonDAO, tableView);
            } catch (NumberFormatException ex) {
                new Alert(Alert.AlertType.ERROR, "Invalid lesson number. Please enter a valid integer.").showAndWait();
            } catch (IllegalArgumentException ex) {
                new Alert(Alert.AlertType.ERROR, "Invalid time format. Please use HH:MM:SS.").showAndWait();
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, "Error adding lesson: " + ex.getMessage()).showAndWait();
            }
        });

        // Кнопки "Delete" и "Update"
        Button deleteButton = new Button("Delete Selected");
        deleteButton.setOnAction(e -> {
            Lesson selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this lesson?");
                confirm.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        try {
                            LessonDAO lessonDAO = new LessonDAO(connection);
                            lessonDAO.deleteLesson(selected.getLessonNumber());
                            new Alert(Alert.AlertType.INFORMATION, "Lesson deleted successfully!").showAndWait();
                            refreshLessonsTable(lessonDAO, tableView);
                        } catch (Exception ex) {
                            new Alert(Alert.AlertType.ERROR, "Error deleting lesson: " + ex.getMessage()).showAndWait();
                        }
                    }
                });
            } else {
                new Alert(Alert.AlertType.WARNING, "No lesson selected.").showAndWait();
            }
        });

        Button updateButton = new Button("Update Selected");
        updateButton.setOnAction(e -> {
            Lesson selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                Stage updateStage = new Stage();
                updateStage.setTitle("Update Lesson");

                TextField updateStartTimeField = new TextField(selected.getStartTime().toString());
                updateStartTimeField.setPromptText("Start Time (HH:MM:SS)");
                TextField updateEndTimeField = new TextField(selected.getEndTime().toString());
                updateEndTimeField.setPromptText("End Time (HH:MM:SS)");
                Button saveButton = new Button("Save Changes");

                saveButton.setOnAction(ev -> {
                    try {
                        Time newStartTime = Time.valueOf(updateStartTimeField.getText());
                        Time newEndTime = Time.valueOf(updateEndTimeField.getText());

                        selected.setStartTime(newStartTime);
                        selected.setEndTime(newEndTime);

                        LessonDAO lessonDAO = new LessonDAO(connection);
                        lessonDAO.updateLesson(selected);
                        new Alert(Alert.AlertType.INFORMATION, "Lesson updated successfully!").showAndWait();
                        updateStage.close();
                        refreshLessonsTable(lessonDAO, tableView);
                    } catch (IllegalArgumentException ex) {
                        new Alert(Alert.AlertType.ERROR, "Invalid time format. Please use HH:MM:SS.").showAndWait();
                    } catch (Exception ex) {
                        new Alert(Alert.AlertType.ERROR, "Error updating lesson: " + ex.getMessage()).showAndWait();
                    }
                });

                VBox updateBox = new VBox(5, new Label("Update Lesson:"), updateStartTimeField, updateEndTimeField, saveButton);
                updateBox.setPadding(new Insets(10));
                Scene updateScene = new Scene(updateBox, 300, 200);
                updateStage.setScene(updateScene);
                updateStage.show();
            } else {
                new Alert(Alert.AlertType.WARNING, "No lesson selected.").showAndWait();
            }
        });

        // Расположение кнопок действий
        HBox actionButtons = new HBox(10, deleteButton, updateButton, searchButton);
        actionButtons.setPadding(new Insets(10));

        // Расположение формы добавления и таблицы в горизонтальном боксе
        VBox addBox = new VBox(5, addLabel, lessonNumberField, startTimeField, endTimeField, addButton);
        addBox.setPadding(new Insets(10));

        VBox searchBox = new VBox(5, tableView, actionButtons);
        searchBox.setPadding(new Insets(10));

        HBox root = new HBox(20, addBox, searchBox);
        root.setPadding(new Insets(10));

        // Инициализация таблицы с существующими занятиями при открытии окна
        try {
            LessonDAO lessonDAO = new LessonDAO(connection);
            List<Lesson> lessons = lessonDAO.getAllLessons();
            tableView.getItems().setAll(lessons);
        } catch (SQLException ex) {
            new Alert(Alert.AlertType.ERROR, "Error fetching lessons: " + ex.getMessage()).showAndWait();
        }

        Scene scene = new Scene(root, 900, 500);
        lessonsStage.setScene(scene);
        lessonsStage.show();
    }

    // Метод для обновления таблицы занятий
    private void refreshLessonsTable(LessonDAO lessonDAO, TableView<Lesson> tableView) {
        try {
            List<Lesson> lessons = lessonDAO.getAllLessons();
            tableView.getItems().setAll(lessons);
        } catch (SQLException ex) {
            new Alert(Alert.AlertType.ERROR, "Error refreshing lessons: " + ex.getMessage()).showAndWait();
        }
    }


    private void openCoursesWindow() {
        Stage coursesStage = new Stage();
        coursesStage.setTitle("Manage Courses");

        // Search Form
        Label searchLabel = new Label("Search Courses:");
        TextField searchIdField = new TextField();
        searchIdField.setPromptText("Course ID");
        TextField searchNameField = new TextField();
        searchNameField.setPromptText("Course Name");
        Button searchButton = new Button("Search");
        TableView<Course> tableView = new TableView<>();

        // Настройка столбцов таблицы
        TableColumn<Course, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Course, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Course, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<Course, Integer> creditsCol = new TableColumn<>("Credits");
        creditsCol.setCellValueFactory(new PropertyValueFactory<>("credits"));

        tableView.getColumns().addAll(idCol, nameCol, descCol, creditsCol);

        // Add Course Form
        Label addLabel = new Label("Add New Course:");
        TextField nameField = new TextField();
        nameField.setPromptText("Course Name");
        TextField descField = new TextField();
        descField.setPromptText("Description");
        TextField creditsField = new TextField();
        creditsField.setPromptText("Credits");
        Button addButton = new Button("Add Course");

        VBox addBox = new VBox(5, addLabel, nameField, descField, creditsField, addButton);
        addBox.setPadding(new Insets(10));

        // Action Buttons
        Button deleteButton = new Button("Delete Selected");
        Button updateButton = new Button("Update Selected");
        HBox actionButtons = new HBox(10, deleteButton, updateButton);
        actionButtons.setPadding(new Insets(10));

        // Layout
        VBox leftBox = new VBox(10, addBox, actionButtons);
        leftBox.setPadding(new Insets(10));

        VBox rightBox = new VBox(10, searchLabel, searchIdField, searchNameField, searchButton, tableView);
        rightBox.setPadding(new Insets(10));

        HBox mainLayout = new HBox(20, leftBox, rightBox);
        mainLayout.setPadding(new Insets(10));

        Scene scene = new Scene(mainLayout, 800, 600);
        coursesStage.setScene(scene);
        coursesStage.show();

        // Initialize CourseDAO
        CourseDAO courseDAO = new CourseDAO(connection);
        refreshCoursesTable(courseDAO, tableView);

        // Search Button Action
        searchButton.setOnAction(e -> {
            try {
                Integer id = searchIdField.getText().isEmpty() ? null : Integer.parseInt(searchIdField.getText());
                String name = searchNameField.getText().isEmpty() ? null : searchNameField.getText();
                List<Course> courses = courseDAO.searchCourses(id, name);
                ObservableList<Course> data = FXCollections.observableArrayList(courses);
                tableView.setItems(data);
            } catch (SQLException ex) {
                showAlert("Error", "Failed to search courses.");
            }
        });

        // Add Button Action
        addButton.setOnAction(e -> {
            String name = nameField.getText();
            String desc = descField.getText();
            int credits;
            try {
                credits = Integer.parseInt(creditsField.getText());
            } catch (NumberFormatException ex) {
                showAlert("Input Error", "Credits must be a number.");
                return;
            }

            Course course = new Course();
            course.setName(name);
            course.setDescription(desc);
            course.setCredits(credits);

            try {
                courseDAO.addCourse(course);
                refreshCoursesTable(courseDAO, tableView);
                nameField.clear();
                descField.clear();
                creditsField.clear();
            } catch (SQLException ex) {
                showAlert("Error", "Failed to add course.");
            }
        });

        // Delete Button Action
        deleteButton.setOnAction(e -> {
            Course selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                try {
                    courseDAO.deleteCourse(selected.getId());
                    refreshCoursesTable(courseDAO, tableView);
                } catch (SQLException ex) {
                    showAlert("Error", "Failed to delete course.");
                }
            } else {
                showAlert("Selection Error", "No course selected.");
            }
        });

        // Update Button Action
        updateButton.setOnAction(e -> {
            Course selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                Stage updateStage = new Stage();
                updateStage.setTitle("Update Course");

                TextField updateNameField = new TextField(selected.getName());
                TextField updateDescriptionField = new TextField(selected.getDescription());
                TextField updateCreditsField = new TextField(String.valueOf(selected.getCredits()));

                Button saveButton = new Button("Save Changes");
                saveButton.setOnAction(ev -> {
                    try {
                        CourseDAO courseDAOInner = new CourseDAO(connection);
                        int credits = Integer.parseInt(updateCreditsField.getText());
                        Course updatedCourse = new Course(
                                selected.getId(),
                                updateNameField.getText(),
                                updateDescriptionField.getText(),
                                credits
                        );
                        courseDAOInner.updateCourse(updatedCourse);
                        new Alert(Alert.AlertType.INFORMATION, "Course updated successfully!").showAndWait();
                        updateStage.close();
                        refreshCoursesTable(courseDAOInner, tableView);
                    } catch (Exception ex) {
                        new Alert(Alert.AlertType.ERROR, "Error updating course: " + ex.getMessage()).showAndWait();
                    }
                });

                VBox updateBox = new VBox(5,
                        new Label("Update Course"),
                        updateNameField,
                        updateDescriptionField,
                        updateCreditsField,
                        saveButton
                );
                updateBox.setPadding(new Insets(10));
                Scene updateScene = new Scene(updateBox, 300, 250);
                updateStage.setScene(updateScene);
                updateStage.show();
            } else {
                new Alert(Alert.AlertType.WARNING, "No course selected.").showAndWait();
            }
        });
    }


    private void refreshCoursesTable(CourseDAO courseDAO, TableView<Course> tableView) {
        try {
            List<Course> courses = courseDAO.getAllCourses();
            ObservableList<Course> data = FXCollections.observableArrayList(courses);
            tableView.setItems(data);
        } catch (SQLException ex) {
            showAlert("Error", "Failed to load courses.");
        }
    }

    private void openScheduleWindow() {
        Stage scheduleStage = new Stage();
        scheduleStage.setTitle("Manage Schedule");

        // -------------------- Add Schedule Section --------------------
        Label addLabel = new Label("Add New Schedule:");

        ComboBox<Course> courseComboBox = new ComboBox<>();
        courseComboBox.setPromptText("Select Course");
        ComboBox<Teacher> teacherComboBox = new ComboBox<>();
        teacherComboBox.setPromptText("Select Teacher");
        ComboBox<Lesson> lessonComboBox = new ComboBox<>();
        lessonComboBox.setPromptText("Select Lesson");
        ComboBox<String> dayComboBox = new ComboBox<>();
        dayComboBox.setPromptText("Select Day");
        TextField roomNumberField = new TextField();
        roomNumberField.setPromptText("Room Number");
        Button addButton = new Button("Add Schedule");

        // Populate ComboBoxes
        try {
            CourseDAO courseDAO = new CourseDAO(connection);
            List<Course> courses = courseDAO.getAllCourses();
            courseComboBox.setItems(FXCollections.observableArrayList(courses));

            TeacherDAO teacherDAO = new TeacherDAO(connection);
            List<Teacher> teachers = teacherDAO.getAllTeachers();
            teacherComboBox.setItems(FXCollections.observableArrayList(teachers));

            LessonDAO lessonDAO = new LessonDAO(connection);
            List<Lesson> lessons = lessonDAO.getAllLessons();
            lessonComboBox.setItems(FXCollections.observableArrayList(lessons));
        } catch (SQLException ex) {
            showAlert("Ошибка", "Не удалось загрузить данные: " + ex.getMessage());
        }

        dayComboBox.setItems(FXCollections.observableArrayList(
            "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
        ));

        // Configure Course ComboBox to display only the course name
        courseComboBox.setCellFactory(new Callback<ListView<Course>, ListCell<Course>>() {
            @Override
            public ListCell<Course> call(ListView<Course> param) {
                return new ListCell<Course>() {
                    @Override
                    protected void updateItem(Course item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(empty || item == null ? null : item.getName());
                    }
                };
            }
        });

        courseComboBox.setButtonCell(new ListCell<Course>() {
            @Override
            protected void updateItem(Course item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });

        // Configure Teacher ComboBox to display "FirstName LastName"
        teacherComboBox.setCellFactory(new Callback<ListView<Teacher>, ListCell<Teacher>>() {
            @Override
            public ListCell<Teacher> call(ListView<Teacher> param) {
                return new ListCell<Teacher>() {
                    @Override
                    protected void updateItem(Teacher item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(item.getFirstName() + " " + item.getLastName());
                        }
                    }
                };
            }
        });

        teacherComboBox.setButtonCell(new ListCell<Teacher>() {
            @Override
            protected void updateItem(Teacher item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getFirstName() + " " + item.getLastName());
            }
        });

        // Configure Lesson ComboBox to display "StartTime - EndTime"
        lessonComboBox.setCellFactory(new Callback<ListView<Lesson>, ListCell<Lesson>>() {
            @Override
            public ListCell<Lesson> call(ListView<Lesson> param) {
                return new ListCell<Lesson>() {
                    @Override
                    protected void updateItem(Lesson item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(item.getStartTime().toString() + " - " + item.getEndTime().toString());
                        }
                    }
                };
            }
        });

        lessonComboBox.setButtonCell(new ListCell<Lesson>() {
            @Override
            protected void updateItem(Lesson item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getStartTime().toString() + " - " + item.getEndTime().toString());
            }
        });

        // Optional: Use StringConverter for better handling
        courseComboBox.setConverter(new StringConverter<Course>() {
            @Override
            public String toString(Course course) {
                return course != null ? course.getName() : "";
            }

            @Override
            public Course fromString(String string) {
                return null; // Not needed for ComboBox
            }
        });

        teacherComboBox.setConverter(new StringConverter<Teacher>() {
            @Override
            public String toString(Teacher teacher) {
                return teacher != null ? teacher.getFirstName() + " " + teacher.getLastName() : "";
            }

            @Override
            public Teacher fromString(String string) {
                return null;
            }
        });

        lessonComboBox.setConverter(new StringConverter<Lesson>() {
            @Override
            public String toString(Lesson lesson) {
                return lesson != null ? lesson.getStartTime() + " - " + lesson.getEndTime() : "";
            }

            @Override
            public Lesson fromString(String string) {
                return null;
            }
        });
        TableView<Schedule> tableView = new TableView<>();

        // Add Button Action
        addButton.setOnAction(e -> {
            try {
                Course selectedCourse = courseComboBox.getValue();
                Teacher selectedTeacher = teacherComboBox.getValue();
                Lesson selectedLesson = lessonComboBox.getValue();
                String selectedDay = dayComboBox.getValue();
                int dayNumber = convertDayToNumber(selectedDay);
                int roomNumber = Integer.parseInt(roomNumberField.getText());

                if (selectedCourse == null || selectedTeacher == null || selectedLesson == null || selectedDay == null) {
                    showAlert("Ошибка", "Пожалуйста, заполните все поля.");
                    return;
                }

                ScheduleDAO scheduleDAO = new ScheduleDAO(connection);

                // Check if the teacher is busy
                if (scheduleDAO.isTeacherBusy(selectedTeacher.getId(), dayNumber, selectedLesson.getLessonNumber())) {
                    showAlert("Ошибка", "Этот учитель уже занят в выбранное время.");
                    return;
                }

                // Check if the room is busy
                if (scheduleDAO.isRoomBusy(roomNumber, dayNumber, selectedLesson.getLessonNumber())) {
                    showAlert("Ошибка", "Эта комната уже занята в выбранное время.");
                    return;
                }

                // If no conflicts, add the schedule
                Schedule schedule = new Schedule();
                schedule.setCourseId(selectedCourse.getId());
                schedule.setTeacherId(selectedTeacher.getId());
                schedule.setDayNumber(dayNumber);
                schedule.setLessonId(selectedLesson.getLessonNumber());
                schedule.setRoomNumber(roomNumber);

                scheduleDAO.addSchedule(schedule);

                showAlert("Успех", "Расписание добавлено успешно!");
                refreshScheduleTable(scheduleDAO, tableView);
            } catch (NumberFormatException ex) {
                showAlert("Ошибка", "Неверный формат номера комнаты.");
            } catch (SQLException ex) {
                showAlert("Ошибка", "Произошла ошибка при добавлении расписания: " + ex.getMessage());
            } catch (Exception ex) {
                showAlert("Ошибка", "Произошла непредвиденная ошибка: " + ex.getMessage());
            }
        });

        VBox addBox = new VBox(10, addLabel, courseComboBox, teacherComboBox, lessonComboBox, dayComboBox, roomNumberField, addButton);
        addBox.setPadding(new Insets(10));
        addBox.setStyle("-fx-border-color: black; -fx-border-width: 1px;");

        // -------------------- Search Section --------------------
        Label searchLabel = new Label("Search Schedules:");
        TextField searchStudentIdField = new TextField();
        searchStudentIdField.setPromptText("Student ID");
        TextField searchTeacherIdField = new TextField();
        searchTeacherIdField.setPromptText("Teacher ID");
        TextField searchRoomIdField = new TextField();
        searchRoomIdField.setPromptText("Room ID");
        Button searchButton = new Button("Search");

        HBox searchBox = new HBox(10, searchStudentIdField, searchTeacherIdField, searchRoomIdField, searchButton);
        searchBox.setPadding(new Insets(10));

        // -------------------- TableView Section --------------------
        ScheduleDAO scheduleDAO = new ScheduleDAO(connection);
        try {
            List<Schedule> schedules = scheduleDAO.getAllSchedules();
            tableView.setItems(FXCollections.observableArrayList(schedules));
        } catch (SQLException ex) {
            showAlert("Ошибка", "Не удалось загрузить расписания: " + ex.getMessage());
        }

        // Настройка столбцов таблицы
        TableColumn<Schedule, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Schedule, String> courseCol = new TableColumn<>("Course");
        courseCol.setCellValueFactory(cellData -> {
            try {
                CourseDAO cDao = new CourseDAO(connection);
                Course course = cDao.getCourseById(cellData.getValue().getCourseId());
                return new SimpleStringProperty(course.getName());
            } catch (SQLException ex) {
                return new SimpleStringProperty("Error");
            }
        });

        TableColumn<Schedule, String> teacherCol = new TableColumn<>("Teacher");
        teacherCol.setCellValueFactory(cellData -> {
            try {
                TeacherDAO tDao = new TeacherDAO(connection);
                Teacher teacher = tDao.getTeacherById(cellData.getValue().getTeacherId());
                return new SimpleStringProperty(teacher.getFirstName() + " " + teacher.getLastName());
            } catch (SQLException ex) {
                return new SimpleStringProperty("Error");
            }
        });

        TableColumn<Schedule, String> lessonCol = new TableColumn<>("Lesson Time");
        lessonCol.setCellValueFactory(cellData -> {
            try {
                LessonDAO lDao = new LessonDAO(connection);
                Lesson lesson = lDao.getLessonById(cellData.getValue().getLessonId());
                return new SimpleStringProperty(lesson.getStartTime() + " - " + lesson.getEndTime());
            } catch (SQLException ex) {
                return new SimpleStringProperty("Error");
            }
        });

        TableColumn<Schedule, String> dayCol = new TableColumn<>("Day");
        dayCol.setCellValueFactory(cellData -> {
            int dayNumber = cellData.getValue().getDayNumber();
            String day = convertNumberToDay(dayNumber);
            return new SimpleStringProperty(day);
        });

        TableColumn<Schedule, Integer> roomCol = new TableColumn<>("Room Number");
        roomCol.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));

        tableView.getColumns().addAll(idCol, courseCol, teacherCol, lessonCol, dayCol, roomCol);

        // Кнопки Delete и Update
        Button deleteButton = new Button("Delete Selected");
        deleteButton.setOnAction(e -> {
            Schedule selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                try {
                    scheduleDAO.deleteSchedule(selected.getId());
                    tableView.getItems().remove(selected);
                    showAlert("Успех", "Расписание удалено успешно!");
                } catch (SQLException ex) {
                    showAlert("Ошибка", "Не удалось удалить расписание: " + ex.getMessage());
                }
            } else {
                showAlert("Информация", "Пожалуйста, выберите расписание для удаления.");
            }
        });

        Button updateButton = new Button("Update Selected");
        updateButton.setOnAction(e -> {
            Schedule selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                // Реализуйте логику обновления расписания
                // Например, откройте диалоговое окно для редактирования выбранного расписания
                showAlert("Информация", "Функция обновления ещё не реализована.");
            } else {
                showAlert("Информация", "Пожалуйста, выберите расписание для обновления.");
            }
        });

        HBox actionButtons = new HBox(10, deleteButton, updateButton);
        actionButtons.setPadding(new Insets(10));

        VBox tableBox = new VBox(10, searchLabel, searchBox, tableView, actionButtons);
        tableBox.setPadding(new Insets(10));
        tableBox.setStyle("-fx-border-color: black; -fx-border-width: 1px;");

        // -------------------- Search Button Action --------------------
        searchButton.setOnAction(e -> {
            String studentIdText = searchStudentIdField.getText().trim();
            String teacherIdText = searchTeacherIdField.getText().trim();
            String roomIdText = searchRoomIdField.getText().trim();

            ScheduleDAO sDao = new ScheduleDAO(connection);
            List<Schedule> results = new ArrayList<>();

            try {
                if (!studentIdText.isEmpty()) {
                    int studentId = Integer.parseInt(studentIdText);
                    results.addAll(sDao.searchByStudentId(studentId));
                }
                if (!teacherIdText.isEmpty()) {
                    int teacherId = Integer.parseInt(teacherIdText);
                    results.addAll(sDao.searchByTeacherId(teacherId));
                }
                if (!roomIdText.isEmpty()) {
                    int roomId = Integer.parseInt(roomIdText);
                    results.addAll(sDao.searchByRoomId(roomId));
                }

                if (results.isEmpty()) {
                    showAlert("Информация", "По вашему запросу ничего не найдено.");
                    tableView.setItems(FXCollections.observableArrayList());
                } else {
                    // Удаление дубликатов
                    results = new ArrayList<>(new HashSet<>(results));
                    ObservableList<Schedule> data = FXCollections.observableArrayList(results);
                    tableView.setItems(data);
                }
            } catch (NumberFormatException ex) {
                showAlert("Ошибка", "Пожалуйста, введите корректные числовые значения для ID.");
            } catch (SQLException ex) {
                showAlert("Ошибка", "Произошла ошибка при поиске: " + ex.getMessage());
            }
        });

        // -------------------- Layout Setup --------------------
        HBox root = new HBox(20, addBox, tableBox);
        root.setPadding(new Insets(10));

        Scene scene = new Scene(root, 1200, 600);
        scheduleStage.setScene(scene);
        scheduleStage.show();
    }

    // Метод для обновления таблицы после добавления расписания
    private void refreshScheduleTable(ScheduleDAO scheduleDAO, TableView<Schedule> tableView) {
        try {
            List<Schedule> schedules = scheduleDAO.getAllSchedules();
            ObservableList<Schedule> data = FXCollections.observableArrayList(schedules);
            tableView.setItems(data);
        } catch (SQLException ex) {
            showAlert("Ошибка", "Не удалось обновить таблицу расписаний: " + ex.getMessage());
        }
    }

    private int convertDayToNumber(String day) {
        return switch (day) {
            case "Monday" -> 1;
            case "Tuesday" -> 2;
            case "Wednesday" -> 3;
            case "Thursday" -> 4;
            case "Friday" -> 5;
            case "Saturday" -> 6;
            case "Sunday" -> 7;
            default -> 0;
        };
    }

    private String convertNumberToDay(int number) {
        return switch (number) {
            case 1 -> "Monday";
            case 2 -> "Tuesday";
            case 3 -> "Wednesday";
            case 4 -> "Thursday";
            case 5 -> "Friday";
            case 6 -> "Saturday";
            case 7 -> "Sunday";
            default -> "Unknown";
        };
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }

}