package jupiterpa.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class LevelUp {
    String name;
    int apWurf;
    String attribute;
    int inc;
}
