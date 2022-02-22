package org.spbstu.file_host.dto.abstraction;

import lombok.Data;

@Data
public abstract class AbstractDto implements DtoType {
    private Long id;
}
