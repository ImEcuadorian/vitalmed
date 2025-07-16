package io.github.imecuadorian.vitalmed.factory;

import io.github.imecuadorian.vitalmed.repository.impl.*;
import io.github.imecuadorian.vitalmed.repository.interfaces.*;
import io.github.imecuadorian.vitalmed.service.*;
import io.github.imecuadorian.vitalmed.service.impl.*;
import lombok.*;

public class ServiceFactory {
    private static final UserRepository JDBC_USER_REPOSITORY = new JdbcUserRepository();
    private static final DoctorRepository JDBC_DOCTOR_REPOSITORY = new JdbcDoctorRepository();
    private static final SpecialtyRepository JDBC_SPECIALTY_REPOSITORY = new JdbcSpecialtyRepository();
    private static final RoomRepository JDBC_ROOM_REPOSITORY = new JdbcRoomRepository();
    @Getter
    private static final UserService USER_SERVICE = new UserServiceImpl(JDBC_USER_REPOSITORY);
    @Getter
    private static final AdminService ADMIN_SERVICE = new AdminServiceImpl(JDBC_USER_REPOSITORY, JDBC_DOCTOR_REPOSITORY, JDBC_SPECIALTY_REPOSITORY, JDBC_ROOM_REPOSITORY);
    @Getter
    private static final RoomService ROOM_SERVICE = new RoomServiceImpl(JDBC_ROOM_REPOSITORY);

    private ServiceFactory() {
        throw new UnsupportedOperationException("ServiceFactory is a utility class and cannot be instantiated.");
    }
}
