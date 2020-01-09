package jupiterpa.model;

import lombok.Data;

@Data
public class DCharacter {
    long id;
    String name;

    String className;
    int level;

    int st;
    int ko;
    int gw;
    int gs;
}
