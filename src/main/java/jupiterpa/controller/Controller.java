package jupiterpa.controller;

import jupiterpa.model.Cost;
import jupiterpa.model.PlayerCharacter;
import jupiterpa.repository.CharacterRepository;
import jupiterpa.security.SecurityService;
import jupiterpa.service.CalculationServiceImpl;
import jupiterpa.service.LearningServiceImpl;
import jupiterpa.service.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

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
    public PlayerCharacter getCharacters(@PathVariable String name) throws UserException {
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
    public PlayerCharacter create(@RequestBody PlayerCharacter playerCharacter) throws UserException {
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
                ) throws UserException {

        // Read corresponding character
        Optional<PlayerCharacter> co = characterRepo.findByName(name);
        if (! co.isPresent()) throw new UserException("Charakter " + name + " existiert nicht");
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

    @ExceptionHandler(UserException.class)
    public ModelAndView handleException(HttpServletRequest req, UserException exp) throws HTTPUserException {
        throw new HTTPUserException(exp.getMessage());
    }

    @ResponseStatus(value= HttpStatus.BAD_REQUEST)
    public static class HTTPUserException extends RuntimeException {
        public HTTPUserException(String text) {
            super(text);
        }
    }

}
