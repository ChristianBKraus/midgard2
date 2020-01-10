package jupiterpa.model;

import lombok.Data;

@Data
public class CostsClass {
    String className;
    String group;
    int cost;

    public CostsClass(String[] line) {

    }
}
