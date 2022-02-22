package org.spbstu.file_host.service.crud;

import lombok.Setter;
import org.spbstu.file_host.entity.Role;
import org.spbstu.file_host.repository.RoleRepository;
import org.spbstu.file_host.service.abstraction.crud.AbstractCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Реализация сервиса, выполняющего операции над персональными данными пользователя.
 * Полностью использует {@link AbstractCrudService}
 */
@Service
public class RoleCrudService extends AbstractCrudService<Role> {
    @Setter(onMethod_ = {@Autowired})
    private RoleRepository roleRepository;

    public Optional<Role> findRoleByName(String role) {
        return roleRepository.findByRole(role);
    }
}
