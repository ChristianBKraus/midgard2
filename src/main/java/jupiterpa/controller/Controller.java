package jupiterpa.controller;

import jupiterpa.model.Cost;
import jupiterpa.repository.CharacterRepository;
import jupiterpa.model.Character;
import jupiterpa.service.CostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(path = Controller.PATH)
@RestController
public class Controller {
    public static final String PATH = "/api";

    @Autowired
    CharacterRepository characterRepo;
    @Autowired
    CostService service;

    @GetMapping("/character")
    public List<Character> getCharacters() {
        return characterRepo.findAll();
    }
    @GetMapping("/character/{name}")
    public List<Character> getCharacters(@PathVariable String name) {
        return characterRepo.findByName(name);
    }

    @PostMapping("/character/{characterId}/learn/{skill}/{ep}")
    public Cost learnSkill(
            @PathVariable long characterId,
            @PathVariable String skill,
            @PathVariable int ep
    ) { return service.learn(characterId, skill,ep);}

}
