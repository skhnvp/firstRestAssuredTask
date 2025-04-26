package ru.stepup.supportMethods;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.path.json.JsonPath;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import ru.stepup.mockClasses.Student;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CheckEqualsStudent {
    @SneakyThrows
    public static void check(Object gotStudentFromRest, Student expectedStudent) {
        ObjectMapper mapper = new ObjectMapper();

        JsonPath jp = new JsonPath(mapper.writeValueAsString(gotStudentFromRest));
        Assertions.assertAll("Got student checking",
                () -> assertEquals(expectedStudent.getId(), jp.getInt("id")),
                () -> assertEquals(expectedStudent.getName(), jp.getString("name")),
                () -> assertEquals(expectedStudent.getMarks(), jp.getList("marks")));
    }
}
