package jupiterpa.repository;

import jupiterpa.model.DSkill;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DSkillRepository extends MongoRepository<Character,String> {
    List<DSkill> findByCharacterId(long id);
}
