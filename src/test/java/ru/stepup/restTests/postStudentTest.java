package ru.stepup.restTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import ru.stepup.mockClasses.Student;

import java.util.List;

import static io.restassured.RestAssured.given;

public class postStudentTest {
    static Student student = new Student((int) (Math.random() * 1000), "Ivan", 2, 3);
    static Student studentWithoutId = new Student("Pete", 4, 5);

    static Student updateStudent = new Student(999, "Pete", 2, 3);

    static ObjectMapper mapper = new ObjectMapper();

//    @Test
//    void createAndSaveIdStudentSuccessTest() {
//        String idCreatedStudent = given()
//                .contentType(ContentType.JSON)
//                .body(student)
//                .log().all()
//                .when()
//                .post("/student")
//                .then()
//                .log().all()
//                .statusCode(201)
//                .extract()
//                .response()
//                .asString();
//        System.out.println("++=====" + idCreatedStudent);
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
                .queryParam("name", studentWithoutId.getName())
                //.pathParam("name", studentWithoutId.getName())
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .get("/student")
                //.get("/student?name={name}")
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

        given()
                .pathParam("id", updateStudent.getId())
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
    void createStudentSuccessTest() {
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
    @Order(2)
    void UpdateStudentSuccessTest() {

        given()
                .contentType(ContentType.JSON)
                .body(student)
                .log().all()
                .when()
                .post("/student")
                .then()
                .log().all()
                .statusCode(201);

        student.setName("Pete");
        student.setMarks(List.of(5, 4, 3, 2));

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
    @Order(3)
    void createStudentWithoutIdSuccessTest() {
        Integer i = given()
                .contentType(ContentType.JSON)
                .body(studentWithoutId)
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

    @Test
    @Order(4)
    void createStudentWithoutNameFailTest() {
        Student studentWithoutName = new Student();
        studentWithoutName.setId((int) (Math.random() * 1000));
        studentWithoutName.setMarks(List.of(3, 4));

        given()
                .contentType(ContentType.JSON)
                .body(studentWithoutName)
                .log().all()
                .when()
                .post("/student")
                .then()
                .log().all()
                .statusCode(400);
    }

    @Test
    @Order(5)
    void UpdateStudentWithoutNameSuccessTest() {
        given()
                .contentType(ContentType.JSON)
                .body(updateStudent)
                .log().all()
                .when()
                .post("/student")
                .then()
                .log().all()
                .statusCode(201);

        updateStudent.setName(null);

        given()
                .contentType(ContentType.JSON)
                .body(updateStudent)
                .log().all()
                .when()
                .post("/student")
                .then()
                .log().all()
                .statusCode(404);

    }
}
