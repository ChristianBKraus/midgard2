package jupiterpa.integration;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;


import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jupiterpa.Application;
import jupiterpa.model.PlayerCharacterEntity;
import jupiterpa.repository.CharacterRepository;
import jupiterpa.util.TestCreation;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class IntegrationTest {

    @Autowired
    CharacterRepository repo;

    @Autowired
    MockMvc mvc;

    @Before
    public void setup() {
        repo.deleteAll();
    }

    @Test
    public void getCharacters() throws Exception {
        // given
        PlayerCharacterEntity ch = TestCreation.create();
        repo.save(ch);

        // when
        List<PlayerCharacterEntity> found =
                repo.findByName("Name");

        // then (Repository)
        assertThat(found.size(),is(1));
        PlayerCharacterEntity entity = found.get(0);
        assertThat(entity,is(ch));

        // then Web
        mvc.perform(get("/api/character")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$[0].name").value(ch.getName()));

    }

    @Test
    public void getCharacterByName() throws Exception {
        // given
        PlayerCharacterEntity ch = TestCreation.create();
        repo.save(ch);

        // when
        List<PlayerCharacterEntity> found =
                repo.findByName("Name");

        // then Repository
        assertThat(found.size(),is(1));
        PlayerCharacterEntity entity = found.get(0);
        assertThat(entity,is(ch));

        // then web
        mvc.perform(get("/api/character/Name")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$[0].name").value(entity.getName()))
                .andExpect(jsonPath("$[0].st").value(entity.getSt()));

    }

    @Test
    public void postCharacter() throws Exception {

        PlayerCharacterEntity entity = TestCreation.create();
        ObjectMapper mapper = new ObjectMapper();
        String json =mapper.writeValueAsString(entity);

        // POST
        mvc.perform(
                post("/api/character")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()
                );

        // GET
        mvc.perform(
                get("/api/character/"+ entity.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$[0].name").value(entity.getName()))
                .andExpect(jsonPath("$[0].st").value(entity.getSt()))
                .andExpect(jsonPath("$[0].skills[0].name").value("Klettern")
                );

    }
}
