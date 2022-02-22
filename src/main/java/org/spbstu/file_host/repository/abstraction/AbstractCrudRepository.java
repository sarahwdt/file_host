package org.spbstu.file_host.repository.abstraction;

import org.spbstu.file_host.entity.abstraction.AbstractEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface AbstractCrudRepository<E extends AbstractEntity>
        extends JpaRepository<E, Long> {
    Optional<E> findByIdAndDeletedFalse(Long id);

    Iterable<E> findByDeletedFalse(Sort sort);

    Page<E> findByDeletedFalse(Pageable pageable);

    boolean existsByIdAndDeletedFalse(Long id);

}