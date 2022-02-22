package org.spbstu.file_host.repository;

import org.spbstu.file_host.entity.Department;
import org.spbstu.file_host.repository.abstraction.AbstractCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends AbstractCrudRepository<Department> {
}
