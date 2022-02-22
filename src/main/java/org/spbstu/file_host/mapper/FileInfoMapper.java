package org.spbstu.file_host.mapper;

import lombok.Setter;
import org.spbstu.file_host.dto.FileInfoDto;
import org.spbstu.file_host.entity.Department;
import org.spbstu.file_host.entity.FileInfo;
import org.spbstu.file_host.mapper.abstractions.AbstractMapper;
import org.spbstu.file_host.repository.DepartmentRepository;
import org.spbstu.file_host.service.file_system.VirtualFileSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.file.Path;
import java.util.Optional;

@Component
public class FileInfoMapper extends AbstractMapper<FileInfo, FileInfoDto> {
    @Setter(onMethod_ = {@Autowired})
    private DepartmentRepository departmentRepository;
    @Setter(onMethod_ = {@Autowired})
    private VirtualFileSystemService virtualFileSystemService;

    @PostConstruct
    public void setupMapper() {
        mapper.createTypeMap(FileInfo.class, FileInfoDto.class)
                .addMappings(m -> m.skip(FileInfoDto::setCreatedDate))
                .addMappings(m -> m.skip(FileInfoDto::setLastModifiedDate))
                .addMappings(m -> m.skip(FileInfoDto::setDepartment))
                .addMappings(m -> m.skip(FileInfoDto::setExists))
                .setPostConverter(toDtoConverter());
        mapper.createTypeMap(FileInfoDto.class, FileInfo.class)
                .addMappings(m -> m.skip(FileInfo::setCreatedDate))
                .addMappings(m -> m.skip(FileInfo::setLastModifiedDate))
                .addMappings(m -> m.skip(FileInfo::setDepartment))
                .setPostConverter(toEntityConverter());
    }

    @Override
    protected void mapSpecificFields(FileInfo source, FileInfoDto destination) {
        destination.setLastModifiedDate(source.getLastModifiedDate());
        destination.setCreatedDate(source.getCreatedDate());
        destination.setExists(virtualFileSystemService.checkFile(Path.of(source.getPath())));
        Optional.ofNullable(source.getDepartment()).map(Department::getId).ifPresent(destination::setDepartment);
    }

    @Override
    protected void mapSpecificFields(FileInfoDto source, FileInfo destination) {
        destination.setLastModifiedDate(source.getLastModifiedDate());
        destination.setCreatedDate(source.getCreatedDate());
        Optional.ofNullable(source.getDepartment()).map(departmentRepository::findById).flatMap(department -> department)
                .ifPresent(destination::setDepartment);
    }
}
