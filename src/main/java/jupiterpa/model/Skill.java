package jupiterpa.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data @AllArgsConstructor @NoArgsConstructor
public class Skill {
    UUID characterId;
    String name;

    int level;

    String baseAttribute;
    int attributeBonus;
    int bonus;

    int practice;

    int costGold;
    int costEP;
}
