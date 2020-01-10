package jupiterpa.repository;

import jupiterpa.model.Character;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CharacterRepository extends MongoRepository<Character,String> {
    List<Character> findByName(String name);
}
