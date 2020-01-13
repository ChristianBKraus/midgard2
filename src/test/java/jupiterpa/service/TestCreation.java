package jupiterpa.service;

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
        s.setLevel(7);
        s.setBaseAttribute("Gs");
        skills.add(s);

        c.setSkills(skills);

        return c;
    }

    public static PlayerCharacter getExpectedCharacter(UUID id) {
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

    public static Skill getExpectedSkill(String name, UUID id) {
        Skill skill = new Skill();
        if (name.equals("Klettern")) {
            skill.setCharacterId(id);
            skill.setName("Klettern");
            skill.setLevel(7);
            skill.setBaseAttribute("Gs");
            skill.setAttributeBonus(1);
            skill.setBonus(8);
            skill.setPractice(0);
            skill.setCostGold(20);
            skill.setCostEP(100);
            skill.setLearned(true);
        }
        return skill;
    }
}
