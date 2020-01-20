package jupiterpa.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class Skill {
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
        name = v[0];
        level = Integer.parseInt(v[1]);
        baseAttribute = v[2];
    }
}
