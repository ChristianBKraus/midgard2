package jupiterpa.service;

import jupiterpa.model.*;
import jupiterpa.repository.CharacterRepository;
import jupiterpa.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MainServiceImpl implements MainService {
    @Autowired
    CharacterRepository repo;
    @Autowired
    LearningService learning;
    @Autowired
    CalculationService calculation;
    @Autowired
    SecurityService security;

    public MainServiceImpl(
            CharacterRepository repo,
            LearningService learning,
            CalculationService calculation,
            SecurityService security) {
        this.repo = repo;
        this.learning = learning;
        this.calculation = calculation;
        this.security = security;
    }

    public List<PlayerCharacterInfo> getCharacters() {
        List<PlayerCharacter> entities = repo.findAll();
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

    public PlayerCharacter getCharacter(String name) throws UserException {
        return read(name);
    }

    public PlayerCharacter create(PlayerCharacter playerCharacter) throws UserException {
        playerCharacter.setUser( security.getUser() ); // Set User
        playerCharacter = calculation.enrich(playerCharacter); // for checks
        // save
        return save(playerCharacter);
    }

    public Cost learnSkill(Learn input) throws UserException {
        PlayerCharacter character = read(input.getName());

        // Learn
        Cost cost = learning.learn(character, input.getSkill(), input.getGold());

        save(character);
        return cost;

    }

    public PlayerCharacter improve(Improve input) throws UserException {
        PlayerCharacter c = read(input.getName());

        c = learning.improve(c,input);

        c = save(c);
        return c;
    }

    public PlayerCharacter levelUp(LevelUp input) throws UserException {
        PlayerCharacter c = read(input.getName());

        c = learning.levelUp(c,input);

        c = save(c);
        return c;
    }

    private PlayerCharacter read(String name) throws  UserException {

        // read from DB
        Optional<PlayerCharacter> co = repo.findByName(name);
        if (! co.isPresent()) throw new UserException("Charakter " + name + " existiert nicht");
        PlayerCharacter character = co.get();

        // filter w.r.t. user
        security.check(character.getUser());

        // Enrich
        return calculation.enrich(character);
    }
    private PlayerCharacter save(PlayerCharacter c) throws UserException {

        // Eliminate unlearned skills
        c.getSkills().removeIf( s -> ! s.isLearned() );

        // save to DB
        c = repo.save(c);

        // enrich saved data again
        return calculation.enrich(c);
    }

}
