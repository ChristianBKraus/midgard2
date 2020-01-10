package jupiterpa.service;

import jupiterpa.model.Skill;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UtilityService {

    Skill findSkill(List<Skill> skills, String name) throws Exception {
        Optional<Skill> option = skills.stream().filter( s -> name.equals(s.getName()) ).findFirst();
        if (! option.isPresent()) throw new Exception("Skill does not exist");
        return option.get();
    }

    boolean existSkill(List<Skill> skills, String name) {
        try {
            findSkill(skills,name);
        } catch (Exception ex){
            return false;
        }
        return true;
    }
}