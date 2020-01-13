package jupiterpa.controller;

import jupiterpa.model.PlayerCharacter;
import jupiterpa.model.Cost;
import jupiterpa.model.PlayerCharacterEntity;
import jupiterpa.repository.CharacterRepository;
import jupiterpa.service.LearningServiceImpl;
import jupiterpa.service.CalculationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping(path = Controller.PATH)
@RestController
public class Controller {
    public static final String PATH = "/api";

    @Autowired
    CharacterRepository characterRepo;
    @Autowired
    LearningServiceImpl costService;
    @Autowired
    CalculationServiceImpl createService;

    @GetMapping("/character")
    public List<PlayerCharacter> getCharacters() {
        return characterRepo.findAll();
    }

    @GetMapping("/character/{name}")
    public List<PlayerCharacter> getCharacters(@PathVariable String name) {
        return characterRepo.findByName(name);
    }

    @PostMapping("/character/{characterId}/learn/{skill}/{gold}")
    public Cost learnSkill(
                @PathVariable UUID characterId,
                @PathVariable String skill,
                @PathVariable int gold
                ) throws Exception {
        return costService.learn(characterId, skill, gold);
    }

    @PostMapping("/character")
    public PlayerCharacter create(@RequestBody PlayerCharacterEntity playerCharacter) throws Exception {
        PlayerCharacter enrichedPlayerCharacter = createService.enrich(playerCharacter);
        return characterRepo.save(enrichedPlayerCharacter);
    }

}
