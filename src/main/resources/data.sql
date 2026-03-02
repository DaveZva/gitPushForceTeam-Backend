INSERT INTO _user (id, first_name, last_name, email, password, require_password_change, role) VALUES
                                                                                                  (1, 'Hlavní', 'Administrátor', 'admin@admin.cz', '$2a$12$d913m8E6ceUSgLYls2XwOO9FdH8wRHPjnCqyaqf9vgjo.M5u9ieD.', false, 'ADMIN'),
                                                                                                  (2, 'Výstavní', 'Sekretariát', 'sekretariat@catshow.cz', '$2a$12$d913m8E6ceUSgLYls2XwOO9FdH8wRHPjnCqyaqf9vgjo.M5u9ieD.', false, 'SECRETARIAT'),
                                                                                                  (3, 'Jana', 'Nováková', 'jana.novakova@email.cz', '$2a$10$8.UnVuG9HLROJOsIpiNdqO6.E9.TjT9v5xQe7/K7c0E9TjT9v5xQe7/K', false, 'USER'),
                                                                                                  (4, 'Pavel', 'Dvořák', 'pavel.dvorak@email.cz', '$2a$10$8.UnVuG9HLROJOsIpiNdqO6.E9.TjT9v5xQe7/K7c0E9TjT9v5xQe7/K', false, 'USER'),
                                                                                                  (5, 'Eliška', 'Svobodová', 'eliska.svobodova@email.cz', '$2a$10$8.UnVuG9HLROJOsIpiNdqO6.E9.TjT9v5xQe7/K7c0E9TjT9v5xQe7/K', false, 'USER'),
                                                                                                  (6, 'Tomáš', 'Kučera', 'tomas.kucera@email.cz', '$2a$10$8.UnVuG9HLROJOsIpiNdqO6.E9.TjT9v5xQe7/K7c0E9TjT9v5xQe7/K', false, 'USER'),
                                                                                                  (7, 'Martin', 'Zelený', 'martin.zeleny@email.cz', '$2a$10$8.UnVuG9HLROJOsIpiNdqO6.E9.TjT9v5xQe7/K7c0E9TjT9v5xQe7/K', false, 'USER'),
                                                                                                  (8, 'Lucie', 'Černá', 'lucie.cerna@email.cz', '$2a$10$8.UnVuG9HLROJOsIpiNdqO6.E9.TjT9v5xQe7/K7c0E9TjT9v5xQe7/K', false, 'USER'),
                                                                                                  (9, 'Petra', 'Veselá', 'petra.vesela@email.cz', '$2a$10$8.UnVuG9HLROJOsIpiNdqO6.E9.TjT9v5xQe7/K7c0E9TjT9v5xQe7/K', false, 'USER'),
                                                                                                  (10, 'Karel', 'Novotný', 'karel.novotny@email.cz', '$2a$10$8.UnVuG9HLROJOsIpiNdqO6.E9.TjT9v5xQe7/K7c0E9TjT9v5xQe7/K', false, 'USER');

INSERT INTO judges (id, first_name, last_name, email, country) VALUES
                                                                   (1, 'Albert', 'Kurkowski', 'albert.kurkowski@fifeshow.pl', 'PL'),
                                                                   (2, 'Eric', 'Reijers', 'eric.reijers@fifeshow.cz', 'CZ'),
                                                                   (3, 'Luigi', 'Comorio', 'luigi.comorio@fifeshow.it', 'IT'),
                                                                   (4, 'Helena', 'Konečná', 'helena.konecna@fifeshow.cz', 'CZ'),
                                                                   (5, 'Vladimir', 'Isakov', 'vladimir@fifeshow.by', 'BY'),
                                                                   (6, 'Laura', 'Burani', 'laura@fifeshow.it', 'IT'),
                                                                   (7, 'Satu', 'Hämäläinen', 'satu@fifeshow.fi', 'FI'),
                                                                   (8, 'Martin', 'Urban', 'martin@fifeshow.cz', 'CZ'),
                                                                   (9, 'Eva', 'Porubská', 'eva@fifeshow.sk', 'SK'),
                                                                   (10, 'Karl', 'Preiss', 'karl@fifeshow.at', 'AT');

INSERT INTO judge_valid_groups (judge_id, group_code) VALUES
                                                          (1, '1'), (1, '2'), (1, '3'), (1, '4'),
                                                          (2, '1'), (2, '2'),
                                                          (3, '3'), (3, '4'),
                                                          (4, '1'), (4, '2'), (4, '3'), (4, '4'),
                                                          (5, '1'), (5, '2'), (5, '3'),
                                                          (6, '2'), (6, '3'), (6, '4'),
                                                          (7, '1'), (7, '4'),
                                                          (8, '1'), (8, '2'), (8, '3'), (8, '4'),
                                                          (9, '2'), (9, '3'),
                                                          (10, '1'), (10, '2'), (10, '3'), (10, '4');

INSERT INTO shows (id, name, description, status, venue_name, venue_address, venue_city, venue_state, venue_zip, start_date, end_date, registration_deadline, organizer_name, organizer_contact_email, organizer_website_url, max_cats) VALUES
                                                                                                                                                                                                                                            (1, 'Jarní výstava koček Praha', 'Výstava s kapacitou 50 koček', 'OPEN', 'Hala 1', 'Ulice 1', 'Praha', 'CZ', '10000', '2026-04-10 08:00:00', '2026-04-11 18:00:00', '2026-03-30 23:59:59', 'ZO Praha', 'info@praha.cz', 'www.praha.cz', 50),
                                                                                                                                                                                                                                            (2, 'Národní výstava koček Brno', 'Výstava s kapacitou 200 koček', 'OPEN', 'Hala 2', 'Ulice 2', 'Brno', 'CZ', '60000', '2026-05-10 08:00:00', '2026-05-11 18:00:00', '2026-04-30 23:59:59', 'ZO Brno', 'info@brno.cz', 'www.brno.cz', 200),
                                                                                                                                                                                                                                            (3, 'Mezinárodní výstava koček Ostrava', 'Výstava s kapacitou 400 koček', 'OPEN', 'Hala 3', 'Ulice 3', 'Ostrava', 'CZ', '70000', '2026-06-10 08:00:00', '2026-06-11 18:00:00', '2026-05-30 23:59:59', 'ZO Ostrava', 'info@ostrava.cz', 'www.ostrava.cz', 400);

INSERT INTO show_judges (show_id, judge_id) VALUES
                                                (1, 1), (1, 2),
                                                (2, 1), (2, 2), (2, 3), (2, 4), (2, 5),
                                                (3, 1), (3, 2), (3, 3), (3, 4), (3, 5), (3, 6), (3, 7), (3, 8), (3, 9), (3, 10);

INSERT INTO owners (id, first_name, last_name, email, city, address, zip, phone, owner_membership_number, owner_local_organization)
SELECT i, 'Majitel' || i, 'Příjmení' || i, 'majitel' || i || '@email.cz', 'Město ' || (i%5), 'Ulice ' || i, '10000', '+420700000' || LPAD(i::text, 3, '0'), 'CZ-' || i, 'ZO ' || (i%3)
FROM generate_series(1, 50) as i;

INSERT INTO breeders (id, first_name, last_name, city)
SELECT i, 'Chovatel' || i, 'Rodina' || i, 'Město ' || (i%5)
FROM generate_series(1, 50) as i;

INSERT INTO cats (id, user_id, cat_name, ems_code, birth_date, pedigree_number, chip_number, breed, gender, category, father_name, mother_name)
SELECT
    i,
    (i % 8) + 3,
    'Kočka ' || i,
    (ARRAY[
         'EXO n 03 22', 'PER w 62', 'RAG a 04 21', 'SBI n', 'TUV d 61',
     'ACL ns 09 23', 'ACS w 63', 'LPL a 31', 'LPS d 03', 'MCO n 22', 'NEM a 21', 'NFO f 09 24', 'SIB e 22', 'TUA w 61',
     'BEN n 24', 'BLH a 03', 'BML n 11 31', 'BSH a', 'BUR n', 'CHA', 'CYM a 51', 'EUR a 22', 'KBL d 22', 'KBS n 03 24', 'KOR', 'MAN w 62 52', 'MAU ns 24', 'OCI b 24', 'SIN', 'SNO a 05 21', 'SOK', 'SRL fs 03', 'SRS ns 22',
     'ABY n', 'BAL w 67', 'CRX a 33', 'DRX ns 09 24', 'DSP n 03', 'GRX w 61', 'JBS n 01 21', 'OLH b 24', 'OSH w 61', 'PEB a 32', 'RUS', 'SIA n', 'SOM n', 'SPH n 03', 'THA a'
         ])[ (i % 48) + 1 ],
    '2022-01-01'::date + (i % 1000) * interval '1 day',
    'CSCH LO ' || i || '/22',
    '900215000' || LPAD(i::text, 6, '0'),
    (ARRAY[
        'EXO', 'PER', 'RAG', 'SBI', 'TUV',
        'ACL', 'ACS', 'LPL', 'LPS', 'MCO', 'NEM', 'NFO', 'SIB', 'TUA',
        'BEN', 'BLH', 'BML', 'BSH', 'BUR', 'CHA', 'CYM', 'EUR', 'KBL', 'KBS', 'KOR', 'MAN', 'MAU', 'OCI', 'SIN', 'SNO', 'SOK', 'SRL', 'SRS',
        'ABY', 'BAL', 'CRX', 'DRX', 'DSP', 'GRX', 'JBS', 'OLH', 'OSH', 'PEB', 'RUS', 'SIA', 'SOM', 'SPH', 'THA'
    ])[ (i % 48) + 1 ],
    CASE WHEN i % 2 = 0 THEN 'MALE' ELSE 'FEMALE' END,
    (i % 4) + 1,
    'Otec ' || i,
    'Matka ' || i
FROM generate_series(1, 650) as i;

INSERT INTO registrations (id, registration_number, created_at, status, show_id, user_id, owner_id, breeder_id, days, data_accuracy, gdpr_consent, amount_paid, paid_at)
SELECT
    i,
    'REG-' || LPAD(i::text, 6, '0'),
    '2026-01-01 10:00:00'::timestamp,
    'CONFIRMED',
    CASE WHEN i <= 50 THEN 1 WHEN i <= 250 THEN 2 ELSE 3 END,
    (i % 8) + 3,
    ((i - 1) % 50) + 1,
    ((i - 1) % 50) + 1,
    'BOTH',
    true,
    true,
    800,
    '2026-01-02 10:00:00'::timestamp
FROM generate_series(1, 650) as i;

INSERT INTO registration_entries (id, registration_id, cat_id, show_class, cage_type, neutered, catalog_number)
SELECT
    i,
    i,
    i,
    (ARRAY[
         'SUPREME_CHAMPION', 'SUPREME_PREMIOR', 'GRANT_INTER_CHAMPION', 'GRANT_INTER_PREMIER',
     'INTERNATIONAL_CHAMPION', 'INTERNATIONAL_PREMIER', 'CHAMPION', 'PREMIER',
     'OPEN', 'NEUTER', 'JUNIOR', 'KITTEN', 'NOVICE_CLASS', 'CONTROL_CLASS',
     'DETERMINATION_CLASS', 'DOMESTIC_CAT', 'OUT_OF_COMPETITION', 'LITTER', 'VETERAN'
         ])[ (i % 19) + 1 ],
    (ARRAY['OWN_CAGE', 'RENT_SMALL', 'RENT_LARGE'])[ (i % 3) + 1 ],
    CASE WHEN i % 2 = 0 THEN true ELSE false END,
    i
FROM generate_series(1, 650) as i;