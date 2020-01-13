package jupiterpa.service;

import jupiterpa.model.PlayerCharacterEntity;
import jupiterpa.model.SkillEntity;

import java.util.ArrayList;
import java.util.List;

public class TestCreation {
    public static PlayerCharacterEntity create() {
        PlayerCharacterEntity c = new PlayerCharacterEntity();
        c.setName("Name");
        c.setClassName("Krieger");
        c.setSt(10);
        c.setKo(10);
        c.setGs(10);
        c.setGw(10);


        SkillEntity s = new SkillEntity();
        s.setName("Klettern");
        s.setLevel(7);
        s.setBaseAttribute("Gs");

        List<SkillEntity> skills = new ArrayList<>();
        skills.add(s);
        c.setSkills(skills);

        return c;
    }
}
