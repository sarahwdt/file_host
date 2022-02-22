package org.spbstu.file_host.service.crud;

import org.spbstu.file_host.entity.UserAuthInfo;
import org.spbstu.file_host.repository.UserAuthInfoRepository;
import org.spbstu.file_host.service.abstraction.crud.AbstractCrudService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Реализация сервиса, выполняющего операции с пользователями.
 */
@Service
public class UserAuthInfoCrudService extends AbstractCrudService<UserAuthInfo> implements UserDetailsService {
    /**
     * Выгрузка пользователя по логину
     *
     * @param login логин
     * @return пользователь в формате {@link UserDetails}
     * @throws UsernameNotFoundException в случае отсутствия пользователя в источнике данных
     */
    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return ((UserAuthInfoRepository) crudRepository).findByLoginAndDeletedFalse(login)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с логином " + login + " не найден"));
    }

    /**
     * Поиск пользователя по логину
     *
     * @param login логин
     * @return {@link Optional} с пользователем в формате {@link UserAuthInfo}
     */
    public Optional<UserAuthInfo> findByLogin(String login) {
        return ((UserAuthInfoRepository) crudRepository).findByLoginAndDeletedFalse(login);
    }
}
