package jupiterpa.service;

import jupiterpa.model.*;

public interface CalculationService {
    PlayerCharacter enrich(PlayerCharacter c) throws Exception;
    Cost calculate(PlayerCharacter c, Skill s);
}
