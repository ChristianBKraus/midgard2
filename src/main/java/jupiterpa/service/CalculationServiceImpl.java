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

    public PlayerCharacter enrich(PlayerCharacter character) throws UserException {
        // Checks
        if (! settings.getClasses().contains(character.getClassName()) ) {
            throw new UserException("Klasse " + character.getClassName() + " existiert nicht");
        }
        // check range
        checkAttribute("St", character.getSt() );
        checkAttribute("Gs", character.getGs() );
        checkAttribute("Gw", character.getGw() );
        checkAttribute("Ko", character.getKo() );
        checkAttribute("In", character.getIn() );
        checkAttribute("Zt", character.getZt() );
        checkAttribute("Au", character.getAu() );
        checkAttribute("pA", character.getPa() );

        // Base Fields
        if (character.getLevel() == 0) {
            character.setLevel(1);
            character.setSpentLevel(1);
        }

        // Attributes

        // Attribute Bonus
        character.setStBonus(getBonus(character.getSt()));
        character.setGsBonus(getBonus(character.getGs()));
        character.setGwBonus(getBonus(character.getGw()));
        character.setKoBonus(getBonus(character.getKo()));
        character.setInBonus(getBonus(character.getIn()));
        character.setZtBonus(getBonus(character.getZt()));
        character.setAuBonus(getBonus(character.getAu()));
        character.setPaBonus(getBonus(character.getPa()));

        // LP & AP
        character.setApBonus( getApBonus(character) );
        character.setAp( character.getApWurf() + character.getApBonus());

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

    private void checkAttribute(String name, int attr) throws UserException {
        if (attr < 1 || attr > 100) throw new UserException(name + " muss zwischen 1 und 100 liegen (" + attr + ")");
    }

    private void enrichSkill(Skill s, PlayerCharacter c) throws UserException {
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
    private int getAttributeBonus(PlayerCharacter c, String name) throws UserException {
        if (name.equals("St")) {
            return getBonus(c.getSt());
        }
        if (name.equals("Gs")) {
            return getBonus(c.getGs());
        }
        if (name.equals("Gw")) {
            return getBonus(c.getGw());
        }
        if (name.equals("Ko")) {
            return getBonus(c.getKo());
        }
        if (name.equals("In")) {
            return getBonus(c.getIn());
        }
        if (name.equals("Zt")) {
            return getBonus(c.getZt());
        }
        if (name.equals("Au")) {
            return getBonus(c.getAu());
        }
        if (name.equals("Pa")) {
            return getBonus(c.getPa());
        }
        throw new UserException("Attribute " + name + " ist nicht bekannt");
    }
    private int getBonus(int v) {
        if (v <= 5) return -2;
        if (v <= 20) return -1;
        if (v >= 96) return 2;
        if (v >= 81) return 1;
        return 0;
    }
    private String getBaseAttribute(Skill skill) throws UserException {
        Skill s = utility.findSkill(settings.getDefaultSkills(),skill.getName());
        return s.getBaseAttribute();
    }
    private int getApBonus(PlayerCharacter c) {
        int bonus = (int) Math.round( c.getKo() / 10.0 + c.getSt() / 20.0 );
        switch (c.getClassName()) {
            case "Krieger":
            case "Waldläufer":
            case "Barbar":
                bonus += 3 * c.getLevel();
                break;
            case "Söldner":
            case "Spitzbube":
            case "Schamane":
                bonus += 2 * c.getLevel();
                break;
            default:
                bonus += c.getLevel();

        }
        return bonus;
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
