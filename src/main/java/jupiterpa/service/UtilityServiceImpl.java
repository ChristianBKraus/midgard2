package jupiterpa.service;

import jupiterpa.model.Skill;
import jupiterpa.model.SkillEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UtilityServiceImpl implements UtilityService {

    public Skill findSkill(List<Skill> skills, String name) throws Exception {
        Optional<Skill> option = skills.stream().filter( s -> name.equals(s.getName()) ).findFirst();
        if (! option.isPresent()) throw new Exception("Skill does not exist");
        return option.get();
    }

    public boolean existSkill(List<Skill> skills, String name) {
        try {
            findSkill(skills,name);
        } catch (Exception ex){
            return false;
        }
        return true;
    }

    public SkillEntity findSkillEntity(List<SkillEntity> skills, String name) throws Exception {
        Optional<SkillEntity> option = skills.stream().filter( s -> name.equals(s.getName()) ).findFirst();
        if (! option.isPresent()) throw new Exception("Skill does not exist");
        return option.get();
    }
}
