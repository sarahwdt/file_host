package org.spbstu.file_host.service.authority;

import org.spbstu.file_host.entity.Personal;
import org.spbstu.file_host.entity.Role;
import org.spbstu.file_host.entity.UserAuthInfo;
import org.spbstu.file_host.service.authority.abstraction.AbstractPreloadedRoleHolder;
import org.spbstu.file_host.service.authority.abstraction.AbstractPreloadedUserHolder;
import org.springframework.stereotype.Service;

@Service
public class PreloadedRootUserHolder extends AbstractPreloadedUserHolder {
    @Override
    protected UserAuthInfo getInstance() {
        return new UserAuthInfo("root", "password", new Personal(), new Role());
    }

    @Override
    protected String getUserIdentity() {
        return "root";
    }

    @Override
    protected Class<? extends AbstractPreloadedRoleHolder> getPreloadedRole() {
        return PreloadedAdminRoleHolder.class;
    }
}
