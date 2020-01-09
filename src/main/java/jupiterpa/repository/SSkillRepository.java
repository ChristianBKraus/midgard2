package jupiterpa.repository;

import jupiterpa.model.SSkill;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SSkillRepository extends MongoRepository<SSkill,String> {
}
