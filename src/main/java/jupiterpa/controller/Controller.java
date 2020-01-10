package jupiterpa.controller;

import jupiterpa.model.Character;
import jupiterpa.model.Cost;
import jupiterpa.repository.CharacterRepository;
import jupiterpa.service.CostService;
import jupiterpa.service.CreateService;
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
    CostService costService;
    @Autowired
    CreateService createService;

    @GetMapping("/character")
    public List<Character> getCharacters() {
        return characterRepo.findAll();
    }

    @GetMapping("/character/{name}")
    public List<Character> getCharacters(@PathVariable String name) {
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
    public Character create(@RequestBody Character character) throws Exception {
        Character enrichedCharacter = createService.create(character);
        return characterRepo.save(enrichedCharacter);
    }

}
