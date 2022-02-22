package org.spbstu.file_host.util.naming;

import org.spbstu.file_host.util.FileSystemObjectUtil;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component("directoryNamingStrategy")
public class TimeStampNamingStrategy implements NamingStrategy {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy_HHmmss_SSS");

    @Override
    public String apply(Stream<String> notAvailableNames, String baseName) {
        String newName = baseName;
        String rawName = FileSystemObjectUtil.removeExtension(baseName);
        String extension = FileSystemObjectUtil.getExtension(baseName);
        Set<String> names = notAvailableNames.collect(Collectors.toSet());
        while (names.contains(newName)) {
            newName = rawName
                    + "_" + LocalDateTime.now().plusSeconds((int) (Math.random() * 100)).format(formatter)
                    + extension;
        }
        return newName;
    }
}
