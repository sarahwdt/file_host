package org.spbstu.file_host.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.spbstu.file_host.dto.abstraction.AbstractDto;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class DepartmentDto extends AbstractDto {
    private String name;
    private String cabinet;
    private UserAuthInfoDto createdBy;
    private LocalDateTime createdDate;
    private UserAuthInfoDto lastModifiedBy;
    private LocalDateTime lastModifiedDate;
}
