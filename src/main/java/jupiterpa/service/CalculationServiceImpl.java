package jupiterpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jupiterpa.model.*;

import java.util.ArrayList;
import java.util.List;

@Service
public class CalculationServiceImpl implements CalculationService {
    @Autowired
    final SettingsService settings;
    @Autowired
    final UtilityService utility;

    public CalculationServiceImpl(SettingsService settings, UtilityService utility) {
        this.settings = settings;
        this.utility = utility;
    }

    public PlayerCharacter enrich(PlayerCharacter character) throws Exception {
        // Checks
        if (! settings.getClasses().contains(character.getClassName()) ) {
            throw new Exception("Class " + character.getClassName() + " not found");
        }
        // check range
        checkAttribute( character.getSt() );
        checkAttribute( character.getKo() );
        checkAttribute( character.getGw() );
        checkAttribute( character.getGs() );

        // Base Fields
        if (character.getLevel() == 0)
            character.setLevel(1);

        // Attributes

        // Attribute Bonus
        character.setStBonus(getBonus(character.getSt()));
        character.setKoBonus(getBonus(character.getKo()));
        character.setGwBonus(getBonus(character.getGw()));
        character.setGsBonus(getBonus(character.getGs()));

        //// Skills
        List<Skill> enrichedSkills = new ArrayList<>();
        for (Skill defaultSkill : settings.getDefaultSkills()) {
            Skill skill = new Skill();
            if (! utility.existSkill(character.getSkills(), defaultSkill.getName()) ) {
                // Skill does not exist for character --> use default
                skill.setName( defaultSkill.getName() );
                skill.setLevel( defaultSkill.getLevel() );
                skill.setPractice(0);
                skill.setLearned(false);
            } else {
                Skill se = utility.findSkill(character.getSkills(),defaultSkill.getName());
                skill.setName( se.getName() );
                skill.setLevel( se.getLevel() );
                skill.setPractice( se.getPractice() );
                skill.setLearned(true);
            }
            // enrich raw skills data
            enrichSkill(skill, character);

            enrichedSkills.add(skill);
        }
        character.setSkills(enrichedSkills);

        return character;
    }

    private void checkAttribute(int attr) throws Exception {
        if (attr < 1 || attr > 100) throw new Exception("Attribute out of Range");
    }

    private void enrichSkill(Skill s, PlayerCharacter c) throws Exception {
        // name
        // level

        s.setBaseAttribute( getBaseAttribute(s) );
        s.setAttributeBonus( getAttributeBonus( c, s.getBaseAttribute() ) );
        s.setBonus( s.getLevel() + s.getAttributeBonus() );

        // practice
        Cost cost = calculate(c,s);
        s.setCostEP(cost.getEp());
        s.setCostGold(cost.getGold());

        // learned

    }
    private int getAttributeBonus(PlayerCharacter c, String name) throws Exception {
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
        Skill s = utility.findSkill(settings.getDefaultSkills(),skill.getName());
            return s.getBaseAttribute();
    }

    public Cost calculate(PlayerCharacter c, Skill s) {

        CostsSkill costsSkill = settings.getSkillCosts().get(s.getName());

        String group = costsSkill.getGroups();
        CostsClass costsClass = settings.getClassCosts().get(c.getClassName()+"/"+group);


        int le = costsSkill.getLe();
        int teCost = costsClass.getCost();
        int ep;
        int te = 0;
        int gold;
        int practice;
        if (s.isLearned()) {
            int newBonus = s.getLevel() + 1;
            String key = costsSkill.getCostRow() + "/" + newBonus;
            CostsMain costsMain = settings.getMainCosts().get(key);
            te = costsMain.getMultiplier();

            practice = Math.min(te, s.getPractice());
            ep = (te - practice) * teCost;
            gold = 20;
        } else {
            practice = 0;
            ep = le * 3 * teCost;
            gold = 200;
        }

        return new Cost(gold,ep,practice,te,le);
    }
}
