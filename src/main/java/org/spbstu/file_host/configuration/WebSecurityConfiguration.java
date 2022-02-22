package org.spbstu.file_host.configuration;

import lombok.RequiredArgsConstructor;
import org.spbstu.file_host.service.crud.UserAuthInfoCrudService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Класс-конфигуратор модуля защиты
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    protected final UserAuthInfoCrudService userService;

    /**
     * Бин конфигурирующий каким сервисом будет осуществляться работа с пользователями
     * и какое будет использовано шифрование пароля
     */
    @Bean
    public RestAuthenticationProvider restAuthenticationProvider() {
        RestAuthenticationProvider authProvider = new RestAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setHideUserNotFoundExceptions(false);
        return authProvider;
    }


    /**
     * Конфигурирование модуля защиты
     * Установка необходимых привелегий для доступа к ресурсам
     * Отключение лишних технологий
     * Описание процесса авторизации
     * Указание обработчиков для ситуаций удачной/неудачной авторизации, выхода из системы
     * Указание обработчика ситуации запрета доступа
     *
     * @param http контекст
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().cors().and().sessionManagement().maximumSessions(200);
        http.authorizeRequests()
                //.antMatchers("/api/v1/auth/**").permitAll()
                //.antMatchers("/api/v1/auth").permitAll()
                .antMatchers("/api/v1/file/**", "/api/v1/file/**/*", "/api/v1/file/**").permitAll()
                .antMatchers(HttpMethod.GET).permitAll()
                .antMatchers("/api/v1/auth/registration").permitAll()
                .anyRequest().authenticated()
                .and().formLogin()
                .loginProcessingUrl("/api/v1/auth/login").permitAll()
                .usernameParameter("login")
                .passwordParameter("password")
                .successHandler(new RestAuthenticationResultHandler())
                .failureHandler(new RestAuthenticationResultHandler())
                .and().logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/api/v1/auth/logout"))
                .logoutSuccessHandler(new RestLogoutResultHandler())
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .and().exceptionHandling().authenticationEntryPoint(new RestEntryPoint());
    }

    // Used by spring security if CORS is enabled.
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    /**
     * Установка конфигурации
     */
    @Override
    public void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(userService);
    }

}
