package org.spbstu.file_host.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.spbstu.file_host.controller.abstraction.AbstractRoController;
import org.spbstu.file_host.dto.FileInfoDto;
import org.spbstu.file_host.entity.FileInfo;
import org.spbstu.file_host.exception.client.ClientSideException;
import org.spbstu.file_host.exception.client.EntityNotFoundInDatabaseException;
import org.spbstu.file_host.service.authority.abstraction.Secured;
import org.spbstu.file_host.service.crud.FileInfoCrudService;
import org.spbstu.file_host.service.file_storage.FileStorageService;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;

@Log4j2
@RestController
@RequestMapping("/api/v1/file")
@RequiredArgsConstructor
public class FileStorageController extends AbstractRoController<FileInfo, FileInfoDto> implements Secured {
    private final FileStorageService fileStorageService;
    private final FileInfoCrudService fileInfoCrudService;

    @PreAuthorize("permitAll()")
    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> download(@PathVariable long id, HttpServletRequest request)
            throws MalformedURLException, ClientSideException {
        Resource resource = fileStorageService.getFileAsResource(id);

        MediaType mediaType;
        try {
            mediaType = MediaType.parseMediaType(request.getServletContext().getMimeType(resource.getFile().getAbsolutePath()));
        } catch (InvalidMediaTypeException | IOException ex) {
            mediaType = MediaType.APPLICATION_OCTET_STREAM;
        }

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/filter")
    public ResponseEntity<Page<FileInfoDto>> getAllStartsWithAsPage(Pageable pageable, @RequestParam(required = false) String startsWith) {
        return ResponseEntity.ok(fileInfoCrudService.getAllStartsWith(pageable, startsWith).map(mapper::toDto));
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/upload")
    public ResponseEntity<FileInfoDto> upload(@RequestPart FileInfoDto dto, @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(mapper.toDto(fileStorageService.storeFile(mapper.toEntity(dto), file)));
    }

    @PreAuthorize("hasPermission(this, 'delete')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) throws EntityNotFoundInDatabaseException {
        return fileStorageService.deleteFile(id) ? ResponseEntity.ok().build()
                : ResponseEntity.internalServerError().build();
    }

    @PreAuthorize("hasPermission(this, 'update')")
    @PutMapping("/{id}")
    public ResponseEntity<FileInfoDto> update(@PathVariable long id, @RequestBody FileInfoDto dto) {
        return roService.get(id)
                .map(oldEntity -> mapper
                        .toDto(fileInfoCrudService
                                .update(mapper
                                        .toEntity(dto))))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("permitAll()")
    @Override
    public ResponseEntity<Page<FileInfoDto>> get(Pageable pageable) {
        return super.get(pageable);
    }

    @PreAuthorize("permitAll()")
    @Override
    public ResponseEntity<FileInfoDto> get(Long id) {
        return super.get(id);
    }

    @Override
    public String getAuthorityPrefix() {
        return "file";
    }

}
