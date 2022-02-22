package org.spbstu.file_host.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.spbstu.file_host.dto.abstraction.AbstractDto;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class FileInfoDto extends AbstractDto {
    private String fileName;
    private String description;
    private String md5;
    private Long department;
    private UserAuthInfoDto createdBy;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime createdDate;
    private UserAuthInfoDto lastModifiedBy;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime lastModifiedDate;
    private Boolean exists;
}
