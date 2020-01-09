package jupiterpa.repository;

import jupiterpa.model.DCharacter;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DCharacterRepository extends MongoRepository<DCharacter,String> {
    List<DCharacter> findByName(String name);
}
