package jupiterpa.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;

@Data @NoArgsConstructor
public class Skill {
    String name;

    int level;
    String baseAttribute;

    @Transient
    int attributeBonus;
    @Transient
    int bonus;

    int practice;

    @Transient
    int costGold;
    @Transient
    int costEP;

    @Transient
    boolean learned;

    public Skill(String[] v) {
        name = v[0];
        level = Integer.parseInt(v[1]);
        baseAttribute = v[2];
    }
}
