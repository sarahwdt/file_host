package org.spbstu.file_host.entity;

import lombok.*;
import org.spbstu.file_host.entity.abstraction.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileInfo extends AbstractEntity {
    private String fileName;
    private String description;
    private String path;
    private String md5;
    @ManyToOne
    private Department department;
    @ManyToOne
    private UserAuthInfo createdBy;
    private LocalDateTime createdDate;
    @ManyToOne
    private UserAuthInfo lastModifiedBy;
    private LocalDateTime lastModifiedDate;
    public FileInfo(String fileName, String description, String path, String md5, Department department) {
        this.description = description;
        this.fileName = fileName;
        this.path = path;
        this.md5 = md5;
        this.department = department;
    }
}