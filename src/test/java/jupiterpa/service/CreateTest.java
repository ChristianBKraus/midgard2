package jupiterpa.service;

import jupiterpa.model.Character;
import jupiterpa.model.Skill;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
public class CreateTest {

    CreateServiceImpl service = new CreateServiceImpl();
    Character character;

    @BeforeEach
    public void setup() {
        Character c = new Character();
        c.setName("Name");
        c.setClassName("Krieger");


        Skill s = new Skill();
        s.setName("Klettern");
        s.setLevel(7);
        s.setBaseAttribute("Gs");

        List<Skill> skills = new ArrayList<>();
        skills.add(s);
        c.setSkills(skills);

        character = c;
    }

    @Test
    public void test() throws Exception {
        service.create(character);
    }
}
