package org.spbstu.file_host.service.authority;

import lombok.Setter;
import org.spbstu.file_host.entity.Role;
import org.spbstu.file_host.service.authority.abstraction.AbstractPreloadedRoleHolder;
import org.spbstu.file_host.service.crud.PrivilegeCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class PreloadedAdminRoleHolder extends AbstractPreloadedRoleHolder {

    @Override
    protected Role getInstance() {
        return new Role("Администратор", "Имеет доступ к полному функционалу", "admin",
                new HashSet<>(privilegeCrudService.getAll()));
    }

    @Override
    protected String getRoleStringIdentity() {
        return "admin";
    }
}
