package jupiterpa.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class SkillEntity {
    String name;
    int level;
    String baseAttribute;

    int practice;
}
