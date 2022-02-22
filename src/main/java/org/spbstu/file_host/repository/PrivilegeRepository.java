package org.spbstu.file_host.repository;

import org.spbstu.file_host.entity.Privilege;
import org.spbstu.file_host.repository.abstraction.AbstractCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrivilegeRepository extends AbstractCrudRepository<Privilege> {
    Optional<Privilege> findByAuthority(String authority);
}
