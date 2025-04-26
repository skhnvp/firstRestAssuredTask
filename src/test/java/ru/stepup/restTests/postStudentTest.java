package ru.stepup.restTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import ru.stepup.mockClasses.Student;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class postStudentTest {
    static Student student = new Student((int) (Math.random() * 1000), "Ivan", 2, 3);
    static Student studentWithoutId = new Student("Pete", 4, 5);

    static ObjectMapper mapper = new ObjectMapper();

//    @SneakyThrows
//    @Test
//    void createAndSaveIdStudentSuccessTest() {
//        Integer idCreatedStudent = given()
//                .contentType(ContentType.JSON)
//                .body(mapper.writeValueAsString(student))
//                .log().all()
//                .when()
//                .post("/student")
//                .then()
//                .log().all()
//                .statusCode(201)
//                .extract().as(Integer.class);
//        System.out.println(idCreatedStudent);
//    }

    @SneakyThrows
    @AfterAll
    static void deleteStudent() {
        given()
                .pathParam("id", student.getId())
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .delete("/student/{id}")
                .then()
                .log().all()
                .statusCode(200);

        Student idStudent = given()
                .pathParam("name", studentWithoutId.getName())
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .get("/student?name={name}")
                .then()
                .log().all()
                .statusCode(200)
                .extract().as(Student.class);

        JsonPath jp = new JsonPath(mapper.writeValueAsString(idStudent));

        given()
                .pathParam("id", jp.getInt("id"))
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
    @Order(1)
    void createStudentSuccessTest() {
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
    @Order(2)
    void UpdateStudentSuccessTest() {
        for (int i = 0; i < 2; i++) {
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
    }

    @SneakyThrows
    @Test
    @Order(3)
    void createStudentWithoutIdSuccessTest() {
        Integer i = given()
                .contentType(ContentType.JSON)
                .body(mapper.writeValueAsString(studentWithoutId))
                .log().all()
                .when()
                .post("/student")
                .then()
                .log().all()
                .contentType(ContentType.JSON)
                .statusCode(201)
                .extract().as(Integer.class);
        Assertions.assertNotNull(i);
    }

    @SneakyThrows
    @Test
    @Order(4)
    void createStudentWithoutNameFailTest() {
        Student studentWithoutName = new Student();
        studentWithoutName.setId((int) (Math.random() * 1000));
        studentWithoutName.setMarks(List.of(3, 4));

        given()
                .contentType(ContentType.JSON)
                .body(mapper.writeValueAsString(studentWithoutName))
                .log().all()
                .when()
                .post("/student")
                .then()
                .log().all()
                .statusCode(400);
    }

}
