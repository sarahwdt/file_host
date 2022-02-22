package org.spbstu.file_host.util.naming;

import java.util.function.BiFunction;
import java.util.stream.Stream;

public interface NamingStrategy extends BiFunction<Stream<String>, String, String> {
    @Override
    String apply(Stream<String> notAvailableNames, String baseName);
}
