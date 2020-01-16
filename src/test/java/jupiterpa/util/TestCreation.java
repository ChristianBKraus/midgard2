package jupiterpa.util;

import jupiterpa.model.PlayerCharacter;
import jupiterpa.model.PlayerCharacterEntity;
import jupiterpa.model.Skill;
import jupiterpa.model.SkillEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TestCreation {

    public static PlayerCharacterEntity create() {
        PlayerCharacterEntity c = new PlayerCharacterEntity();
        c.setName("Name");
        c.setClassName("Krieger");
        c.setSt(2);
        c.setKo(10);
        c.setGs(90);
        c.setGw(99);
        List<SkillEntity> skills = new ArrayList<>();


        SkillEntity s = new SkillEntity();
        s.setName("Klettern");
        s.setLevel(12);
        s.setBaseAttribute("Gs");
        skills.add(s);

        c.setSkills(skills);

        return c;
    }

    public static PlayerCharacter getExpectedCharacter(long id) {
        PlayerCharacter exp = new PlayerCharacter();
        exp.setId( id );
        exp.setName( "Name" );
        exp.setClassName( "Krieger" );
        exp.setLevel(1);
        exp.setNotSpentEp(0);
        exp.setTotalEp(0);
        exp.setSt( 2 );
        exp.setKo( 10 );
        exp.setGs( 90 );
        exp.setGw( 99 );

        exp.setStBonus(-2);
        exp.setKoBonus(-1);
        exp.setGsBonus(1);
        exp.setGwBonus(2);

        return exp;
    }

    public static Skill getExpectedSkill(String name, long id) {
        Skill skill = new Skill();
        if (name.equals("Klettern")) {
            skill.setCharacterId(id);
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
            skill.setCharacterId(id);
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