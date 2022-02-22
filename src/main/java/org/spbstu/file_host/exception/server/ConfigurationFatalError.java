package org.spbstu.file_host.exception.server;

public class ConfigurationFatalError extends Error {
    private final String parameter;
    private final String value;
    private final String reason;

    public ConfigurationFatalError(String parameter, String value, String reason) {
        super(messageBuilder(parameter, value, reason));
        this.parameter = parameter;
        this.value = value;
        this.reason = reason;
    }

    public ConfigurationFatalError(String parameter, String value, Throwable cause) {
        super(messageBuilder(parameter, value, cause.getLocalizedMessage()), cause);
        this.parameter = parameter;
        this.value = value;
        this.reason = cause.getMessage();
    }

    private static String messageBuilder(String parameter, String value, String reason) {
        return "Не удалось запустить сервер. Значение параметра конфигурации '" + parameter + "' = '" + value
                + "' содержит критическую ошибку. Причина: " + reason;
    }

    public String getReason() {
        return reason;
    }

    public String getValue() {
        return value;
    }

    public String getParameter() {
        return parameter;
    }
}
