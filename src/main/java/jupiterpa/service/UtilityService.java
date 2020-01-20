package jupiterpa.service;

import jupiterpa.model.Skill;

import java.util.List;

public interface UtilityService {
    Skill findSkill(List<Skill> skills, String name) throws Exception;
    boolean existSkill(List<Skill> skills, String name);
}
