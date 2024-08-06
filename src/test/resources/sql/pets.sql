INSERT INTO adoption (id, adoption_date_time)
VALUES (1, '2024-01-01T00:00:00');

INSERT INTO status (id, cleanliness, happiness, hunger, tiredness)
VALUES (1, 100, 100, 100, 100);

INSERT INTO pet (id, taxon, species, name, birth_date, adoption_id, status_id)
VALUES (1, 0, 2, 'Buddy', '2020-01-01', null, null),
       (2, 1, 5, 'Penny', '2018-02-01', 1, 1);