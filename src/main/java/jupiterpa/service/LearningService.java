package jupiterpa.service;

import jupiterpa.model.*;

public interface LearningService {
    Cost learn(PlayerCharacter character, String skillName, int gold) throws UserException;
}
