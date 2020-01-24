package jupiterpa.service;

import jupiterpa.model.PlayerCharacter;
import jupiterpa.model.Skill;
import jupiterpa.util.TestCreation;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class CalculationTest {

    CalculationService service;
    UtilityService utility;

    @Before
    public void injet() throws UserException {
        SettingsService settings = new SettingsServiceImpl();
        utility = new UtilityServiceImpl();
        service = new CalculationServiceImpl(settings,utility);
    }


    @Test
    public void test() {
        // Test Input
        PlayerCharacter entity = TestCreation.create();

        // Process
        PlayerCharacter act = service.enrich(entity);

        // Split away skills
        List<Skill> skills = act.getSkills();
        act.setSkills(new ArrayList<>());

        // Get expected and check
        assertThat( act, equalTo(TestCreation.getExpectedCharacter()));

        // Get expected skill and check
        assertThat( skills.size(), is(2));

        Skill klettern = utility.findSkill(skills,"Klettern");
        assertThat( klettern, is( TestCreation.getExpectedSkill("Klettern")));

        Skill reiten = utility.findSkill(skills,"Reiten");
        assertThat( reiten, is( TestCreation.getExpectedSkill("Reiten")));
    }

    @Test
    public void test_elf() {
        // Test Input
        PlayerCharacter entity = TestCreation.create();
        entity.setRace("Elf");

        // Process
        PlayerCharacter act = service.enrich(entity);

        Skill klettern = utility.findSkill(act.getSkills(),"Klettern");
        assertThat( klettern.getCostEP(), is(TestCreation.getExpectedSkill("Klettern").getCostEP()));

        Skill reiten = utility.findSkill(act.getSkills(),"Reiten");
        assertThat( reiten.getCostEP(), is( TestCreation.getExpectedSkill("Reiten").getCostEP() + 6));
    }
}
