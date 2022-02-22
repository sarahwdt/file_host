package org.spbstu.file_host.mapper;

import lombok.Setter;
import org.spbstu.file_host.dto.RoleDto;
import org.spbstu.file_host.entity.Role;
import org.spbstu.file_host.entity.abstraction.AbstractEntity;
import org.spbstu.file_host.mapper.abstractions.AbstractMapper;
import org.spbstu.file_host.service.crud.PrivilegeCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class RoleMapper extends AbstractMapper<Role, RoleDto> {
    @Setter(onMethod_ = {@Autowired})
    private PrivilegeCrudService privilegeCrudService;

    @PostConstruct
    public void setupMapper() {
        mapper.createTypeMap(Role.class, RoleDto.class)
                .addMappings(m -> m.skip(RoleDto::setPrivileges))
                .setPostConverter(toDtoConverter());
        mapper.createTypeMap(RoleDto.class, Role.class)
                .addMappings(m -> m.skip(Role::setPrivileges))
                .setPostConverter(toEntityConverter());
    }

    @Override
    protected void mapSpecificFields(Role source, RoleDto destination) {
        destination.setPrivileges(source.getPrivileges().stream()
                .map(AbstractEntity::getId)
                .collect(Collectors.toSet()));
    }

    @Override
    protected void mapSpecificFields(RoleDto source, Role destination) {
        destination.setPrivileges(source.getPrivileges().stream()
                .map(privilegeCrudService::get)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet()));
    }
}
