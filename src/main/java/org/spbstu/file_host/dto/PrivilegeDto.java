package org.spbstu.file_host.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.spbstu.file_host.dto.abstraction.AbstractDto;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PrivilegeDto extends AbstractDto {
    private String name;
    private String description;
    private String authority;
}
