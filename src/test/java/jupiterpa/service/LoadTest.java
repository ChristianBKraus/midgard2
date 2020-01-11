package jupiterpa.service;

import jupiterpa.model.CostsClass;
import jupiterpa.model.CostsMain;
import jupiterpa.model.CostsSkill;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;


import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class LoadTest {
    @Before
    public void setup() {

    }
    @Test
    public void test() throws IOException, URISyntaxException {
        SettingsServiceImpl settings = new SettingsServiceImpl();

        assertThat( settings.mainCosts.size(), greaterThanOrEqualTo(1) );
        assertThat( settings.classCosts.size(), greaterThanOrEqualTo(1 ) );
        assertThat( settings.skillCosts.size(), greaterThanOrEqualTo(1 ) );

        CostsMain mainCosts = settings.mainCosts.get("A/8");
        assertThat( mainCosts.getMultiplier(), is(10) );

        CostsSkill skillCosts = settings.skillCosts.get("Klettern");
        assertThat( skillCosts.getAttribute(), is("Gs") );


        CostsClass classCosts = settings.classCosts.get("Krieger/Freiland");
        assertThat( classCosts.getCost(), is(10) );
    }
}
