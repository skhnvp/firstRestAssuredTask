package ru.stepup.restTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import ru.stepup.mockClasses.Student;

import static io.restassured.RestAssured.given;

public class deleteStudentTest {
    static Student student = new Student((int) (Math.random() * 1000), "Ivan", 2, 3);

    static ObjectMapper mapper = new ObjectMapper();

    @SneakyThrows
    @BeforeAll
    static void createStudent() {
        given()
                .contentType(ContentType.JSON)
                .body(mapper.writeValueAsString(student))
                .log().all()
                .when()
                .post("/student")
                .then()
                .log().all()
                .statusCode(201);
    }

    @SneakyThrows
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

    @SneakyThrows
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
