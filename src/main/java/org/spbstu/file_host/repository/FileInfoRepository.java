package org.spbstu.file_host.repository;

import org.spbstu.file_host.entity.FileInfo;
import org.spbstu.file_host.repository.abstraction.AbstractCrudRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface FileInfoRepository extends AbstractCrudRepository<FileInfo> {
    Page<FileInfo> getAllByFileNameStartsWithAndDeletedFalse(Pageable pageable, String startsWith);
}
