package jupiterpa.service;

import jupiterpa.model.*;

public interface CalculationService {
    PlayerCharacter enrich(PlayerCharacterEntity c) throws Exception;
    Cost calculate(PlayerCharacter c, Skill s);
    PlayerCharacterEntity condense(PlayerCharacter character) throws Exception;
}
