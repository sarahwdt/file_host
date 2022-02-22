package org.spbstu.file_host.exception.client;

import lombok.Getter;
import org.springframework.util.StringUtils;

@Getter
public class EntityNotFoundInDatabaseException extends ClientSideException {
    private final String entityType;
    private final Long id;

    public EntityNotFoundInDatabaseException(String entityType, Long id) {
        super(StringUtils.capitalize(entityType) + " с id " + id + " отсутствует в базе данных.");
        this.entityType = entityType;
        this.id = id;
    }
}
