-- This file allow to write SQL commands that will be emitted in test and dev.
-- The commands are commented as their support depends of the database
insert into patient (id, name, surname, birthDate, ssn)
values(1, 'Name 1', 'Surname 1', '2022-12-17T09:03:15.142+00:00', 'SSN1');

insert into patient (id, name, surname, birthDate, ssn)
values(2, 'Name 2', 'Surname 2', '2022-12-17T09:03:15.142+00:00', 'SSN2');

insert into visit (id, patient_id, visit_type, visit_reason, date, family_history)
values(1, 2, 0, 1, '2022-12-17T09:03:15.142+00:00', 'Lorem ipsum dolor sit amet, consectetur adipisci');

alter sequence hibernate_sequence restart with 3;