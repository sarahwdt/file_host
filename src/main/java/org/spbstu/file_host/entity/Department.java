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
public class Department extends AbstractEntity {
    private String name;
    private String cabinet;
    @ManyToOne
    private UserAuthInfo createdBy;
    private LocalDateTime createdDate;
    @ManyToOne
    private UserAuthInfo lastModifiedBy;
    private LocalDateTime lastModifiedDate;
}
