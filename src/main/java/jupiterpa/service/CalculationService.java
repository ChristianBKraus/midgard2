package jupiterpa.service;

import jupiterpa.model.*;

public interface CalculationService {
    PlayerCharacter enrich(PlayerCharacter c) throws UserException;
    Cost calculate(PlayerCharacter c, Skill s);
}
