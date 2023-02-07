package ruijosecj.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ruijosecj.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>{

}
