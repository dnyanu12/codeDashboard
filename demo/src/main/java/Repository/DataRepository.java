package Repository;

import io.demo.DataInput1;
import model.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface DataRepository extends JpaRepository<Data,Long> {

}
