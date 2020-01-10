package jupiterpa.service;

import jupiterpa.model.Character;
import jupiterpa.model.Cost;
import jupiterpa.model.Skill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CreateService {
    @Autowired
    SettingsService settings;
    @Autowired
    UtilityService utility;
    @Autowired
    CostService costService;

    public Character create(Character c) throws Exception {

        // Checks
        if (! settings.classes.contains(c.getClassName()) ) {
            throw new Exception("Class not found");
        }
        // check range
        checkAttribute( c.getSt() );
        checkAttribute( c.getKo() );
        checkAttribute( c.getGw() );
        checkAttribute( c.getGs() );

        // ID and Level
        c.setId(UUID.randomUUID());
        c.setLevel(1);

        // Attribute Bonus
        c.setStBonus(getBonus(c.getSt()));
        c.setKoBonus(getBonus(c.getKo()));
        c.setGwBonus(getBonus(c.getGw()));
        c.setGsBonus(getBonus(c.getGs()));

        // Determine all Skills (incl. unskilled)
        settings.loadFile("defaultSkills.csv",settings.defaultSkillConsumer);
        for (Skill s : c.getSkills()) {
            s.setLearned(true); // mark all passed skills as learned
        }
        for (Skill s : settings.defaultSkills) {
            // if skill is passed use it, otherwise copy default skill
            if (! utility.existSkill(c.getSkills(), s.getName()) ) {
                c.getSkills().add(s);
            }
            // enrich raw skills data
            enrichSkill(s, c);
        }

        return c;
    }

    private void checkAttribute(int attr) throws Exception {
        if (attr < 1 || attr > 100) throw new Exception("Attribute out of Range");
    }

    private void enrichSkill(Skill s, Character c) throws Exception {
        s.setCharacterId(c.getId());
        // name
        // level

        s.setBaseAttribute( getBaseAttribute(s) );
        s.setAttributeBonus( getAttributeBonus( c, s.getBaseAttribute() ) );
        s.setBonus( s.getLevel() + s.getAttributeBonus() );

        // practice
        Cost cost = costService.getCost(c,s);
        s.setCostEP(cost.getEp());
        s.setCostGold(cost.getGold());

        // learned

    }
    private int getAttributeBonus(Character c, String name) throws Exception {
        if (name.equals("St")) {
            return getBonus(c.getSt());
        }
        if (name.equals("Ko")) {
            return getBonus(c.getKo());
        }
        if (name.equals("Gw")) {
            return getBonus(c.getGw());
        }
        if (name.equals("Gs")) {
            return getBonus(c.getGs());
        }
        throw new Exception("Unknown Attribute");
    }
    private int getBonus(int v) {
        if (v <= 5) return -2;
        if (v <= 10) return -1;
        if (v >= 95) return 2;
        if (v >= 90) return 1;
        return 0;
    }
    private String getBaseAttribute(Skill skill) throws Exception {
        Skill s = utility.findSkill(settings.defaultSkills,skill.getName());
            return s.getBaseAttribute();
    }


}
