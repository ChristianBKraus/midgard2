package jupiterpa.service;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import jupiterpa.model.CostsClass;
import jupiterpa.model.CostsMain;
import jupiterpa.model.CostsSkill;
import jupiterpa.model.Skill;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Service
public class SettingsService {
    public Map<String, CostsMain> mainCosts; //CostRow+Bonus
    public Map<String, CostsClass> classCosts; //ClassName+Group
    public Map<String, CostsSkill> skillCosts; //Name
    public List<String> classes = new ArrayList<>(); // by CostsClass
    public List<Skill> defaultSkills;                // on Request

    SettingsService() throws URISyntaxException, IOException {
        loadFile("mainCosts.csv",mainConsumer);
        loadFile("classCosts.csv",classConsumer);
        loadFile("skillCosts.csv",skillConsumer);
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
    };
    Consumer<String[]> defaultSkillConsumer = line -> {
        Skill c = new Skill( line );
        defaultSkills.add(c);
    };

    void loadFile(String file,Consumer<String[]> consumer) throws URISyntaxException, IOException {
        CSVParser parser = new CSVParserBuilder().withSeparator(';').withIgnoreQuotations(true).build();
        Reader reader = Files.newBufferedReader(Paths.get(ClassLoader.getSystemResource(file).toURI()));
        CSVReader mainReader = new CSVReaderBuilder(reader).withSkipLines(1).withCSVParser(parser).build();
        mainReader.forEach(consumer);
    }
}
