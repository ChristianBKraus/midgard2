package jupiterpa.controller;

import jupiterpa.repository.DCharacterRepository;
import jupiterpa.repository.DSkillRepository;
import jupiterpa.model.DCharacter;
import jupiterpa.model.DSkill;
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
    DSkillRepository skillRepo;
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
    public List<DSkill> getSkills(@PathVariable long id) {
        return skillRepo.findByCharacterId(id);
    }

}
