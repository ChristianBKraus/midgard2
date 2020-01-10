package jupiterpa.model;

import lombok.Data;

import java.util.UUID;

@Data
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

    boolean learned;

    public Skill(String[] v) {
        characterId = UUID.fromString(v[0]);
        name = v[1];
        level = Integer.getInteger(v[2]);
        baseAttribute = v[3];
        attributeBonus = Integer.getInteger(v[4]);
        bonus = Integer.getInteger(v[5]);
        practice = Integer.getInteger(v[6]);
        costGold = Integer.getInteger(v[7]);
        costEP = Integer.getInteger(v[8]);
        learned = Boolean.getBoolean(v[9]);
    }
}
