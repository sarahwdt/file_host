package org.spbstu.file_host.mapper;

import org.spbstu.file_host.dto.PersonalDto;
import org.spbstu.file_host.entity.Personal;
import org.spbstu.file_host.mapper.abstractions.AbstractMapper;
import org.springframework.stereotype.Component;

@Component
public class PersonalMapper extends AbstractMapper<Personal, PersonalDto> {
}
