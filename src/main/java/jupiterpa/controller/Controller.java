package jupiterpa.controller;

import jupiterpa.model.Cost;
import jupiterpa.model.PlayerCharacter;
import jupiterpa.repository.CharacterRepository;
import jupiterpa.security.SecurityService;
import jupiterpa.service.CalculationServiceImpl;
import jupiterpa.service.LearningServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequestMapping(path = Controller.PATH)
@RestController
public class Controller {
    public static final String PATH = "/api";

    @Autowired
    CharacterRepository characterRepo;
    @Autowired
    LearningServiceImpl costService;
    @Autowired
    CalculationServiceImpl calculationService;
    @Autowired
    SecurityService security;

    @GetMapping("/character")
    public List<PlayerCharacterInfo> getCharacters() {
        List<PlayerCharacter> entities = characterRepo.findAll();
        List<PlayerCharacterInfo> characters = new ArrayList<>();
        for (PlayerCharacter entity : entities) {
            PlayerCharacterInfo c = new PlayerCharacterInfo();
            c.setName(entity.getName());
            c.setClassName(entity.getClassName());
            c.setLevel(entity.getLevel());
            if (security.allowed(entity.getUser()))
                characters.add(c);
        }
        return characters;

    }

    @GetMapping("/character/{name}")
    public PlayerCharacter getCharacters(@PathVariable String name) throws Exception {
        Optional<PlayerCharacter> entity = characterRepo.findByName(name);
        if (entity.isPresent()) {
            PlayerCharacter c = calculationService.enrich(entity.get());
            if (security.allowed(c.getUser()))
                return c;
            else
                return null;
        } else
            return null;
    }

    @PostMapping("/character")
    public PlayerCharacter create(@RequestBody PlayerCharacter playerCharacter) throws Exception {
        playerCharacter.setUser( security.getUser() ); // Set User
        playerCharacter = calculationService.enrich(playerCharacter); // for checks
        playerCharacter.getSkills().removeIf( s -> !s.isLearned());
        playerCharacter = characterRepo.save(playerCharacter); // save
        playerCharacter = calculationService.enrich(playerCharacter); // enrich again for return parameter
        return playerCharacter;
    }

    @PostMapping("/character/{name}/learn/{skill}/{gold}")
    public Cost learnSkill(
                @PathVariable String name,
                @PathVariable String skill,
                @PathVariable int gold
                ) throws Exception {

        // Read corresponding character
        Optional<PlayerCharacter> co = characterRepo.findByName(name);
        if (! co.isPresent()) throw new Exception("Character does not exist");
        PlayerCharacter character = co.get();

        security.check(character.getUser());

        // Enrich
        character = calculationService.enrich(character);

        // Learn
        Cost cost = costService.learn(character, skill, gold);

        // Save
        characterRepo.save(character);

        return cost;
    }

}
