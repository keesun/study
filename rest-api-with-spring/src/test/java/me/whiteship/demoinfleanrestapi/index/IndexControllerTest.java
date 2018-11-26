package me.whiteship.demoinfleanrestapi.index;

import me.whiteship.demoinfleanrestapi.common.BaseControllerTest;
import org.junit.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class IndexControllerTest extends BaseControllerTest {

    @Test
    public void index() throws Exception {
        this.mockMvc.perform(get("/api/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_links.events").exists());
    }

}
