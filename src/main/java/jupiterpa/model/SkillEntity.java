package jupiterpa.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Data @NoArgsConstructor
public class SkillEntity {
    @Id
    long characterId;

    String name;
    int level;
    String baseAttribute;

    int practice;

}
