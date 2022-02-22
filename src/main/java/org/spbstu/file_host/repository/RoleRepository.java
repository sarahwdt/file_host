package org.spbstu.file_host.repository;

import org.spbstu.file_host.entity.Role;
import org.spbstu.file_host.repository.abstraction.AbstractCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends AbstractCrudRepository<Role> {
    Optional<Role> findByRole(String role);
}
