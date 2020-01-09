package jupiterpa.repository;

import jupiterpa.model.SClass;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SClassRepository extends MongoRepository<SClass,String> {
}
