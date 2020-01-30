package jupiterpa.service;

import jupiterpa.model.*;
import jupiterpa.util.TestCreation;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class LearningTest {

    SettingsService settings;
    UtilityService utility;
    CalculationService calculation;
    LearningService learning;

    PlayerCharacter setup() throws UserException {
        settings = new SettingsServiceImpl();
        utility = new UtilityServiceImpl();
        calculation = new CalculationServiceImpl(settings,utility);
        learning = new LearningServiceImpl(settings, utility, calculation);

        PlayerCharacter ce = TestCreation.create();
        return calculation.enrich( ce );
    }

    @Test
    public void learn() throws UserException {

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
    public void learn_with_practice_and_gold() throws UserException {
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
    public void train() throws UserException {
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
    public void train_with_gold() throws UserException {
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

    @Test
    public void improve() throws UserException {
        // Prepare
        PlayerCharacter ch = setup();
        ch.setTotalEp(10);
        ch.setNotSpentEp(5);
        ch.setGold(100);

        // Process (no Level Up)
        learning.improve(ch,new Improve(ch.getName(),11,111));

        assertThat( ch.getNotSpentEp(), is(16) );
        assertThat( ch.getTotalEp(), is(21) );
        assertThat( ch.getGold(), is(211) );
        assertThat( ch.getLevel(), is(1) );
        assertThat( ch.getSpentLevel(), is(1) );

        // Process (incl. Level Up)
        learning.improve(ch,new Improve(ch.getName(),100,0));

        assertThat( ch.getNotSpentEp(), is(116) );
        assertThat( ch.getTotalEp(), is(121) );
        assertThat( ch.getGold(), is(211) );
        assertThat( ch.getLevel(), is(2) );
        assertThat( ch.getSpentLevel(), is(1) );
    }

    @Test
    public void levelUp() throws UserException {
        // Prepare
        PlayerCharacter ch = setup();
        ch.setTotalEp(0);
        ch.setNotSpentEp(0);
        ch.setGold(100);

        // Process (incl. Level Up)
        learning.improve(ch,new Improve(ch.getName(),120,0));

        assertThat( ch.getNotSpentEp(), is(120) );
        assertThat( ch.getTotalEp(), is(120) );
        assertThat( ch.getGold(), is(100) );
        assertThat( ch.getLevel(), is(2) );
        assertThat( ch.getSpentLevel(), is(1) );
        assertThat( ch.getApWurf(), is(2) );

        // LevelUp (without Attribute Increase)
        learning.levelUp(ch, new LevelUp(ch.getName(),2,"",0));

        assertThat( ch.getNotSpentEp(), is(120) );
        assertThat( ch.getTotalEp(), is(120) );
        assertThat( ch.getGold(), is(100) );
        assertThat( ch.getLevel(), is(2) );
        assertThat( ch.getSpentLevel(), is(2) );
        assertThat( ch.getApWurf(), is(4) );

    }

    @Test
    public void improve_zwerg() throws UserException {
        // Prepare
        PlayerCharacter ch = setup();
        ch.setRace("Zwerg");
        ch.setTotalEp(0);
        ch.setNotSpentEp(0);
        ch.setGold(0);

        // Process (no Level Up because of Gold)
        learning.improve(ch,new Improve(ch.getName(),200,0));

        assertThat( ch.getGold(), is(0) );
        assertThat( ch.getLevel(), is(1) );
        assertThat( ch.getSpentLevel(), is(1) );

        // Process (incl. Level Up because of gold)
        learning.improve(ch,new Improve(ch.getName(),0,1000));

        assertThat( ch.getGold(), is(1000) );
        assertThat( ch.getLevel(), is(2) );
        assertThat( ch.getSpentLevel(), is(1) );

    }

}

