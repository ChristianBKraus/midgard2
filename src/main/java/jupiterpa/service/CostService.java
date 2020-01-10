package jupiterpa.service;

import jupiterpa.model.Character;
import jupiterpa.model.Cost;
import jupiterpa.model.Skill;
import jupiterpa.repository.CharacterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CostService {

    @Autowired
    CharacterRepository characterRepo;
    @Autowired
    SettingsService settings;
    @Autowired
    UtilityService utility;

    public Cost learn(UUID characterId, String skillName, int gold) throws Exception {
        // TODO practice

        // Read corresponding character
        Optional<Character> co = characterRepo.findById(characterId);
        if (! co.isPresent()) throw new Exception("Character does not exist");
        Character c = co.get();

        // Find corresponding Skill
        Skill s = utility.findSkill(c.getSkills(),skillName);

        // Determine current cost (anew)
        Cost cost = getCost(c, s, s.getPractice());

        // consider used gold and reduce amount of EP
        if (cost.getGold() > gold) throw new Exception("Not enough Gold");
        int reducedByGold = cost.getEp() - ( gold - cost.getGold() ) / 10;
        int notSpentEp = c.getNotSpentEp() - reducedByGold;
        if (notSpentEp < 0) throw new Exception("Not enough EP left");

        // update current not spent EP of character
        c.setNotSpentEp(notSpentEp);

        // update skill (level, bonus)
        s.setLevel(s.getLevel() + 1);
        s.setBonus(s.getLevel() + s.getAttributeBonus());

        // Recalculate new cost and store on skill
        cost = getCost(c,s,0);
        s.setCostEP(cost.getEp());
        s.setCostGold(cost.getGold());

        // save character
        characterRepo.save(c);

        // return cost spend for this learning
        return new Cost(gold,reducedByGold,0);
    }

    public Cost getCost(Character c, Skill s, int practice) {
        return new Cost(0,0,0);
    }
}
