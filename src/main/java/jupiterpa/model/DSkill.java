package jupiterpa.model;

import lombok.Data;

@Data
public class DSkill {
    long characterId;
    String name;
    int level;


    public int getBonus() {
        return level;
    }
}
