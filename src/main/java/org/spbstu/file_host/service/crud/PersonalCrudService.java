package org.spbstu.file_host.service.crud;

import org.spbstu.file_host.entity.Personal;
import org.spbstu.file_host.service.abstraction.crud.AbstractCrudService;
import org.springframework.stereotype.Service;

/**
 * Реализация сервиса, выполняющего операции над персональными данными пользователя.
 * Полностью использует {@link AbstractCrudService}
 */
@Service
public class PersonalCrudService extends AbstractCrudService<Personal> {

}
