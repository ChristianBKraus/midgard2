package jupiterpa.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class PlayerCharacterInfo {
    long id;
    String name;
    String className;
    int level;
}
