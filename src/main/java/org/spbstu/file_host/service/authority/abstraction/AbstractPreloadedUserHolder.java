package org.spbstu.file_host.service.authority.abstraction;

import lombok.Setter;
import org.spbstu.file_host.entity.UserAuthInfo;
import org.spbstu.file_host.service.crud.UserAuthInfoCrudService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

public abstract class AbstractPreloadedUserHolder implements PreloadedEntityHolder<UserAuthInfo> {
    @Setter(onMethod_ = {@Autowired})
    protected UserAuthInfoCrudService userAuthInfoCrudService;
    @Setter(onMethod_ = {@Autowired})
    protected Set<AbstractPreloadedRoleHolder> preloadedRoles;

    @Override
    public UserAuthInfo load() {
        return userAuthInfoCrudService.findByLogin(getUserIdentity())
                .orElseGet(() -> {
                    UserAuthInfo userAuthInfo = getInstance();
                    userAuthInfo.setLogin(getUserIdentity());
                    userAuthInfo.setPassword("{noop}" + userAuthInfo.getPassword());
                    userAuthInfo.setRole(preloadedRoles.stream()
                            .filter(preloadedRole -> getPreloadedRole().equals(preloadedRole.getClass()))
                            .findFirst().map(AbstractPreloadedRoleHolder::load).orElseThrow());
                    return userAuthInfoCrudService.create(userAuthInfo);
                });
    }


    protected abstract UserAuthInfo getInstance();

    protected abstract String getUserIdentity();

    protected abstract Class<? extends AbstractPreloadedRoleHolder> getPreloadedRole();


}
