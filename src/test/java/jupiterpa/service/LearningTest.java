package jupiterpa.service;

import jupiterpa.model.PlayerCharacter;
import jupiterpa.model.PlayerCharacterEntity;
import jupiterpa.model.Skill;
import jupiterpa.util.TestCreation;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class LearningTest {

    SettingsService settings;
    UtilityService utility;
    CalculationService calculation;
    LearningService learning;

    PlayerCharacter setup() throws Exception {
        settings = new SettingsServiceImpl();
        utility = new UtilityServiceImpl();
        calculation = new CalculationServiceImpl(settings,utility);
        learning = new LearningServiceImpl(settings, utility, calculation);

        PlayerCharacterEntity ce = TestCreation.create();
        return calculation.enrich( ce );
    }

    @Test
    public void increase() throws Exception {

        // Prepare
        PlayerCharacter ch = setup();
        ch.setTotalEp(200);
        ch.setNotSpentEp(200);
        ch.setGold(1000);

        // Process
        learning.learn(ch,"Klettern",20);

        // Check Skill
        Skill klettern = utility.findSkill(ch.getSkills(),"Klettern");

        assertThat( klettern.getBonus(), is(14));
        assertThat( klettern.getCostEP(), is( 200));
        assertThat( klettern.getCostGold(), is(20));

        // Check Character
        assertThat( ch.getNotSpentEp(), is(100));
        assertThat( ch.getTotalEp(), is(200));
        assertThat( ch.getGold(), is(980));
    }

    @Test
    public void increase_with_practice_and_gold() throws Exception {
        // Prepare
        PlayerCharacter ch = setup();
        ch.setTotalEp(200);
        ch.setNotSpentEp(200);
        ch.setGold(1000);

        // Process (100 gold - 10 EP)
        learning.learn(ch,"Klettern",120);

        // Check Skill
        Skill klettern = utility.findSkill(ch.getSkills(),"Klettern");

        assertThat( klettern.getBonus(), is(14));
        assertThat( klettern.getCostEP(), is( 200));
        assertThat( klettern.getCostGold(), is(20));

        // Check Character
        assertThat( ch.getNotSpentEp(), is(110));
        assertThat( ch.getTotalEp(), is(200));
        assertThat( ch.getGold(), is(880));
    }

    @Test
    public void learn() throws Exception {
        // Prepare
        PlayerCharacter ch = setup();
        ch.setTotalEp(60);
        ch.setNotSpentEp(60);
        ch.setGold(200);

        // Process (minimal Gold)
        learning.learn(ch,"Reiten",200);

        // Check Skill
        Skill reiten = utility.findSkill(ch.getSkills(),"Reiten");

        assertThat( reiten.getBonus(), is(13));
        assertThat( reiten.getCostEP(), is( 100));
        assertThat( reiten.getCostGold(), is(20));

        // Check Character
        assertThat( ch.getNotSpentEp(), is(0));
        assertThat( ch.getTotalEp(), is(60));
        assertThat( ch.getGold(), is(0));
    }

    @Test
    public void learn_with_gold() throws Exception {
        // Prepare
        PlayerCharacter ch = setup();
        ch.setTotalEp(260);
        ch.setNotSpentEp(260);
        ch.setGold(2200);

        // Process (minimal Gold)
        learning.learn(ch,"Reiten",300); //100 gold --> EP

        // Check Skill
        Skill reiten = utility.findSkill(ch.getSkills(),"Reiten");

        assertThat( reiten.getBonus(), is(13));
        assertThat( reiten.getCostEP(), is( 100));
        assertThat( reiten.getCostGold(), is(20));

        // Check Character
        assertThat( ch.getNotSpentEp(), is(210));
        assertThat( ch.getTotalEp(), is(260));
        assertThat( ch.getGold(), is(1900));
    }
}

