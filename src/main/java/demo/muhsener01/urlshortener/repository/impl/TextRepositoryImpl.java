package demo.muhsener01.urlshortener.repository.impl;

import demo.muhsener01.urlshortener.domain.entity.Text;
import demo.muhsener01.urlshortener.repository.TextRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class TextRepositoryImpl implements TextRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Text save(Text text) {
        entityManager.persist(text);
        entityManager.flush();

        return text;
    }
}
