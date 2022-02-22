package org.spbstu.file_host.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.spbstu.file_host.entity.abstraction.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Role extends AbstractEntity {
    private String name;
    private String description;
    @Column(unique = true)
    private String role;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Privilege> privileges;
}
