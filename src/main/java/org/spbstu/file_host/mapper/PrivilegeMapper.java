package org.spbstu.file_host.mapper;

import org.spbstu.file_host.dto.PrivilegeDto;
import org.spbstu.file_host.entity.Privilege;
import org.spbstu.file_host.mapper.abstractions.AbstractMapper;
import org.springframework.stereotype.Component;

@Component
public class PrivilegeMapper extends AbstractMapper<Privilege, PrivilegeDto> {
}
