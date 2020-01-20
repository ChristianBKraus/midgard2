package jupiterpa.integration;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
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

    @WithMockUser()
    @Test
    public void getCharacters() throws Exception {
        // given
        PlayerCharacterEntity ch = TestCreation.create();
        ch.setUser("user");
        repo.save(ch);

        // when
        Optional<PlayerCharacterEntity> found =
                repo.findByName("Name");

        // then (Repository)
        assertThat(found.isPresent(),is(true));

        // then Web
        mvc.perform(get("/api/character")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$[0].name").value(ch.getName()));

    }

    @WithMockUser()
    @Test
    public void getCharacterByName() throws Exception {
        // given
        PlayerCharacterEntity ch = TestCreation.create();
        ch.setUser("user");
        repo.save(ch);

        // when
        Optional<PlayerCharacterEntity> found =
                repo.findByName("Name");

        // then Repository
        assertThat(found.isPresent(),is(true));
        PlayerCharacterEntity entity = found.orElse(null);
        assert entity != null;

        // then web
        mvc.perform(get("/api/character/Name")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.name").value(entity.getName()))
                .andExpect(jsonPath("$.st").value(entity.getSt()));

    }

    @WithMockUser()
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
                .andExpect(jsonPath("$.name").value(entity.getName()))
                .andExpect(jsonPath("$.st").value(entity.getSt()))
                .andExpect(jsonPath("$.skills[0].name").value("Klettern")
                );

    }

    @WithMockUser( value = "admin")
    @Test
    public void adminsCanGetForeignCharacters() throws Exception {
        // given
        PlayerCharacterEntity ch = TestCreation.create();
        ch.setUser("user");
        repo.save(ch);

        // when
        Optional<PlayerCharacterEntity> found =
                repo.findByName("Name");

        // then (Repository)
        assertThat(found.isPresent(),is(true));
        PlayerCharacterEntity entity = found.orElse(null);
        assert entity != null;

        // then Web
        mvc.perform(get("/api/character")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$[0].name").value(ch.getName()));

    }

    @WithMockUser()
    @Test
    public void usersCannotGetForeignCharacters() throws Exception {
        // given
        PlayerCharacterEntity ch = TestCreation.create();
        ch.setUser("admin");
        repo.save(ch);

        // when
        Optional<PlayerCharacterEntity> found =
                repo.findByName("Name");

        // then (Repository)
        assertThat(found.isPresent(),is(true));
        PlayerCharacterEntity entity = found.orElse(null);
        assert entity != null;
        assertThat(entity,is(ch));

        // then Web
        mvc.perform(get("/api/character")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(content().string("[]"));

    }
}
