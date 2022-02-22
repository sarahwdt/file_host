package org.spbstu.file_host.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.spbstu.file_host.dto.abstraction.AbstractDto;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserAuthInfoDto extends AbstractDto {
    private String login;
    private PersonalDto personal = new PersonalDto();
    private Long role;
}
