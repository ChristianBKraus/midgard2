package jupiterpa.service;

import jupiterpa.model.PlayerCharacter;
import jupiterpa.model.PlayerCharacterEntity;
import jupiterpa.model.Skill;
import jupiterpa.model.SkillEntity;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class CalculationTest {

    CalculationService service;

    @Before
    public void injet() throws IOException, URISyntaxException {
        SettingsService settings = new SettingsServiceImpl();
        UtilityService utility = new UtilityServiceImpl();
        service = new CalculationServiceImpl(settings,utility);
    }


    @Test
    public void test() throws Exception {
        PlayerCharacterEntity c = TestCreation.create();
        service.enrich(c);
    }
}
