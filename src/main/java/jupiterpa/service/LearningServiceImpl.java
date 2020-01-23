package jupiterpa.service;

import jupiterpa.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LearningServiceImpl implements LearningService {

    @Autowired
    final SettingsService settings;
    @Autowired
    final UtilityService utility;
    @Autowired
    final CalculationService calculation;

    public LearningServiceImpl(SettingsService settings, UtilityService utility, CalculationService calculation) {
        this.settings = settings;
        this.utility = utility;
        this.calculation = calculation;
    }

    public Cost learn(PlayerCharacter c, String skillName, int gold) throws UserException {

        // Find corresponding Skill
        Skill s = utility.findSkill(c.getSkills(),skillName);

        // Determine current cost (anew)
        Cost cost = calculation.calculate(c, s);

        //// consider used gold and reduce amount of EP
        // check for minimum gold
        if (cost.getGold() > gold) throw new UserException("Nicht genug Gold (" + cost.getGold() + " benötigt)");
        // reduce spent EP by gold
        int reducedByGold = cost.getEp() - ( gold - cost.getGold() ) / 10;
        // maximal halve EP can be converted to gold
        reducedByGold = Math.max(cost.getEp() / 2,reducedByGold);
        // determine maximal gold that is spent
        int spentGold = ( cost.getEp() - reducedByGold ) * 10;


        // update current not spent EP of character
        int notSpentEp = c.getNotSpentEp() - reducedByGold;
        if (notSpentEp < 0) throw new UserException("Nicht genug EP (" + reducedByGold + " benötigt)");
        c.setNotSpentEp(notSpentEp);
        // update gold
        c.setGold( c.getGold() - spentGold - cost.getGold());
        // update practice
        s.setPractice( s.getPractice() - cost.getPractice());

        // update skill (level, bonus)
        if (s.isLearned())
          s.setLevel(s.getLevel() + 1);
        else {
            int level = settings.getSkillCosts().get(skillName).getStartBonus();
            s.setLevel(level);
            s.setLearned(true);
        }
        s.setBonus(s.getLevel() + s.getAttributeBonus());

        // Recalculate new cost and store on skill
        Cost newCost = calculation.calculate(c,s);
        s.setCostEP(newCost.getEp());
        s.setCostGold(newCost.getGold());

        // return cost spend for this learning
        return new Cost(spentGold + cost.getGold(),
                        reducedByGold,
                        cost.getPractice(),
                        cost.getTe(),
                        cost.getLe());
    }

    public PlayerCharacter improve(PlayerCharacter c, Improve input) throws UserException {
        c.setNotSpentEp( c.getNotSpentEp() + input.getEp() );
        c.setTotalEp( c.getTotalEp() + input.getEp() );
        c.setGold( c.getGold() + input.getGold() );

        boolean levelUp;
        do {
            levelUp = false;
            CostsLevel cost = settings.getLevelCosts().get(String.valueOf(c.getLevel() + 1));
            if (cost.getCost() < c.getTotalEp()) {
                c.setLevel(c.getLevel() + 1);
                levelUp = true;
            }
        } while (levelUp);

        return c;
    }

    public PlayerCharacter levelUp(PlayerCharacter c, LevelUp input) throws UserException {
        if (c.getLevel() <= c.getSpentLevel())
            throw new UserException("Alle Stufen sind bereits gesteigert");
        c.setSpentLevel( c.getSpentLevel() + 1 );

        c.setApWurf(c.getApWurf() + input.getApWurf());

        if (input.getAttribute() != null) {
            if (!input.getAttribute().equals("")) {
                if (input.getInc() <= 0)
                    throw new UserException("Inkrement is nicht positiv");
                switch (input.getAttribute()) {
                    case "St": c.setSt( c.getSt() + input.getInc() ); break;
                    case "Gs": c.setGs( c.getGs() + input.getInc() ); break;
                    case "Gw": c.setGw( c.getGw() + input.getInc() ); break;
                    case "Ko": c.setKo( c.getKo() + input.getInc() ); break;
                    case "In": c.setIn( c.getIn() + input.getInc() ); break;
                    case "Zt": c.setZt( c.getZt() + input.getInc() ); break;
                    case "Au": c.setAu( c.getAu() + input.getInc() ); break;
                    case "pA": c.setPa( c.getPa() + input.getInc() ); break;
                    default: throw new UserException("Unbekanntes Attribut " + input.getAttribute()) ;
                }
            }
        }

        return c;
    }

}
