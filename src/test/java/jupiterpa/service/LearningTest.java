package jupiterpa.service;

import jupiterpa.model.Cost;
import jupiterpa.model.PlayerCharacter;
import jupiterpa.model.PlayerCharacterEntity;
import jupiterpa.model.Skill;
import org.junit.Test;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class LearningTest {

    SettingsService settings;
    UtilityService utility;
    CalculationService calculation;
    LearningService learning;

    void setup() throws IOException, URISyntaxException {
        settings = new SettingsServiceImpl();
        utility = new UtilityServiceImpl();
        calculation = new CalculationServiceImpl(settings,utility);
        learning = new LearningServiceImpl(settings, utility, calculation);
    }

    @Test
    public void increase() throws Exception {
        setup();

        // Prepare
        PlayerCharacterEntity ce = TestCreation.create();
        PlayerCharacter ch = calculation.enrich( ce );

        ch.setTotalEp(200);
        ch.setNotSpentEp(200);

        Skill ori = utility.findSkill(ch.getSkills(),"Klettern");
        int ep = ori.getCostEP();
        int gold = ori.getCostGold();

        // Process
        learning.learn(ch,"Klettern",20);

        // Check Skill
        Skill klettern = utility.findSkill(ch.getSkills(),"Klettern");

        assertThat( klettern.getBonus(), is(14));
        assertThat( klettern.getCostEP(), is( ep *2));
        assertThat( klettern.getCostGold(), is(gold));

        // Check Character
        assertThat( ch.getNotSpentEp(), is(100));
        assertThat( ch.getTotalEp(), is(200));
        // Gold??? TODO
    }
}

