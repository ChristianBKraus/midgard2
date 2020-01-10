package jupiterpa.model;

import lombok.Data;

@Data
public class CostsSkill {
    String name;
    String attribute;
    String groups;
    int le;
    String costRow;

    public CostsSkill(String[] line) {

    }
}
