package org.spbstu.file_host.service.abstraction.crud;

import lombok.Setter;
import org.spbstu.file_host.entity.abstraction.AbstractEntity;
import org.spbstu.file_host.repository.abstraction.AbstractCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Частичная реализация интерфейса получения сущностей из источника данных
 *
 * @param <E> {@link AbstractEntity} класс сущности
 */
@Service
public abstract class AbstractRoService<E extends AbstractEntity> implements RoService<E> {

    /**
     * Репозиторий, для взаимодействия с данными
     */
    @Setter(onMethod_ = {@Autowired})
    protected AbstractCrudRepository<E> crudRepository;

    public Optional<E> get(Long id) {
        return crudRepository.findByIdAndDeletedFalse(id);
    }

    public Page<E> getAll(Pageable pageable) {
        return crudRepository.findByDeletedFalse(pageable);
    }

    public Page<E> getAllAsOnePage() {
        return getAll(PageRequest.of(0, (int) crudRepository.count()));
    }

    public List<E> getAll() {
        return crudRepository.findAll();
    }
}
