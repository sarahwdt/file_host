package org.spbstu.file_host.service.crud;

import lombok.Setter;
import org.spbstu.file_host.entity.Department;
import org.spbstu.file_host.service.UserContextService;
import org.spbstu.file_host.service.abstraction.crud.AbstractCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


/**
 * Реализация сервиса, выполняющего операции с сущностями департаментов. Помимо стандартного функционала указывает время
 * операции с департаментом и пользователя, выполнявшего операцию
 */
@Service
public class DepartmentCrudService extends AbstractCrudService<Department> {
    /**
     * Сервис взаимодействия с текущим контекстом пользователя
     */
    @Setter(onMethod_ = {@Autowired})
    private UserContextService userContextService;

    @Override
    public Department create(Department entity) {
        entity.setCreatedBy(userContextService.getCurrentUser().orElse(null));
        entity.setCreatedDate(LocalDateTime.now());
        return super.create(entity);
    }

    @Override
    public Department update(Department entity) {
        entity.setLastModifiedBy(userContextService.getCurrentUser().orElse(null));
        entity.setLastModifiedDate(LocalDateTime.now());
        return super.update(entity);
    }

    @Override
    public boolean delete(Department entity) {
        entity.setLastModifiedBy(userContextService.getCurrentUser().orElse(null));
        entity.setLastModifiedDate(LocalDateTime.now());
        return super.delete(entity);
    }
}
