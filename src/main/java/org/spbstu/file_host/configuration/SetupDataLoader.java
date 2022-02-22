package org.spbstu.file_host.configuration;


import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.spbstu.file_host.entity.abstraction.EntityType;
import org.spbstu.file_host.service.authority.PrivilegePreloaderService;
import org.spbstu.file_host.service.authority.abstraction.PreloadedEntityHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * Создание данных при запуске
 */
@Component
@RequiredArgsConstructor
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;
    @Setter(onMethod_ = {@Autowired})
    private Set<PreloadedEntityHolder<? extends EntityType>> preloadedEntityHolders;
    @Setter(onMethod_ = {@Autowired})
    private PrivilegePreloaderService privilegePreloaderService;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup)
            return;

        privilegePreloaderService.preload();
        preloadedEntityHolders.forEach(PreloadedEntityHolder::load);
        alreadySetup = true;
    }

}