package jupiterpa.model;

import lombok.Data;

@Data
public class CostsLevel {
    int level;
    int cost;

    public CostsLevel(String[] input) {
        level = Integer.parseInt(input[0]);
        cost = Integer.parseInt(input[1]);
    }
}
