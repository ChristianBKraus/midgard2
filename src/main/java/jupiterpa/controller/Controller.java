package jupiterpa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import jupiterpa.model.*;
import jupiterpa.service.*;

@RequestMapping(path = Controller.PATH)
@RestController
public class Controller {
    public static final String PATH = "/api";

    @Autowired
    MainService service;

//  GET
    @GetMapping("/character")
    public List<PlayerCharacterInfo> getCharacters() {
        return service.getCharacters();
    }

    @GetMapping("/character/{name}")
    public PlayerCharacter getCharacter(@PathVariable String name) throws UserException {
        return service.getCharacter(name);
    }

// POST
    @PostMapping("/character")
    public PlayerCharacter create(@RequestBody PlayerCharacter playerCharacter) throws UserException {
        return service.create(playerCharacter);
    }

//  PATCH
    @PatchMapping("/character/learn")
    public Cost learnSkill(@RequestBody Learn value) throws UserException {

        return service.learnSkill(value);
    }

    @PatchMapping("character/improve")
    public PlayerCharacter improve(@RequestBody Improve value) {
        return service.improve(value);
    }

    @PatchMapping("character/levelup")
    public PlayerCharacter levelup(@RequestBody LevelUp v) throws UserException {
        return service.levelUp(v);
    }

//  Exceptions
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
