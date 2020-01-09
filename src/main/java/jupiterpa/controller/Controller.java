package jupiterpa.controller;

import jupiterpa.repository.DCharacterRepository;
import jupiterpa.model.DCharacter;
import jupiterpa.model.SkillDTO;
import jupiterpa.service.CostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(path = Controller.PATH)
@RestController
public class Controller {
    public static final String PATH = "/api";

    @Autowired
    DCharacterRepository characterRepo;
    @Autowired
    CostService service;

    @GetMapping("/character")
    public List<DCharacter> getCharacters() {
        return characterRepo.findAll();
    }
    @GetMapping("/character/{name}")
    public List<DCharacter> getCharacters(@PathVariable String name) {
        return characterRepo.findByName(name);
    }
    @GetMapping("/skill/{characterId}")
    public List<SkillDTO> getSkills(@PathVariable long id) {
        return service.getSkills(id);
    }

    @PostMapping("/character/{characterId}/learn/{skill}")
    public int learnSkill(@PathVariable long characterId, @PathVariable String skill) { return service.learn(characterId, skill);}

}
