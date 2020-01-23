package jupiterpa.repository;

import jupiterpa.model.PlayerCharacter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

@Document
public interface PlayerCharacterRepository extends MongoRepository<PlayerCharacter,String> {
    Optional<PlayerCharacter> findByName(String name);
}
