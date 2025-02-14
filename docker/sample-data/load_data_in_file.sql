### user

## user
LOAD DATA INFILE '/var/lib/mysql-files/sample-data/user_small.csv'
    INTO TABLE user
    FIELDS TERMINATED BY ','
    LINES TERMINATED BY '\n'
    IGNORE 1 ROWS
    (id, name, @alt_id, created_at, modified_at)
    SET alt_id = UNHEX(@alt_id);

## user_balance
LOAD DATA INFILE '/var/lib/mysql-files/sample-data/user_balance_small.csv'
    INTO TABLE user_balance
    FIELDS TERMINATED BY ','
    LINES TERMINATED BY '\n'
    IGNORE 1 ROWS
    (id, user_id, point, created_at, modified_at);

### concert

## concert_small
LOAD DATA INFILE '/var/lib/mysql-files/sample-data/concert_small.csv'
    INTO TABLE concert
    FIELDS TERMINATED BY ','
    LINES TERMINATED BY '\n'
    IGNORE 1 ROWS
    (id, concert_id, name, created_at, modified_at);

## concert_middle
LOAD DATA INFILE '/var/lib/mysql-files/sample-data/concert_medium.csv'
    INTO TABLE concert
    FIELDS TERMINATED BY ','
    LINES TERMINATED BY '\n'
    IGNORE 1 ROWS
    (id, concert_id, name, created_at, modified_at);

## concert_large
LOAD DATA INFILE '/var/lib/mysql-files/sample-data/concert_large.csv'
    INTO TABLE concert
    FIELDS TERMINATED BY ','
    LINES TERMINATED BY '\n'
    IGNORE 1 ROWS
    (id, concert_id, name, created_at, modified_at);

### concert_schedule

## concert_schedule_small
LOAD DATA INFILE '/var/lib/mysql-files/sample-data/concert_schedule_small.csv'
    INTO TABLE concert_schedule
    FIELDS TERMINATED BY ','
    LINES TERMINATED BY '\n'
    IGNORE 1 ROWS
    (id, concert_id, start_at, end_at, created_at, modified_at);


## concert_schedule_middle
LOAD DATA INFILE '/var/lib/mysql-files/sample-data/concert_schedule_medium.csv'
    INTO TABLE concert_schedule
    FIELDS TERMINATED BY ','
    LINES TERMINATED BY '\n'
    IGNORE 1 ROWS
    (id, concert_id, start_at, end_at, created_at, modified_at);

## concert_schedule_large
LOAD DATA INFILE '/var/lib/mysql-files/sample-data/concert_schedule_large.csv'
    INTO TABLE concert_schedule
    FIELDS TERMINATED BY ','
    LINES TERMINATED BY '\n'
    IGNORE 1 ROWS
    (id, concert_id, start_at, end_at, created_at, modified_at);

## concert_seat_small
LOAD DATA INFILE '/var/lib/mysql-files/sample-data/concert_seat_small.csv'
    INTO TABLE concert_seat
    FIELDS TERMINATED BY ','
    LINES TERMINATED BY '\n'
    IGNORE 1 ROWS
    (id,schedule_id,seat_number,status,price,created_at,modified_at);

## concert_seat_middle
LOAD DATA INFILE '/var/lib/mysql-files/sample-data/concert_seat_medium.csv'
    INTO TABLE concert_seat
    FIELDS TERMINATED BY ','
    LINES TERMINATED BY '\n'
    IGNORE 1 ROWS
    (id, schedule_id, seat_number, status, price, created_at, modified_at);

## concert_seat_large
LOAD DATA INFILE '/var/lib/mysql-files/sample-data/concert_seat_large.csv'
    INTO TABLE concert_seat
    FIELDS TERMINATED BY ','
    LINES TERMINATED BY '\n'
    IGNORE 1 ROWS
    (id, schedule_id, seat_number, status, price, created_at, modified_at);
