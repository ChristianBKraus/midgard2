package jupiterpa.service;

import jupiterpa.model.Skill;
import jupiterpa.model.SkillEntity;

import java.util.List;

public interface UtilityService {
    Skill findSkill(List<Skill> skills, String name) throws Exception;
    boolean existSkill(List<Skill> skills, String name);

    SkillEntity findSkillEntity(List<SkillEntity> skills, String name) throws Exception;
    boolean existSkillEntity(List<SkillEntity> skills, String name);
}
