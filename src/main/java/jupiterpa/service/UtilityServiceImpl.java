package jupiterpa.service;

import jupiterpa.model.Skill;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UtilityServiceImpl implements UtilityService {

    public Skill findSkill(List<Skill> skills, String name) throws UserException {
        Optional<Skill> option = skills.stream().filter( s -> name.equals(s.getName()) ).findFirst();
        if (! option.isPresent()) throw new UserException("Fähigkeit " + name + " existiert nicht");
        return option.get();
    }

    public boolean existSkill(List<Skill> skills, String name) {
        try {
            findSkill(skills,name);
        } catch (UserException ex){
            return false;
        }
        return true;
    }
}
