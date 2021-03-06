package jupiterpa.util;

import jupiterpa.model.PlayerCharacter;
import jupiterpa.model.Skill;

import java.util.ArrayList;
import java.util.List;

public class TestCreation {

    public static PlayerCharacter create() {
        PlayerCharacter c = new PlayerCharacter();
        c.setName("Name");
        c.setClassName("Krieger");
        c.setSt(2);
        c.setGs(90);
        c.setGw(99);
        c.setKo(10);
        c.setIn(50);
        c.setZt(50);
        c.setAu(50);
        c.setPa(50);
        c.setApWurf(2);
        List<Skill> skills = new ArrayList<>();


        Skill s = new Skill();
        s.setName("Klettern");
        s.setLevel(12);
        s.setBaseAttribute("Gs");
        skills.add(s);

        c.setSkills(skills);

        return c;
    }

    public static PlayerCharacter getExpectedCharacter() {
        PlayerCharacter exp = new PlayerCharacter();
        exp.setName( "Name" );
        exp.setClassName( "Krieger" );
        exp.setRace("Mensch");
        exp.setLevel(1);
        exp.setSpentLevel(1);
        exp.setNotSpentEp(0);
        exp.setTotalEp(0);
        exp.setSt( 2 );
        exp.setGs( 90 );
        exp.setGw( 99 );
        exp.setKo( 10 );
        exp.setIn( 50 );
        exp.setZt( 50 );
        exp.setAu( 50 );
        exp.setPa( 50 );

        exp.setStBonus(-2);
        exp.setGsBonus(1);
        exp.setGwBonus(2);
        exp.setKoBonus(-1);
        exp.setInBonus(0);
        exp.setZtBonus(0);
        exp.setAuBonus(0);
        exp.setPaBonus(0);

        exp.setApWurf(2);
        exp.setApBonus(4);
        exp.setAp(6);

        return exp;
    }

    public static Skill getExpectedSkill(String name) {
        Skill skill = new Skill();
        if (name.equals("Klettern")) {
            skill.setName("Klettern");
            skill.setLevel(12);
            skill.setBaseAttribute("Gs");
            skill.setAttributeBonus(1);
            skill.setBonus(13); // 7+1
            skill.setPractice(0);
            // learned = 20
            skill.setCostGold(20);
            //learned --> Freiland/A +8 --> 10 * Krieger/Freiland=10 --> 100
            skill.setCostEP(100);
            skill.setLearned(true);
        }
        if (name.equals("Reiten")) {
            skill.setName("Reiten");
            skill.setLevel(2);
            skill.setBaseAttribute("Gs");
            skill.setAttributeBonus(1);
            skill.setBonus(3);  // 2+1
            skill.setPractice(0);
            // unlearned = 200
            skill.setCostGold(200);
            // unlearned --> Klettern = Freiland 2LE = 3*2 -->  * Krieger/Freiland=10 --> 60
            skill.setCostEP(60);
            skill.setLearned(false);
        }
        return skill;
    }
}
