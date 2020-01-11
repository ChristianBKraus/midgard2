package jupiterpa.service;

import jupiterpa.model.*;
import jupiterpa.model.Character;

public interface CostService {
    Cost getCost(Character c, Skill s);
}
