package jupiterpa.service;

import jupiterpa.model.*;

import java.util.List;
import java.util.Map;

public interface SettingsService {
    Map<String, CostsMain> getMainCosts();
    Map<String, CostsClass> getClassCosts();
    Map<String, CostsSkill> getSkillCosts();
    List<String> getClasses();
    List<Skill>  getDefaultSkills() throws UserException;
    Map<String, CostsLevel> getLevelCosts();
}
