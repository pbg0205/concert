# secure_file_priv
SHOW VARIABLES LIKE 'secure_file_priv';

## select concerts_count
select count(id) from concert as concert_count;
select count(id) from concert_schedule as concert_schedule_count;
select count(id) from concert_seat as concert_seat_count;

## truncate concerts
truncate concert;
truncate concert_schedule;
truncate concert_seat;
