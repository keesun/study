package com.example.demothymeleafweb;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest
public class SampleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testHello() throws Exception {


        this.mockMvc.perform(get("/s")
                    .param("q","aaa"))
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }

    @Test
    public void json() throws Exception {
        Person person = new Person();
        person.setName("keesun");
        person.setAge(-100);

        this.mockMvc.perform(post("/json")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(objectMapper.writeValueAsString(person)))
                .andDo(print())
                .andExpect(jsonPath("name").value("keesun"));
    }

    @Test
    public void redirect() throws Exception {
        this.mockMvc.perform(get("/redirect"))
                .andDo(print());
    }

}