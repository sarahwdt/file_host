package org.spbstu.file_host.service.crud;

import lombok.Setter;
import org.spbstu.file_host.entity.Privilege;
import org.spbstu.file_host.repository.PrivilegeRepository;
import org.spbstu.file_host.service.abstraction.crud.AbstractCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Реализация сервиса, выполняющего операции над персональными данными пользователя.
 * Полностью использует {@link AbstractCrudService}
 */
@Service
public class PrivilegeCrudService extends AbstractCrudService<Privilege> {
    @Setter(onMethod_ = {@Autowired})
    private PrivilegeRepository privilegeRepository;

    public Optional<Privilege> findByAuthority(String authority) {
        return privilegeRepository.findByAuthority(authority);
    }
}
