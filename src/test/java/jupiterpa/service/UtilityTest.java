package jupiterpa.service;

import jupiterpa.model.Skill;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class UtilityTest {
    UtilityServiceImpl service = new UtilityServiceImpl();

    List<Skill> skills;
    Skill a;
    Skill b;

    @Before
    public void setup() {
        skills = new ArrayList<>();
        a = new Skill();
        a.setName("A");
        a.setBonus(1);
        b = new Skill();
        b.setName("B");
        b.setBonus(2);
        skills.add(a);
        skills.add(b);
    }

    @Test
    public void find() throws Exception {
        Skill fa = service.findSkill(skills,"A");
        assertThat( fa, equalTo(a));
        Skill fb = service.findSkill(skills,"B");
        assertThat( fb, equalTo(b));
    }
    @Test(expected = Exception.class)
    public void findFalse() throws Exception {
        service.findSkill(skills,"C");
    }
    @Test
    public void exist() {
        assertThat( service.existSkill(skills,"A"), is(true));
        assertThat( service.existSkill(skills,"B"), is(true));
        assertThat( service.existSkill(skills,"C"), is(false));
    }
}
