package ru.stepup.restTests;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import ru.stepup.mockClasses.Student;

import static io.restassured.RestAssured.given;

public class deleteStudentTest {
    static Student student = new Student((int) (Math.random() * 1000), "Ivan", 2, 3);

    @BeforeAll
    static void createStudent() {
        given()
                .contentType(ContentType.JSON)
                .body(student)
                .log().all()
                .when()
                .post("/student")
                .then()
                .log().all()
                .statusCode(201);
    }

    @Test
    @Order(1)
    void deleteStudentSuccessTest() {
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

    @Test
    @Order(2)
    void deleteStudentFailTest() {
        given()
                .pathParam("id", 0)
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .delete("/student/{id}")
                .then()
                .log().all()
                .statusCode(404);
    }


}
