package jupiterpa.repository;

import jupiterpa.model.PlayerCharacterEntity;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Document
public interface CharacterRepository extends MongoRepository<PlayerCharacterEntity,String> {
    List<PlayerCharacterEntity> findByName(String name);
    Optional<PlayerCharacterEntity> findById(UUID id);
}
