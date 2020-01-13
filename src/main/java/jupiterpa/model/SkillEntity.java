package jupiterpa.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data @NoArgsConstructor
public class SkillEntity {
    UUID characterId;

    String name;
    int level;
    String baseAttribute;

    int practice;

    public SkillEntity(String[] v) {
        name = v[0];
        level = Integer.parseInt(v[1]);
        baseAttribute = v[2];
    }
}
