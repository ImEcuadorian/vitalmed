package io.github.imecuadorian.vitalmed.factory;

import io.github.imecuadorian.vitalmed.repository.impl.*;
import io.github.imecuadorian.vitalmed.repository.interfaces.*;
import io.github.imecuadorian.vitalmed.service.*;
import io.github.imecuadorian.vitalmed.service.impl.*;
import lombok.*;

public class ServiceFactory {
    private static final UserRepository JDBC_USER_REPOSITORY = new JdbcUserRepository();
    private static final SpecialtyRepository JDBC_SPECIALTY_REPOSITORY = new JdbcSpecialtyRepository();
    private static final RoomRepository JDBC_ROOM_REPOSITORY = new JdbcRoomRepository();
    private static final HistoryRepository JDBC_HISTORY_REPOSITORY = new JdbcHistoryRepository();
    private static final ScheduleRepository JDBC_SCHEDULE_REPOSITORY = new JdbcScheduleRepository();
    private static final AppointmentSlotRepository JDBC_APPOINTMENT_SLOT_REPOSITORY = new JdbcAppointmentSlotRepository(JDBC_SCHEDULE_REPOSITORY);
    private static final AppointmentRepository JDBC_APPOINTMENT_REPOSITORY = new JdbcAppointmentRepository(JDBC_USER_REPOSITORY, JDBC_SPECIALTY_REPOSITORY, JDBC_SCHEDULE_REPOSITORY);
    private static final DoctorRepository JDBC_DOCTOR_REPOSITORY = new JdbcDoctorRepository(JDBC_SCHEDULE_REPOSITORY, JDBC_APPOINTMENT_SLOT_REPOSITORY, JDBC_APPOINTMENT_REPOSITORY, JDBC_HISTORY_REPOSITORY);
    @Getter
    private static final UserService USER_SERVICE = new UserServiceImpl(JDBC_USER_REPOSITORY);
    @Getter
    private static final AdminService ADMIN_SERVICE = new AdminServiceImpl(JDBC_USER_REPOSITORY, JDBC_DOCTOR_REPOSITORY, JDBC_SPECIALTY_REPOSITORY, JDBC_ROOM_REPOSITORY);
    @Getter
    private static final RoomService ROOM_SERVICE = new RoomServiceImpl(JDBC_ROOM_REPOSITORY);

    @Getter
    private static final DoctorService DOCTOR_SERVICE = new DoctorServiceImpl(JDBC_DOCTOR_REPOSITORY);
    @Getter
    private static final AppointmentService APPOINTMENT_SERVICE = new AppointmentServiceImpl(JDBC_APPOINTMENT_REPOSITORY);

    private ServiceFactory() {
        throw new UnsupportedOperationException("ServiceFactory is a utility class and cannot be instantiated.");
    }
}
