package ru.stepup.restTests;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import ru.stepup.BaseTest;
import ru.stepup.mockClasses.Student;
import ru.stepup.supportMethods.CheckEqualsStudent;

import static io.restassured.RestAssured.given;

public class getStudentTest extends BaseTest {
    Student student = new Student((int) (Math.random() * 1000), "Ivan", 2, 3);

    @BeforeEach
    void createStudent() {
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

    @AfterEach
    void deleteStudent() {
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
    @Order(1)
    void getErrorNotFoundStudentFailTest() {
        given()
                .pathParam("id", 0)
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .get("/student/{id}")
                .then()
                .log().all()
                .header("Content-Length", "0")
                .statusCode(404);
    }

    @Test
    @Order(2)
    void getStudentSuccessTest() {
        Student extractStudent = given()
                .pathParam("id", student.getId())
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .get("/student/{id}")
                .then()
                .log().all()
                .contentType(ContentType.JSON)
                .statusCode(200)
                .extract().as(Student.class);

        CheckEqualsStudent.check(extractStudent, student);
    }
}
