package io.github.imecuadorian.vitalmed.factory;

import io.github.imecuadorian.vitalmed.repository.impl.*;
import io.github.imecuadorian.vitalmed.repository.interfaces.*;
import io.github.imecuadorian.vitalmed.service.*;
import io.github.imecuadorian.vitalmed.service.impl.*;
import lombok.*;

public class ServiceFactory {
    private static final UserRepository JDBC_USER_REPOSITORY = new JdbcUserRepository();

    @Getter
    private static final UserService USER_SERVICE = new UserServiceImpl(JDBC_USER_REPOSITORY);

    private ServiceFactory() {
        throw new UnsupportedOperationException("ServiceFactory is a utility class and cannot be instantiated.");
    }
}
