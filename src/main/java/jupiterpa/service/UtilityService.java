package jupiterpa.service;

import jupiterpa.model.Skill;

import java.util.List;

public interface UtilityService {
    Skill findSkill(List<Skill> skills, String name) throws UserException;
    boolean existSkill(List<Skill> skills, String name);
}
