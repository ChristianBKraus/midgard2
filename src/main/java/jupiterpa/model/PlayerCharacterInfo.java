package jupiterpa.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlayerCharacterInfo {
    String name;
    String className;
    int level;
    String race;
}
