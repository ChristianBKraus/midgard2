package jupiterpa.repository;

import jupiterpa.model.PlayerCharacter;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CharacterRepository extends MongoRepository<PlayerCharacter,String> {
    List<PlayerCharacter> findByName(String name);
    Optional<PlayerCharacter> findById(UUID id);
}
