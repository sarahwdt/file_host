package org.spbstu.file_host.entity.abstraction;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Getter
@Setter
public abstract class AbstractEntity extends AbstractPersistable<Long> implements EntityType {
    private boolean deleted = false;
}