package org.spbstu.file_host.mapper;

import lombok.Setter;
import org.spbstu.file_host.dto.UserAuthInfoDto;
import org.spbstu.file_host.entity.UserAuthInfo;
import org.spbstu.file_host.mapper.abstractions.AbstractMapper;
import org.spbstu.file_host.repository.UserAuthInfoRepository;
import org.spbstu.file_host.service.crud.RoleCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Component
public class UserAuthInfoMapper extends AbstractMapper<UserAuthInfo, UserAuthInfoDto> {
    @Setter(onMethod_ = {@Autowired})
    private RoleCrudService roleCrudService;
    @Setter(onMethod_ = {@Autowired})
    private UserAuthInfoRepository userAuthInfoRepository;

    @PostConstruct
    public void setupMapper() {
        mapper.createTypeMap(UserAuthInfo.class, UserAuthInfoDto.class)
                .addMappings(m -> m.skip(UserAuthInfoDto::setRole))
                .setPostConverter(toDtoConverter());
        mapper.createTypeMap(UserAuthInfoDto.class, UserAuthInfo.class)
                .addMappings(m -> m.skip(UserAuthInfo::setPassword))
                .addMappings(m -> m.skip(UserAuthInfo::setRole))
                .setPostConverter(toEntityConverter());
    }

    @Override
    protected void mapSpecificFields(UserAuthInfo source, UserAuthInfoDto destination) {
        destination.setRole(Optional.ofNullable(source.getRole()).map(AbstractPersistable::getId).orElse(null));
    }

    @Override
    protected void mapSpecificFields(UserAuthInfoDto source, UserAuthInfo destination) {
        destination.setPassword(userAuthInfoRepository.getById(source.getId()).getPassword());
        destination.setRole(Optional.of(source)
                .map(UserAuthInfoDto::getRole)
                .flatMap(roleCrudService::get)
                .orElse(null));
    }
}
