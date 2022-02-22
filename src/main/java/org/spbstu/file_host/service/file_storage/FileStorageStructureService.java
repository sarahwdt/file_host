package org.spbstu.file_host.service.file_storage;

import lombok.RequiredArgsConstructor;
import org.spbstu.file_host.entity.FileInfo;
import org.spbstu.file_host.service.crud.FileInfoCrudService;
import org.spbstu.file_host.service.file_system.VirtualFileSystemStructureService;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileStorageStructureService {
    private final VirtualFileSystemStructureService virtualFileSystemStructureService;
    private final FileInfoCrudService fileInfoCrudService;

    public Set<Path> getAvailableDirectories() {
        Set<Path> directoriesFromDatabase = fileInfoCrudService.getAll().stream()
                .map(FileInfo::getPath)
                .map(Path::of)
                .map(Path::getParent)
                .collect(Collectors.toSet());
        return virtualFileSystemStructureService.getAllDirectoriesAsStream().filter(directoriesFromDatabase::contains)
                .collect(Collectors.toSet());
    }
}
