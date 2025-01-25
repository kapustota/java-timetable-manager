-- Отключение проверки внешних ключей временно
SET FOREIGN_KEY_CHECKS = 0;

-- Заполнение таблицы department
INSERT INTO department (id, name) VALUES
(1, 'Computer Science'),
(2, 'Mathematics'),
(3, 'Physics');

-- Заполнение таблицы teacher
INSERT INTO teacher (id, first_name, last_name, email, phone_number, department_id) VALUES
(1, 'Alice', 'Smith', 'alice.smith@example.com', '1234567890', 1),
(2, 'Bob', 'Johnson', 'bob.johnson@example.com', '0987654321', 2),
(3, 'Charlie', 'Williams', 'charlie.williams@example.com', '1122334455', 3);

-- Назначение начальников кафедр
UPDATE department SET chief_id = 1 WHERE id = 1;
UPDATE department SET chief_id = 2 WHERE id = 2;
UPDATE department SET chief_id = 3 WHERE id = 3;

-- Заполнение таблицы course
INSERT INTO course (id, name, description, credits) VALUES
(1, 'Introduction to Programming', 'Learn the basics of programming using Python.', 4),
(2, 'Linear Algebra', 'Study vector spaces, linear transformations, and matrices.', 3),
(3, 'Quantum Mechanics', 'Explore the fundamentals of quantum mechanics.', 4);

-- Заполнение таблицы lesson
INSERT INTO lesson (lesson_number, start_time, end_time) VALUES
(1, '09:00:00', '09:50:00'),
(2, '10:00:00', '10:50:00'),
(3, '11:00:00', '11:50:00'),
(4, '12:00:00', '12:50:00'),
(5, '13:00:00', '13:50:00');

-- Заполнение таблицы schedule
INSERT INTO schedule (id, course_id, teacher_id, day_number, lesson_id, room_number) VALUES
(1, 1, 1, 1, 1, 101),
(2, 1, 1, 3, 2, 101),
(3, 2, 2, 2, 1, 202),
(4, 2, 2, 4, 2, 202),
(5, 3, 3, 3, 3, 303),
(6, 3, 3, 5, 4, 303);

-- Заполнение таблицы student
INSERT INTO student (id, first_name, last_name, email, phone_number, enrollment_date) VALUES
(1, 'John', 'Doe', 'john.doe@example.com', '1231231234', '2024-01-01'),
(2, 'Jane', 'Smith', 'jane.smith@example.com', '3213214321', '2024-02-01'),
(3, 'Alice', 'Brown', 'alice.brown@example.com', '2342342345', '2024-03-01');

-- Заполнение таблицы enrollment
INSERT INTO enrollment (id, student_id, course_id, date, final_grade) VALUES
(1, 1, 1, '2024-01-10', 85.50),
(2, 2, 1, '2024-01-15', 90.00),
(3, 1, 2, '2024-02-01', 88.00),
(4, 3, 3, '2024-03-01', 92.00);

-- Включение проверки внешних ключей обратно
SET FOREIGN_KEY_CHECKS = 1;
