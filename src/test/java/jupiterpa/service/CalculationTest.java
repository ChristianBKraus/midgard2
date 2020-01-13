package jupiterpa.service;

import jupiterpa.model.PlayerCharacter;
import jupiterpa.model.PlayerCharacterEntity;
import jupiterpa.model.Skill;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class CalculationTest {

    CalculationService service;
    UtilityService utility;

    @Before
    public void injet() throws IOException, URISyntaxException {
        SettingsService settings = new SettingsServiceImpl();
        utility = new UtilityServiceImpl();
        service = new CalculationServiceImpl(settings,utility);
    }


    @Test
    public void test() throws Exception {
        // Test Input
        PlayerCharacterEntity entity = TestCreation.create();

        // Process
        PlayerCharacter act = service.enrich(entity);

        // Split away skills
        List<Skill> skills = act.getSkills();
        act.setSkills(new ArrayList<>());

        // Get expected and check
        PlayerCharacter exp = TestCreation.getExpectedCharacter(act.getId());
        assertThat( act, equalTo(exp));

        // Get expected skill and check
        assertThat( skills.size(), is(2));
        Skill klettern = utility.findSkill(skills,"Klettern");
        assertThat( klettern, is( TestCreation.getExpectedSkill("Klettern", act.getId())));
    }
}
