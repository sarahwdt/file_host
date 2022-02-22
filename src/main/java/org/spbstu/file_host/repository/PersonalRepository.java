package org.spbstu.file_host.repository;

import org.spbstu.file_host.entity.Personal;
import org.spbstu.file_host.repository.abstraction.AbstractCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalRepository extends AbstractCrudRepository<Personal> {
}
