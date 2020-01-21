package jupiterpa.service;

import jupiterpa.model.CostsClass;
import jupiterpa.model.CostsMain;
import jupiterpa.model.CostsSkill;
import jupiterpa.model.Skill;

import java.util.List;
import java.util.Map;

public interface SettingsService {
    Map<String, CostsMain> getMainCosts();
    Map<String, CostsClass> getClassCosts();
    Map<String, CostsSkill> getSkillCosts();
    List<String> getClasses();
    List<Skill>  getDefaultSkills() throws UserException;
}
