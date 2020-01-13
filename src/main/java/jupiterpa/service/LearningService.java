package jupiterpa.service;

import jupiterpa.model.*;

import java.util.UUID;

public interface LearningService {
    Cost learn(PlayerCharacter character, String skillName, int gold) throws Exception;
}
