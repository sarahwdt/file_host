package org.spbstu.file_host.service.authority.abstraction;

import org.spbstu.file_host.entity.abstraction.EntityType;

public interface PreloadedEntityHolder<Entity extends EntityType> {
    Entity load();
}
