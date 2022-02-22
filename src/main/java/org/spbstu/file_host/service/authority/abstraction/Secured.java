package org.spbstu.file_host.service.authority.abstraction;

import java.util.Set;

public interface Secured {
    String getAuthorityPrefix();

    default Set<String> getActions() {
        return Set.of("create", "update", "delete", "read");
    }
}
