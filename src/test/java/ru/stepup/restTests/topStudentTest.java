package ru.stepup.restTests;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import ru.stepup.mockClasses.Student;
import ru.stepup.supportMethods.CheckEqualsStudent;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;

public class topStudentTest {
    @Test
    @Order(1)
    void getTopStudentsWithoutStudentsSuccessTest() {
        given()
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .get("/topStudent")
                .then()
                .log().all()
                .header("Content-Length", "0")
                .statusCode(200);
    }

    @Test
    @Order(2)
    void getTopStudentsWithoutMarksSuccessTest() {
        Student student = new Student(1, "Steve");

        given()
                .contentType(ContentType.JSON)
                .body(student)
                .log().all()
                .when()
                .post("/student")
                .then()
                .log().all()
                .statusCode(201);

        try {
            given()
                    .contentType(ContentType.JSON)
                    .log().all()
                    .when()
                    .get("/topStudent")
                    .then()
                    .log().all()
                    .header("Content-Length", "0")
                    .statusCode(200);
        } finally {
            given()
                    .pathParam("id", student.getId())
                    .contentType(ContentType.JSON)
                    .log().all()
                    .when()
                    .delete("/student/{id}")
                    .then()
                    .log().all()
                    .statusCode(200);
        }
    }

    @Test
    @Order(3)
    void getTopStudentOneStudentSuccessTest() {
        Student student = new Student(1, "Steve", 4, 3);

        given()
                .contentType(ContentType.JSON)
                .body(student)
                .log().all()
                .when()
                .post("/student")
                .then()
                .log().all()
                .statusCode(201);

        try {
            ArrayList students = given()
                    .contentType(ContentType.JSON)
                    .log().all()
                    .when()
                    .get("/topStudent")
                    .then()
                    .log().all()
                    .contentType(ContentType.JSON)
                    .statusCode(200)
                    .extract().as(ArrayList.class);

            CheckEqualsStudent.check(students.get(0), student);
        } finally {
            given()
                    .pathParam("id", student.getId())
                    .contentType(ContentType.JSON)
                    .log().all()
                    .when()
                    .delete("/student/{id}")
                    .then()
                    .log().all()
                    .statusCode(200);
        }
    }

    @Test
    @Order(4)
    void getTopStudentMoreThanOneStudentSuccessTest() {
        Student studentMinAvgMarks = new Student(1, "Steve", 2, 3);
        Student studentMaxAvgMarks = new Student(2, "Ben", 4, 5);

        given()
                .contentType(ContentType.JSON)
                .body(studentMinAvgMarks)
                .log().all()
                .when()
                .post("/student")
                .then()
                .log().all()
                .statusCode(201);

        given()
                .contentType(ContentType.JSON)
                .body(studentMaxAvgMarks)
                .log().all()
                .when()
                .post("/student")
                .then()
                .log().all()
                .statusCode(201);

        try {
            ArrayList students = given()
                    .contentType(ContentType.JSON)
                    .log().all()
                    .when()
                    .get("/topStudent")
                    .then()
                    .log().all()
                    .contentType(ContentType.JSON)
                    .statusCode(200)
                    .extract().as(ArrayList.class);

            CheckEqualsStudent.check(students.get(0), studentMaxAvgMarks);

        } finally {
            given()
                    .pathParam("id", studentMinAvgMarks.getId())
                    .contentType(ContentType.JSON)
                    .log().all()
                    .when()
                    .delete("/student/{id}")
                    .then()
                    .log().all()
                    .statusCode(200);
            given()
                    .pathParam("id", studentMaxAvgMarks.getId())
                    .contentType(ContentType.JSON)
                    .log().all()
                    .when()
                    .delete("/student/{id}")
                    .then()
                    .log().all()
                    .statusCode(200);
        }
    }

    @Test
    @Order(5)
    void getTopStudentSameMarksStudentSuccessTest() {
        Student firstStudent = new Student(1, "Steve", 4, 5);
        Student secondStudent = new Student(2, "Ben", 4, 5);

        given()
                .contentType(ContentType.JSON)
                .body(firstStudent)
                .log().all()
                .when()
                .post("/student")
                .then()
                .log().all()
                .statusCode(201);

        given()
                .contentType(ContentType.JSON)
                .body(secondStudent)
                .log().all()
                .when()
                .post("/student")
                .then()
                .log().all()
                .statusCode(201);

        try {
            ArrayList students = given()
                    .contentType(ContentType.JSON)
                    .log().all()
                    .when()
                    .get("/topStudent")
                    .then()
                    .log().all()
                    .contentType(ContentType.JSON)
                    .statusCode(200)
                    .extract().as(ArrayList.class);

            Assertions.assertEquals(2, students.size());

            CheckEqualsStudent.check(students.get(0), firstStudent);
            CheckEqualsStudent.check(students.get(1), secondStudent);

        } finally {
            given()
                    .pathParam("id", firstStudent.getId())
                    .contentType(ContentType.JSON)
                    .log().all()
                    .when()
                    .delete("/student/{id}")
                    .then()
                    .log().all()
                    .statusCode(200);
            given()
                    .pathParam("id", secondStudent.getId())
                    .contentType(ContentType.JSON)
                    .log().all()
                    .when()
                    .delete("/student/{id}")
                    .then()
                    .log().all()
                    .statusCode(200);
        }
    }

    @Test
    @Order(6)
    void getTopStudentMaxCountOfMarksStudentSuccessTest() {
        Student firstStudent = new Student(1, "Steve", 3, 5);
        Student secondStudent = new Student(2, "Ben", 4, 4, 4);

        given()
                .contentType(ContentType.JSON)
                .body(firstStudent)
                .log().all()
                .when()
                .post("/student")
                .then()
                .log().all()
                .statusCode(201);

        given()
                .contentType(ContentType.JSON)
                .body(secondStudent)
                .log().all()
                .when()
                .post("/student")
                .then()
                .log().all()
                .statusCode(201);

        try {
            ArrayList students = given()
                    .contentType(ContentType.JSON)
                    .log().all()
                    .when()
                    .get("/topStudent")
                    .then()
                    .log().all()
                    .contentType(ContentType.JSON)
                    .statusCode(200)
                    .extract().as(ArrayList.class);

            Assertions.assertEquals(1, students.size());
            CheckEqualsStudent.check(students.get(0), secondStudent);
        } finally {
            given()
                    .pathParam("id", firstStudent.getId())
                    .contentType(ContentType.JSON)
                    .log().all()
                    .when()
                    .delete("/student/{id}")
                    .then()
                    .log().all()
                    .statusCode(200);
            given()
                    .pathParam("id", secondStudent.getId())
                    .contentType(ContentType.JSON)
                    .log().all()
                    .when()
                    .delete("/student/{id}")
                    .then()
                    .log().all()
                    .statusCode(200);
        }
    }
}
