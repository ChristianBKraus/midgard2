package jupiterpa.controller;

import jupiterpa.model.Cost;
import jupiterpa.model.PlayerCharacter;
import jupiterpa.model.PlayerCharacterEntity;
import jupiterpa.repository.CharacterRepository;
import jupiterpa.security.SecurityService;
import jupiterpa.security.UserStore;
import jupiterpa.service.CalculationServiceImpl;
import jupiterpa.service.LearningServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    CalculationServiceImpl calculationService;
    @Autowired
    SecurityService security;

    @GetMapping("/character")
    public List<PlayerCharacterInfo> getCharacters() {
        List<PlayerCharacterEntity> entities = characterRepo.findAll();
        List<PlayerCharacterInfo> characters = new ArrayList<>();
        for (PlayerCharacterEntity entity : entities) {
            PlayerCharacterInfo c = new PlayerCharacterInfo();
            c.setId(entity.getId());
            c.setName(entity.getName());
            c.setClassName(entity.getClassName());
            c.setLevel(entity.getLevel());
            if (security.allowed(entity.getUser()))
                characters.add(c);
        }
        return characters;

    }

    @GetMapping("/character/{name}")
    public List<PlayerCharacter> getCharacters(@PathVariable String name) throws Exception {
        List<PlayerCharacterEntity> entities = characterRepo.findAll();
        List<PlayerCharacter> characters = new ArrayList<>();
        for (PlayerCharacterEntity entity : entities) {
            PlayerCharacter c = calculationService.enrich(entity);
            if (security.allowed(c.getUser()))
                characters.add(c);
        }
        return characters;
    }

    @PostMapping("/character")
    public PlayerCharacter create(@RequestBody PlayerCharacterEntity playerCharacter) throws Exception {
        playerCharacter.setUser( security.getUser() ); // Set User
        PlayerCharacter enrichedPlayerCharacter = calculationService.enrich(playerCharacter);
        playerCharacter = calculationService.condense(enrichedPlayerCharacter);
        characterRepo.save(playerCharacter);
        return enrichedPlayerCharacter;
    }

    @PostMapping("/character/{characterId}/learn/{skill}/{gold}")
    public Cost learnSkill(
                @PathVariable UUID characterId,
                @PathVariable String skill,
                @PathVariable int gold
                ) throws Exception {

        // Read corresponding character
        Optional<PlayerCharacterEntity> co = characterRepo.findById(characterId);
        if (! co.isPresent()) throw new Exception("Character does not exist");
        PlayerCharacterEntity entity = co.get();

        security.check(entity.getUser());

        // Enrich
        PlayerCharacter c = calculationService.enrich(entity);

        // Learn
        Cost cost = costService.learn(c, skill, gold);

        // Save
        entity = calculationService.condense(c);
        characterRepo.save(entity);

        return cost;
    }

}
