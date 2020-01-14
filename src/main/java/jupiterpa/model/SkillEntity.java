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

}
