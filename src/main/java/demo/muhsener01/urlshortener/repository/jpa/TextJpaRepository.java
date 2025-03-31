package demo.muhsener01.urlshortener.repository.jpa;

import demo.muhsener01.urlshortener.domain.entity.Text;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TextJpaRepository extends JpaRepository<Text, String> {

    Optional<Text> findById(String id);


}
