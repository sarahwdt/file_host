package org.spbstu.file_host.mapper;

import org.spbstu.file_host.dto.DepartmentDto;
import org.spbstu.file_host.entity.Department;
import org.spbstu.file_host.mapper.abstractions.AbstractMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DepartmentMapper extends AbstractMapper<Department, DepartmentDto> {
    @PostConstruct
    public void setupMapper() {
        mapper.createTypeMap(Department.class, DepartmentDto.class)
                .addMappings(m -> m.skip(DepartmentDto::setCreatedDate))
                .addMappings(m -> m.skip(DepartmentDto::setLastModifiedDate))
                .setPostConverter(toDtoConverter());
        mapper.createTypeMap(DepartmentDto.class, Department.class)
                .addMappings(m -> m.skip(Department::setCreatedDate))
                .addMappings(m -> m.skip(Department::setLastModifiedDate))
                .setPostConverter(toEntityConverter());
    }

    @Override
    protected void mapSpecificFields(Department source, DepartmentDto destination) {
        destination.setLastModifiedDate(source.getLastModifiedDate());
        destination.setCreatedDate(source.getCreatedDate());
    }

    @Override
    protected void mapSpecificFields(DepartmentDto source, Department destination) {
        destination.setLastModifiedDate(source.getLastModifiedDate());
        destination.setCreatedDate(source.getCreatedDate());
    }
}
