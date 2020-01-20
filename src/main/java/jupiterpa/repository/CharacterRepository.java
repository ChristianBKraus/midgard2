package jupiterpa.repository;

import jupiterpa.model.PlayerCharacterEntity;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

@Document
public interface CharacterRepository extends MongoRepository<PlayerCharacterEntity,String> {
    Optional<PlayerCharacterEntity> findByName(String name);
}
