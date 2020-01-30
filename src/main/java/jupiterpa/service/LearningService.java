package jupiterpa.service;

import jupiterpa.model.*;

public interface LearningService {
    Cost learn(PlayerCharacter c, String skillName, int gold) throws UserException;
    PlayerCharacter improve(PlayerCharacter c, Improve input) throws UserException;
    PlayerCharacter levelUp(PlayerCharacter c, LevelUp input) throws UserException;
}
