package jupiterpa.service;

import jupiterpa.model.*;
import jupiterpa.model.PlayerCharacter;

import java.util.UUID;

public interface LearningService {
    Cost learn(UUID characterId, String skillName, int gold) throws Exception;
}
