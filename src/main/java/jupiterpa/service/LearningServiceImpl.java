package jupiterpa.service;

import jupiterpa.model.Cost;
import jupiterpa.model.PlayerCharacter;
import jupiterpa.model.Skill;
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
        cost = calculation.calculate(c,s);
        s.setCostEP(cost.getEp());
        s.setCostGold(cost.getGold());

        // return cost spend for this learning
        return new Cost(spentGold,
                        reducedByGold,
                        cost.getPractice(),
                        cost.getTe(),
                        cost.getLe());
    }

}
