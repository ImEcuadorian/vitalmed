SET default_storage_engine = InnoDB;

CREATE TABLE specialties
(
    id   INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
) ENGINE = InnoDB;

CREATE TABLE rooms
(
    id           INT AUTO_INCREMENT PRIMARY KEY,
    code         VARCHAR(20) NOT NULL UNIQUE,
    specialty_id INT         NOT NULL,
    CONSTRAINT fk_rooms_specialty
        FOREIGN KEY (specialty_id) REFERENCES specialties (id)
            ON UPDATE CASCADE
            ON DELETE RESTRICT
) ENGINE = InnoDB;

CREATE TABLE users
(
    id            INT AUTO_INCREMENT PRIMARY KEY,
    cedula        VARCHAR(10)                       NOT NULL UNIQUE,
    fullname      VARCHAR(150)                      NOT NULL,
    email         VARCHAR(100)                      NOT NULL UNIQUE,
    phone         VARCHAR(20),
    cell          VARCHAR(20),
    address       VARCHAR(255),
    password_hash VARCHAR(255)                      NOT NULL,
    role          ENUM ('ADMIN','DOCTOR','PATIENT') NOT NULL,
    created_at    TIMESTAMP                         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP                         NOT NULL
                                                             DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,
    CHECK (CHAR_LENGTH(password_hash) > 0)
) ENGINE = InnoDB;

CREATE TABLE schedules
(
    id           INT AUTO_INCREMENT PRIMARY KEY,
    doctor_id    INT                                                       NOT NULL,
    room_id      INT                                                       NOT NULL,
    day_of_week  ENUM ('MONDAY','TUESDAY','WEDNESDAY','THURSDAY','FRIDAY') NOT NULL,
    shift_number TINYINT                                                   NOT NULL,
    start_time   TIME                                                      NOT NULL,
    end_time     TIME                                                      NOT NULL,
    CONSTRAINT fk_schedule_doctor
        FOREIGN KEY (doctor_id) REFERENCES users (id)
            ON UPDATE CASCADE
            ON DELETE RESTRICT,
    CONSTRAINT fk_schedule_room
        FOREIGN KEY (room_id) REFERENCES rooms (id)
            ON UPDATE CASCADE
            ON DELETE RESTRICT,
    UNIQUE KEY u_schedule (doctor_id, day_of_week, shift_number),
    CHECK (shift_number BETWEEN 1 AND 4),
    CHECK (start_time < end_time)
) ENGINE = InnoDB;

CREATE TABLE appointment_slots
(
    id           INT AUTO_INCREMENT PRIMARY KEY,
    schedule_id  INT     NOT NULL,
    slot_time    TIME    NOT NULL,
    is_available BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_slot_schedule
        FOREIGN KEY (schedule_id) REFERENCES schedules (id)
            ON UPDATE CASCADE
            ON DELETE CASCADE,
    UNIQUE KEY u_slot (schedule_id, slot_time)
) ENGINE = InnoDB;

CREATE TABLE appointments
(
    id               INT AUTO_INCREMENT PRIMARY KEY,
    patient_id       INT       NOT NULL,
    doctor_id        INT       NOT NULL,
    slot_id          INT       NOT NULL,
    appointment_date DATE      NOT NULL,
    status           ENUM ('PROGRAMMED','ATTENDED','CANCELED')
                               NOT NULL DEFAULT 'PROGRAMMED',
    created_at       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_appt_patient
        FOREIGN KEY (patient_id) REFERENCES users (id)
            ON UPDATE CASCADE
            ON DELETE RESTRICT,
    CONSTRAINT fk_appt_doctor
        FOREIGN KEY (doctor_id) REFERENCES users (id)
            ON UPDATE CASCADE
            ON DELETE RESTRICT,
    CONSTRAINT fk_appt_slot
        FOREIGN KEY (slot_id) REFERENCES appointment_slots (id)
            ON UPDATE CASCADE
            ON DELETE RESTRICT,

    UNIQUE KEY u_appt_slot (slot_id)
) ENGINE = InnoDB;

CREATE TABLE clinical_history
(
    id            INT AUTO_INCREMENT PRIMARY KEY,
    patient_id    INT                            NOT NULL,
    record_number VARCHAR(20)                    NOT NULL,
    age           TINYINT                        NOT NULL,
    sex           ENUM ('MALE','FEMALE','OTHER') NOT NULL,
    allergies     TEXT,
    diseases      TEXT,
    medications   TEXT,
    surgeries     TEXT,
    created_at    TIMESTAMP                      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP                      NOT NULL
                                                          DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_history_patient
        FOREIGN KEY (patient_id) REFERENCES users (id)
            ON UPDATE CASCADE
            ON DELETE CASCADE
) ENGINE = InnoDB;


