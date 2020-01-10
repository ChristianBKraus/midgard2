package jupiterpa.repository;

import jupiterpa.model.Character;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CharacterRepository extends MongoRepository<Character,String> {
    List<Character> findByName(String name);
    Optional<Character> findById(UUID id);
}
