package org.spbstu.file_host.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.spbstu.file_host.entity.abstraction.AbstractEntity;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Privilege extends AbstractEntity implements GrantedAuthority {
    private String name;
    private String description;
    @Column(unique = true)
    private String authority;

    @Override
    public String getAuthority() {
        return authority;
    }
}
