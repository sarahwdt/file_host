package org.spbstu.file_host.repository;

import org.spbstu.file_host.entity.UserAuthInfo;
import org.spbstu.file_host.repository.abstraction.AbstractCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAuthInfoRepository extends AbstractCrudRepository<UserAuthInfo> {
    Optional<UserAuthInfo> findByLoginAndDeletedFalse(String login);
}
