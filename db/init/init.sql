-- Отключение проверки внешних ключей временно
SET FOREIGN_KEY_CHECKS = 0;

-- Создание таблицы department без внешнего ключа chief_id
CREATE TABLE department (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- Создание таблицы teacher без внешнего ключа department_id
CREATE TABLE teacher (
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone_number VARCHAR(20),
    department_id INT
);

-- Теперь можно добавить внешние ключи
ALTER TABLE department
    ADD COLUMN chief_id INT,
    ADD CONSTRAINT fk_department_chief
        FOREIGN KEY (chief_id) REFERENCES teacher(id);

ALTER TABLE teacher
    ADD CONSTRAINT fk_teacher_department
        FOREIGN KEY (department_id) REFERENCES department(id);


-- Включение проверки внешних ключей обратно
SET FOREIGN_KEY_CHECKS = 1;

-- Создание таблицы course
CREATE TABLE course (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    credits INT NOT NULL
);

-- Создание таблицы lesson_time (новая таблица)
CREATE TABLE lesson (
    lesson_number INT PRIMARY KEY,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL
);

-- Создание таблицы schedule
CREATE TABLE schedule (
    id INT AUTO_INCREMENT PRIMARY KEY,
    course_id INT NOT NULL,
    teacher_id INT NOT NULL,
    day_number TINYINT NOT NULL CHECK (day_number BETWEEN 1 AND 7),
    lesson_id INT NOT NULL,
    room_number INT NOT NULL,
    FOREIGN KEY (course_id) REFERENCES course(id),
    FOREIGN KEY (teacher_id) REFERENCES teacher(id),
    FOREIGN KEY (lesson_id) REFERENCES lesson(lesson_number)
);

-- Создание таблицы student
CREATE TABLE student (
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone_number VARCHAR(20),
    enrollment_date DATE NOT NULL
);

-- Создание таблицы enrollment
CREATE TABLE enrollment (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    course_id INT NOT NULL,
    date DATE NOT NULL,
    final_grade INT CHECK (final_grade BETWEEN 0 AND 100),
    FOREIGN KEY (student_id) REFERENCES student(id),
    FOREIGN KEY (course_id) REFERENCES course(id)
);