package jupiterpa.service;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import jupiterpa.model.*;
import jupiterpa.model.Character;
import jupiterpa.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

@Service
public class CostService {

    @Autowired
    CharacterRepository characterRepo;

    Map<String,CostsMain> mainCosts; //CostRow+Bonus
    Map<String,CostsClass> classCosts; //ClassName+Group
    Map<String,CostsSkill> skillCosts; //Name

    List<String> classes = new ArrayList<>();
    List<String> skills = new ArrayList<>();


    public Cost learn(long CharacterId, String skill, int ep) {
        return new Cost(0,ep,0);
    }

    void load(String file,Consumer<String[]> consumer) throws URISyntaxException, IOException {
        CSVParser parser = new CSVParserBuilder().withSeparator(';').withIgnoreQuotations(true).build();
        Reader reader = Files.newBufferedReader(Paths.get(ClassLoader.getSystemResource(file).toURI()));
        CSVReader mainReader = new CSVReaderBuilder(reader).withSkipLines(1).withCSVParser(parser).build();
        mainReader.forEach(consumer);
    }
    Consumer<String[]> mainConsumer = line -> {
        CostsMain c = new CostsMain( line );
        mainCosts.put(c.getCostRow() + "/" + c.getBonus(),c);
    };
    Consumer<String[]> classConsumer = line -> {
        CostsClass c = new CostsClass( line );
        classCosts.put(c.getClassName() + "/" + c.getGroup(),c);
        classes.add(c.getClassName());
    };
    Consumer<String[]> skillConsumer = line -> {
        CostsSkill c = new CostsSkill( line );
        skillCosts.put(c.getName(),c);
        skills.add(c.getName());
    };

    void loadCosts() throws URISyntaxException, IOException {
        load("mainCosts.csv",mainConsumer);
        load("classCosts.csv",classConsumer);
        load("skillCosts.csv",skillConsumer);
        //initializeId();
    }

    Character create(Character c) throws Exception {
        Character c1 = enrichCharacter(c);
        for (Skill s : c1.getSkills()) {
            enrichSkill(s);
        }
        return c;
    }

    Character enrichCharacter(Character c) throws Exception {
        // Checks
        if (! classes.contains(c.getClassName()) ) {
            throw new Exception("Class not found");
        }
        // check range

        // enrich
        c.setId(UUID.randomUUID());
        c.setLevel(1);

        c.setStBonus(getBonus(c.getSt()));
        c.setKoBonus(getBonus(c.getKo()));
        c.setGwBonus(getBonus(c.getGw()));
        c.setGsBonus(getBonus(c.getGs()));

        // Determine all Skills (incl. unskilled)

        return c;
    }
    int getBonus(int v) {
        if (v <= 5) return -2;
        if (v <= 10) return -1;
        if (v >= 95) return 2;
        if (v >= 90) return 1;
        return 0;
    }
    void enrichSkill(Skill s) {
    }
}
