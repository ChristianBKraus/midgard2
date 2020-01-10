package jupiterpa.model;

import lombok.Data;

@Data
public class CostsClass {
    String className;
    String group;
    int cost;

    public CostsClass(String[] v) {
        className = v[0];
        group = v[1];
        cost = Integer.getInteger(v[2]);
    }
}
