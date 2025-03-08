CREATE TABLE IF NOT EXISTS shedlock
(
    name       VARCHAR(64)  NOT NULL,
    lock_until TIMESTAMP(3) NOT NULL,
    locked_at  TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    locked_by  VARCHAR(255) NOT NULL,
    PRIMARY KEY (name)
);

CREATE TABLE IF NOT EXISTS `concert`
(
    `concert_id`  bigint                                  NOT NULL,
    `created_at`  bigint                                  NOT NULL DEFAULT '0',
    `id`          bigint                                  NOT NULL AUTO_INCREMENT,
    `modified_at` bigint                                  NOT NULL DEFAULT '0',
    `name`        varchar(300) COLLATE utf8mb4_unicode_ci NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci

CREATE TABLE IF NOT EXISTS `concert_schedule`
(
    `concert_id`  bigint NOT NULL,
    `created_at`  bigint NOT NULL DEFAULT '0',
    `end_at`      bigint NOT NULL DEFAULT '0',
    `id`          bigint NOT NULL AUTO_INCREMENT,
    `modified_at` bigint NOT NULL DEFAULT '0',
    `start_at`    bigint NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci

CREATE TABLE `concert_seat`
(
    `version`     int                                    NOT NULL DEFAULT '0',
    `created_at`  bigint                                 NOT NULL DEFAULT '0',
    `id`          bigint                                 NOT NULL AUTO_INCREMENT,
    `modified_at` bigint                                 NOT NULL DEFAULT '0',
    `price`       bigint                                 NOT NULL DEFAULT '0',
    `schedule_id` bigint                                 NOT NULL,
    `seat_number` bigint                                 NOT NULL,
    `status`      varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_seat_status` (`status`, `schedule_id`),
    CONSTRAINT `concert_seat_chk_1` CHECK ((`status` in (_utf8mb4'AVAILABLE', _utf8mb4'UNAVAILABLE')))
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci

CREATE TABLE `payment`
(
    `created_at`     bigint                                 NOT NULL DEFAULT '0',
    `id`             bigint                                 NOT NULL AUTO_INCREMENT,
    `modified_at`    bigint                                 NOT NULL DEFAULT '0',
    `reservation_id` bigint                                 NOT NULL,
    `alt_id`         binary(16)                             NOT NULL,
    `status`         varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UKsc71no2id16p16rpkscw72y9c` (`alt_id`),
    CONSTRAINT `payment_chk_1` CHECK ((`status` in (_utf8mb4'PENDING', _utf8mb4'COMPLETED', _utf8mb4'CANCELLED')))
) ENGINE = InnoDB
  AUTO_INCREMENT = 101
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci

CREATE TABLE `queue_token_expire_outbox`
(
    `created_at`  bigint                                  NOT NULL DEFAULT '0',
    `id`          bigint                                  NOT NULL AUTO_INCREMENT,
    `modified_at` bigint                                  NOT NULL DEFAULT '0',
    `payment_id`  binary(16)                              NOT NULL,
    `payload`     varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `topic`       varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `type`        varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci

CREATE TABLE `reservation`
(
    `created_at`  bigint                                 NOT NULL DEFAULT '0',
    `id`          bigint                                 NOT NULL AUTO_INCREMENT,
    `modified_at` bigint                                 NOT NULL DEFAULT '0',
    `seat_id`     bigint                                 NOT NULL,
    `user_id`     bigint                                 NOT NULL,
    `alt_id`      binary(16)                             NOT NULL,
    `status`      varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UKe1cb7m01argyt7ese1qt39ms7` (`alt_id`),
    CONSTRAINT `reservation_chk_1` CHECK ((`status` in (_utf8mb4'PENDING', _utf8mb4'CANCELED', _utf8mb4'RESERVED')))
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci

CREATE TABLE `user`
(
    `created_at`  bigint                                  NOT NULL DEFAULT '0',
    `id`          bigint                                  NOT NULL AUTO_INCREMENT,
    `modified_at` bigint                                  NOT NULL DEFAULT '0',
    `alt_id`      binary(16)                              NOT NULL,
    `name`        varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK5k2h6ko48u39foc4de0q7i6xb` (`alt_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci

CREATE TABLE `user_balance`
(
    `created_at`  bigint NOT NULL DEFAULT '0',
    `id`          bigint NOT NULL AUTO_INCREMENT,
    `modified_at` bigint NOT NULL DEFAULT '0',
    `point`       bigint          DEFAULT NULL,
    `user_id`     bigint NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UKm97fk95b7xaspv6kejmtoneww` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci

