package jupiterpa.repository;

import jupiterpa.model.SCost;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SCostRepository extends MongoRepository<SCost,String> {
}
