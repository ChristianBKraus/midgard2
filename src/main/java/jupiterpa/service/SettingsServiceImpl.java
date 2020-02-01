package jupiterpa.service;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import jupiterpa.actuator.Health;
import jupiterpa.actuator.HealthInfo;
import jupiterpa.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Service
public class SettingsServiceImpl implements SettingsService {
    final Map<String, CostsMain> mainCosts = new HashMap<>(); //CostRow+Bonus
    final Map<String, CostsClass> classCosts = new HashMap<>(); //ClassName+Group
    final Map<String, CostsSkill> skillCosts = new HashMap<>(); //Name
    final List<String> classes = new ArrayList<>(); // by CostsClass
    List<Skill> defaultSkills;                // on Request
    final Map<String, CostsLevel> levelCosts = new HashMap<>(); // Level

    @Autowired
    Health health;

    public Map<String, CostsMain> getMainCosts() {
        return mainCosts;
    }

    public Map<String, CostsSkill> getSkillCosts() {
        return skillCosts;
    }

    public Map<String, CostsClass> getClassCosts() {
        return classCosts;
    }

    public List<String> getClasses() {
        return classes;
    }

    public List<Skill> getDefaultSkills() throws UserException {
        defaultSkills = new ArrayList<>();
        loadFile("defaultSkills.csv", defaultSkillConsumer);
        return defaultSkills;
    }

    public Map<String, CostsLevel> getLevelCosts() { return levelCosts; }

    public SettingsServiceImpl() throws UserException {
        loadFile("mainCosts.csv", mainConsumer);
        loadFile("classCosts.csv", classConsumer);
        loadFile("skillCosts.csv", skillConsumer);
        loadFile("levelCosts.csv", levelConsumer );
        if (health != null)
            health.setHealth(new HealthInfo("Status", false, "Initialized"));
    }

    final Consumer<String[]> mainConsumer = line -> {
        CostsMain c = new CostsMain(line);
        mainCosts.put(c.getCostRow() + "/" + c.getBonus(), c);
    };
    final Consumer<String[]> classConsumer = line -> {
        CostsClass c = new CostsClass(line);
        classCosts.put(c.getClassName() + "/" + c.getGroup(), c);
        classes.add(c.getClassName());
    };
    final Consumer<String[]> skillConsumer = line -> {
        CostsSkill c = new CostsSkill(line);
        skillCosts.put(c.getName(), c);
    };
    final Consumer<String[]> defaultSkillConsumer = line -> {
        Skill c = new Skill(line);
        defaultSkills.add(c);
    };
    final Consumer<String[]> levelConsumer = line -> {
        CostsLevel c = new CostsLevel(line);
        levelCosts.put(String.valueOf(c.getLevel()),c);
    };

    void loadFile(String file, Consumer<String[]> consumer) throws UserException {
        try {
            CSVParser parser = new CSVParserBuilder().withSeparator(';').withIgnoreQuotations(true).build();
            URL resource = getClass().getClassLoader().getResource(file);
            Reader reader;
            try  {
                assert resource != null;
                URI uri = resource.toURI();
                Path path = Paths.get(uri);
                reader = Files.newBufferedReader(path);
            } catch (FileSystemNotFoundException e) {
                reader = new BufferedReader (new FileReader(file));
            }
            CSVReader mainReader = new CSVReaderBuilder(reader).withSkipLines(1).withCSVParser(parser).build();
            mainReader.forEach(consumer);
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
            throw new UserException("Interner Fehler beim Laden");
        }
    }
}
