package app.microservice.employee.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Test
    @Sql("/sql/employees.sql")
    void findEmployee_ReturnsEmployeesList() throws Exception {
        //given
        var requestBuilder = MockMvcRequestBuilders.get("/api/v1/employees")
                .accept(MediaType.APPLICATION_JSON);
        //when
        this.mockMvc.perform(requestBuilder)
                //then
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        content().json("""
                                [
                                {"id": 1, "fullName": "John Doe", "phone": "70952756596"},
                                {"id": 2, "fullName": "Jane Doe", "phone": "70952756595"}
                                ]""")
                );
    }

    @Test
    void createEmployee_RequestIsValid_ReturnsNewEmployee() throws Exception {
        //given
        var requestBuilder = MockMvcRequestBuilders.post("/api/v1/employees")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"fullName": "John Doe", "phone": "70952756596"}
                        """);

        //when
        this.mockMvc.perform(requestBuilder)
                //then
                .andDo(print())
                .andExpectAll(
                        status().isCreated(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        content().json("""
                                {"id": 1, "fullName": "John Doe", "phone": "70952756596"}
                                """)
                );

    }

    @Test
    void createEmployee_RequestIsInvalid_ReturnsProblemDetail() throws Exception {
        //given
        var requestBuilder = MockMvcRequestBuilders.post("/api/v1/employees")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"fullName": "", "phone": null}
                        """)
                .locale(Locale.of("ru", "RU"));

        //when
        this.mockMvc.perform(requestBuilder)
                //then
                .andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON),
                        content().json("""
                                {
                                        "errors": [
                                               "ФИО не может быть меньше 3 и больше 100 символов.",
                                               "не должно равняться null"
                                        ]
                                }
                                """)
                );

    }
}
