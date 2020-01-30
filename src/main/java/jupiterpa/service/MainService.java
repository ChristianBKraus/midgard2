package jupiterpa.service;

import jupiterpa.model.*;
import java.util.List;

public interface MainService {

    List<PlayerCharacterInfo> getCharacters();
    PlayerCharacter getCharacter(String name) throws UserException;

    PlayerCharacter create(PlayerCharacter playerCharacter) throws UserException;

    Cost learnSkill(Learn value) throws UserException;
    PlayerCharacter improve(Improve value) throws UserException;
    PlayerCharacter levelUp(LevelUp value) throws UserException;
}
