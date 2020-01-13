package jupiterpa.service;

import jupiterpa.model.PlayerCharacter;
import jupiterpa.model.PlayerCharacterEntity;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

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
        PlayerCharacterEntity entity = TestCreation.create();

        PlayerCharacter act = service.enrich(entity);
        act.setSkills(new ArrayList<>());

        PlayerCharacter exp = TestCreation.enriched(act.getId());

        assertThat( act, equalTo(exp));
    }
}
