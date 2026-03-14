-- ==========================================
-- 1. UŽIVATELÉ (_user)
-- ==========================================
INSERT INTO _user (id, first_name, last_name, email, password, require_password_change, role) VALUES
                                                                                                  (1, 'Hlavní', 'Administrátor', 'admin@admin.cz', '$2a$12$ffwUH/9CVS2buF7sW4zkxenrsdTlUhmikPl4EcohZ2Apu.tPwO85C', false, 'ADMIN'),
                                                                                                  (2, 'Výstavní', 'Sekretariát', 'sekretariat@catshow.cz', '$2a$12$ffwUH/9CVS2buF7sW4zkxenrsdTlUhmikPl4EcohZ2Apu.tPwO85C', false, 'SECRETARIAT'),
                                                                                                  (3, 'Jana', 'Nováková', 'jana.novakova@email.cz', '$2a$10$8.UnVuG9HLROJOsIpiNdqO6.E9.TjT9v5xQe7/K7c0E9TjT9v5xQe7/K', false, 'USER'),
                                                                                                  (4, 'Pavel', 'Dvořák', 'pavel.dvorak@email.cz', '$2a$10$8.UnVuG9HLROJOsIpiNdqO6.E9.TjT9v5xQe7/K7c0E9TjT9v5xQe7/K', false, 'USER'),
                                                                                                  (5, 'Eliška', 'Svobodová', 'eliska.svobodova@email.cz', '$2a$10$8.UnVuG9HLROJOsIpiNdqO6.E9.TjT9v5xQe7/K7c0E9TjT9v5xQe7/K', false, 'USER'),
                                                                                                  (6, 'Tomáš', 'Kučera', 'tomas.kucera@email.cz', '$2a$10$8.UnVuG9HLROJOsIpiNdqO6.E9.TjT9v5xQe7/K7c0E9TjT9v5xQe7/K', false, 'USER'),
                                                                                                  (7, 'Martin', 'Zelený', 'martin.zeleny@email.cz', '$2a$10$8.UnVuG9HLROJOsIpiNdqO6.E9.TjT9v5xQe7/K7c0E9TjT9v5xQe7/K', false, 'USER'),
                                                                                                  (8, 'Lucie', 'Černá', 'lucie.cerna@email.cz', '$2a$10$8.UnVuG9HLROJOsIpiNdqO6.E9.TjT9v5xQe7/K7c0E9TjT9v5xQe7/K', false, 'USER'),
                                                                                                  (9, 'Petra', 'Veselá', 'petra.vesela@email.cz', '$2a$10$8.UnVuG9HLROJOsIpiNdqO6.E9.TjT9v5xQe7/K7c0E9TjT9v5xQe7/K', false, 'USER'),
                                                                                                  (10, 'Karel', 'Novotný', 'karel.novotny@email.cz', '$2a$10$8.UnVuG9HLROJOsIpiNdqO6.E9.TjT9v5xQe7/K7c0E9TjT9v5xQe7/K', false, 'USER'),
                                                                                                  (11, 'Ladislav', 'Karšay', 'ladislav@pawdium.cz', '$2a$12$ffwUH/9CVS2buF7sW4zkxenrsdTlUhmikPl4EcohZ2Apu.tPwO85C', false, 'ADMIN');


-- ==========================================
-- 2. POSUZOVATELÉ (judges)
-- ==========================================
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
                                                                   (10, 'Karl', 'Preiss', 'karl@fifeshow.at', 'AT'),
                                                                   (11, 'Lena', 'Venclíková', 'lena@fifeshow.cz', 'CZ'),
                                                                   (12, 'Fabio', 'Brambilla', 'fabio@fifeshow.cz', 'IT'),
                                                                   (13, 'Martin', 'Urban', 'martin2@fifeshow.cz', 'CZ'),
                                                                   (14, 'Christiano', 'F. Sandon', 'christiano@fifeshow.cz', 'IT');

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
                                                          (10, '1'), (10, '2'), (10, '3'), (10, '4'),
                                                          (11, '1'), (11, '2'), (11, '3'), (11, '4'),
                                                          (12, '1'), (12, '2'), (12, '3'), (12, '4'),
                                                          (13, '2'), (13, '3'), (13, '4'),
                                                          (14, '1'), (14, '2'), (14, '4');

-- ==========================================
-- 3. VÝSTAVY (shows)
-- ==========================================
INSERT INTO shows (id, name, description, status, venue_name, venue_address, venue_city, venue_state, venue_zip, start_date, end_date, registration_deadline, organizer_name, organizer_contact_email, organizer_website_url, max_cats) VALUES
                                                                                                                                                                                                                                            (1, 'Jarní výstava koček Praha', 'Výstava s kapacitou 50 koček', 'OPEN', 'Hala 1', 'Ulice 1', 'Praha', 'CZ', '10000', '2026-04-10 08:00:00', '2026-04-11 18:00:00', '2026-03-30 23:59:59', 'ZO Praha', 'info@praha.cz', 'https://www.praha.cz', 150),
                                                                                                                                                                                                                                            (2, 'Národní výstava koček Brno', 'Výstava s kapacitou 200 koček', 'OPEN', 'Hala 2', 'Ulice 2', 'Brno', 'CZ', '60000', '2026-05-10 08:00:00', '2026-05-11 18:00:00', '2026-04-30 23:59:59', 'ZO Brno', 'info@brno.cz', 'https://www.brno.cz', 200),
                                                                                                                                                                                                                                            (3, 'Mezinárodní výstava koček Ostrava', 'Výstava s kapacitou 400 koček', 'OPEN', 'Hala 3', 'Ulice 3', 'Ostrava', 'CZ', '70000', '2026-06-10 08:00:00', '2026-06-11 18:00:00', '2026-05-30 23:59:59', 'ZO Ostrava', 'info@ostrava.cz', 'https://www.ostrava.cz', 400),
                                                                                                                                                                                                                                            (4, 'NVK Ostrava', 'Speciální výstava vytvořená pro testování nasimulované výstavy s reálnými daty 60 koček.', 'OPEN', 'Brick House Ostrava', 'Vítkovická 3335/15', 'Ostrava', 'CZ', '70200', '2026-04-04 08:00:00', '2026-04-05 18:00:00', '2026-03-30 23:59:59', 'ZO Ostrava', 'info@ostrava.cz', 'https://www.ostrava.cz', 100);

INSERT INTO show_judges (show_id, judge_id) VALUES
                                                (1, 1), (1, 2),
                                                (2, 1), (2, 2), (2, 3), (2, 4), (2, 5),
                                                (3, 1), (3, 2), (3, 3), (3, 4), (3, 5), (3, 6), (3, 7), (3, 8), (3, 9), (3, 10),
                                                (4, 11), (4, 12), (4, 13), (4, 14);

-- =========================================================================================
-- ČÁST A: GENEROVANÁ DATA PRO VÝSTAVU 1 (Kočky 1-100)
-- =========================================================================================
INSERT INTO owners (id, first_name, last_name, email, city, address, zip, phone, owner_membership_number, owner_local_organization)
SELECT i, 'Majitel' || i, 'Příjmení' || i, 'majitel' || i || '@email.cz', 'Město ' || (i%5), 'Ulice ' || i, '10000', '+420700000' || LPAD(i::text, 3, '0'), 'CZ-' || i, 'ZO ' || (i%3)
FROM generate_series(1, 100) as i;

INSERT INTO breeders (id, first_name, last_name, city)
SELECT i, 'Chovatel' || i, 'Rodina' || i, 'Město ' || (i%5)
FROM generate_series(1, 100) as i;

INSERT INTO cats (id, user_id, cat_name, ems_code, birth_date, pedigree_number, chip_number, breed, gender, category, father_name, mother_name)
SELECT
    i, (i % 8) + 3, 'Kočka ' || i,
    (ARRAY['EXO n 03 22', 'PER w 62', 'RAG a 04 21', 'MCO n 22', 'NEM a 21', 'BSH a', 'BUR n'])[ (i % 7) + 1 ],
    '2022-01-01'::date + (i % 1000) * interval '1 day',
    'CSCH LO ' || i || '/22', '900215000' || LPAD(i::text, 6, '0'),
    (ARRAY['EXO', 'PER', 'RAG', 'MCO', 'NEM', 'BSH', 'BUR'])[ (i % 7) + 1 ],
    CASE WHEN i % 2 = 0 THEN 'MALE' ELSE 'FEMALE' END, (i % 4) + 1, 'Otec ' || i, 'Matka ' || i
FROM generate_series(1, 100) as i;

INSERT INTO registrations (id, registration_number, created_at, status, show_id, user_id, owner_id, breeder_id, days, data_accuracy, gdpr_consent, amount_paid, paid_at)
SELECT
    i, 'REG-' || LPAD(i::text, 6, '0'), '2026-01-01 10:00:00'::timestamp, 'CONFIRMED', 1, (i % 8) + 3, i, i, 'BOTH', true, true, 800, '2026-01-02 10:00:00'::timestamp
FROM generate_series(1, 100) as i;

INSERT INTO registration_entries (id, registration_id, cat_id, show_class, cage_type, neutered, catalog_number)
SELECT
    i, i, i, (ARRAY['OPEN', 'NEUTER', 'JUNIOR', 'KITTEN'])[ (i % 4) + 1 ], 'OWN_CAGE', CASE WHEN i % 2 = 0 THEN true ELSE false END, i
FROM generate_series(1, 100) as i;


-- =========================================================================================
-- ČÁST B: 60 REÁLNÝCH KOČEK PRO VÝSTAVU 4 (ID 1001 - 1060)
-- =========================================================================================

-- 1. MAJITELÉ (1001-1060)
INSERT INTO owners (id, first_name, last_name, email, city, address, zip, phone, owner_membership_number, owner_local_organization) VALUES
                                                                                                                                        (1001, 'Ilona', 'Glasová', 'ilona.glasova@catshow.cz', 'CZ', 'Neuvedeno', '00000', '+420', '29578', 'The Czech Aristocats Team'),
                                                                                                                                        (1002, 'Milan', 'Matějka', 'milan.matejka1@catshow.cz', 'CZ', 'Neuvedeno', '00000', '+420', '29273', 'The Czech Aristocats Team'),
                                                                                                                                        (1003, 'Milan', 'Matějka', 'milan.matejka2@catshow.cz', 'CZ', 'Neuvedeno', '00000', '+420', '23601', 'The Czech Aristocats Team'),
                                                                                                                                        (1004, 'Kateřina', 'Stonjeková', 'katerina.stonjekova@catshow.cz', 'CZ', 'Neuvedeno', '00000', '+420', '29577', 'The Czech Aristocats Team'),
                                                                                                                                        (1005, 'Pavel', 'Zrubek', 'pavel.zrubek1@catshow.cz', 'CZ', 'Neuvedeno', '00000', '+420', '26281', 'The Czech Aristocats Team'),
                                                                                                                                        (1006, 'Dominik', 'Klimša', 'dominik.klimsa1@catshow.cz', 'CZ', 'Neuvedeno', '00000', '+420', '29733', 'The Czech Aristocats Team'),
                                                                                                                                        (1007, 'Pavel', 'Zrubek', 'pavel.zrubek2@catshow.cz', 'CZ', 'Neuvedeno', '00000', '+420', '26281', 'The Czech Aristocats Team'),
                                                                                                                                        (1008, 'Pavel', 'Zrubek', 'pavel.zrubek3@catshow.cz', 'CZ', 'Neuvedeno', '00000', '+420', '26281', 'The Czech Aristocats Team'),
                                                                                                                                        (1009, 'Pavel', 'Zrubek', 'pavel.zrubek4@catshow.cz', 'CZ', 'Neuvedeno', '00000', '+420', '26281', 'The Czech Aristocats Team'),
                                                                                                                                        (1010, 'Pavel', 'Zrubek', 'pavel.zrubek5@catshow.cz', 'CZ', 'Neuvedeno', '00000', '+420', '26281', 'The Czech Aristocats Team'),
                                                                                                                                        (1011, 'Petr', 'Orieščík', 'petr.oriescik1@catshow.cz', 'CZ', 'Neuvedeno', '00000', '+420', '29251', 'The Czech Aristocats Team'),
                                                                                                                                        (1012, 'Petr', 'Orieščík', 'petr.oriescik2@catshow.cz', 'CZ', 'Neuvedeno', '00000', '+420', '29251', 'The Czech Aristocats Team'),
                                                                                                                                        (1013, 'Vojtěch', 'Fiža', 'vojtech.fiza@catshow.cz', 'CZ', 'Neuvedeno', '00000', '+420', '29520', 'The Czech Aristocats Team'),
                                                                                                                                        (1014, 'Vendula', 'Motlochová', 'vendula.motlochova@catshow.cz', 'CZ', 'Neuvedeno', '00000', '+420', '29519', 'The Czech Aristocats Team'),
                                                                                                                                        (1015, 'Ivana', 'Konečná', 'ivana.konecna1@catshow.cz', 'CZ', 'Neuvedeno', '00000', '+420', '20117', 'The Czech Aristocats Team'),
                                                                                                                                        (1016, 'Ivana', 'Konečná', 'ivana.konecna2@catshow.cz', 'CZ', 'Neuvedeno', '00000', '+420', '20117', 'The Czech Aristocats Team'),
                                                                                                                                        (1017, 'Ivana', 'Konečná', 'ivana.konecna3@catshow.cz', 'CZ', 'Neuvedeno', '00000', '+420', '20117', 'The Czech Aristocats Team'),
                                                                                                                                        (1018, 'Ivana', 'Konečná', 'ivana.konecna4@catshow.cz', 'CZ', 'Neuvedeno', '00000', '+420', '20117', 'The Czech Aristocats Team'),
                                                                                                                                        (1019, 'Matěj', 'Mika', 'matej.mika1@catshow.cz', 'CZ', 'Neuvedeno', '00000', '+420', '28275', 'The Czech Aristocats Team'),
                                                                                                                                        (1020, 'Matěj', 'Mika', 'matej.mika2@catshow.cz', 'CZ', 'Neuvedeno', '00000', '+420', '28275', 'The Czech Aristocats Team'),
                                                                                                                                        (1021, 'Matěj', 'Mika', 'matej.mika3@catshow.cz', 'CZ', 'Neuvedeno', '00000', '+420', '28275', 'The Czech Aristocats Team'),
                                                                                                                                        (1022, 'Pavlína', 'Kořínková', 'pavlina.korinkova@catshow.cz', 'CZ', 'Neuvedeno', '00000', '+420', '25827', 'ZO 36 Brno'),
                                                                                                                                        (1023, 'Denisa', 'Dubčáková', 'denisa.dubcakova@catshow.cz', 'CZ', 'Neuvedeno', '00000', '+420', '25412', 'SO Ostrava'),
                                                                                                                                        (1024, 'Petra', 'Kociánová', 'petra.kocianova@catshow.cz', 'CZ', 'Neuvedeno', '00000', '+420', '29884', 'SO Ostrava'),
                                                                                                                                        (1025, 'Dominik', 'Klimša', 'dominik.klimsa2@catshow.cz', 'CZ', 'Neuvedeno', '00000', '+420', '29733', 'The Czech Aristocats Team'),
                                                                                                                                        (1026, 'Renata', 'Drajsajtlová', 'renata.drajsajtlova1@catshow.cz', 'CZ', 'Neuvedeno', '00000', '+420', '30112', 'The Czech Aristocats Team'),
                                                                                                                                        (1027, 'Renata', 'Drajsajtlová', 'renata.drajsajtlova2@catshow.cz', 'CZ', 'Neuvedeno', '00000', '+420', '30112', 'The Czech Aristocats Team'),
                                                                                                                                        (1028, 'Kristina', 'Polová', 'kristina.polova@catshow.cz', 'CZ', 'Neuvedeno', '00000', '+420', '36509-2022', 'BCC'),
                                                                                                                                        (1029, 'JANA, PETR', 'KADLECOVÁ, BRAUNEROVI', 'jana.kadlecova1@catshow.cz', 'CZ', 'Neuvedeno', '00000', '+420', '19847', 'Star Cats Praha'),
                                                                                                                                        (1030, 'JANA, PETR', 'KADLECOVÁ, BRAUNEROVI', 'jana.kadlecova2@catshow.cz', 'CZ', 'Neuvedeno', '00000', '+420', '19847', 'Star Cats Praha'),
                                                                                                                                        (1031, 'JANA, PETR', 'KADLECOVÁ, BRAUNEROVI', 'jana.kadlecova3@catshow.cz', 'CZ', 'Neuvedeno', '00000', '+420', '19847', 'Star Cats Praha'),
                                                                                                                                        (1032, 'Lucia', 'Lavička', 'lucia.lavicka1@catshow.cz', 'CZ', 'Neuvedeno', '00000', '+420', '28183', 'Star Cats Praha'),
                                                                                                                                        (1033, 'Lucia', 'Lavička', 'lucia.lavicka2@catshow.cz', 'CZ', 'Neuvedeno', '00000', '+420', '28183', 'Star Cats Praha'),
                                                                                                                                        (1034, 'Pavlína', 'Hanzlíčková', 'pavlina.hanzlickova@catshow.cz', 'CZ', 'Neuvedeno', '00000', '+420', '29542', 'SO Kočky Brno'),
                                                                                                                                        (1035, 'AGNIESZKA', 'KUCIEL', 'agnieszka.kuciel1@catshow.pl', 'PL', 'Neuvedeno', '00000', '+420', '000', 'Cat Club Feniks'),
                                                                                                                                        (1036, 'AGNIESZKA', 'KUCIEL', 'agnieszka.kuciel2@catshow.pl', 'PL', 'Neuvedeno', '00000', '+420', '000', 'Cat Club Feniks'),
                                                                                                                                        (1037, 'Georgina', 'Alban', 'georgina.alban1@catshow.ro', 'RO', 'Neuvedeno', '00000', '+420', '000', 'Sofisticat'),
                                                                                                                                        (1038, 'Georgina', 'Alban', 'georgina.alban2@catshow.ro', 'RO', 'Neuvedeno', '00000', '+420', '000', 'Sofisticat'),
                                                                                                                                        (1039, 'Georgina', 'Alban', 'georgina.alban3@catshow.ro', 'RO', 'Neuvedeno', '00000', '+420', '000', 'Sofisticat'),
                                                                                                                                        (1040, 'Georgina', 'Alban', 'georgina.alban4@catshow.ro', 'RO', 'Neuvedeno', '00000', '+420', '000', 'Sofisticat'),
                                                                                                                                        (1041, 'Georgina', 'Alban', 'georgina.alban5@catshow.ro', 'RO', 'Neuvedeno', '00000', '+420', '000', 'Sofisticat'),
                                                                                                                                        (1042, 'Georgina', 'Alban', 'georgina.alban6@catshow.ro', 'RO', 'Neuvedeno', '00000', '+420', '000', 'Sofisticat'),
                                                                                                                                        (1043, 'Dorota', 'Biskupska', 'dorota.biskupska1@catshow.pl', 'PL', 'Neuvedeno', '00000', '+420', '000', 'Cat Club Wroctaw'),
                                                                                                                                        (1044, 'Dorota', 'Biskupska', 'dorota.biskupska2@catshow.pl', 'PL', 'Neuvedeno', '00000', '+420', '000', 'Cat Club Wroctaw'),
                                                                                                                                        (1045, 'Dorota', 'Biskupska', 'dorota.biskupska3@catshow.pl', 'PL', 'Neuvedeno', '00000', '+420', '000', 'Cat Club Wroctaw'),
                                                                                                                                        (1046, 'Dorota', 'Biskupska', 'dorota.biskupska4@catshow.pl', 'PL', 'Neuvedeno', '00000', '+420', '000', 'Cat Club Wroctaw'),
                                                                                                                                        (1047, 'Dorota', 'Biskupska', 'dorota.biskupska5@catshow.pl', 'PL', 'Neuvedeno', '00000', '+420', '000', 'Cat Club Wroctaw'),
                                                                                                                                        (1048, 'IRENA', 'Galbavá', 'irena.galbava1@catshow.cz', 'CZ', 'Neuvedeno', '00000', '+420', '28368', 'SO Kolín'),
                                                                                                                                        (1049, 'IRENA', 'Galbavá', 'irena.galbava2@catshow.cz', 'CZ', 'Neuvedeno', '00000', '+420', '28368', 'SO Kolín'),
                                                                                                                                        (1050, 'Radka', 'Hoňková', 'radka.honkova@catshow.cz', 'CZ', 'Neuvedeno', '00000', '+420', '28812', '1. Slezská Kočičí'),
                                                                                                                                        (1051, 'Damian', 'Wasielewski', 'damian.wasielewski1@catshow.pl', 'PL', 'Neuvedeno', '00000', '+420', '16792', 'Amofeles FPL'),
                                                                                                                                        (1052, 'Damian', 'Wasielewski', 'damian.wasielewski2@catshow.pl', 'PL', 'Neuvedeno', '00000', '+420', '16792', 'Amofeles FPL'),
                                                                                                                                        (1053, 'Damian', 'Wasielewski', 'damian.wasielewski3@catshow.pl', 'PL', 'Neuvedeno', '00000', '+420', '16792', 'Amofeles FPL'),
                                                                                                                                        (1054, 'Jana', 'Rajter', 'jana.rajter@catshow.cz', 'CZ', 'Neuvedeno', '00000', '+420', '25884', '1. Slezská Kočičí'),
                                                                                                                                        (1055, 'Monika', 'Starzec', 'monika.starzec1@catshow.pl', 'PL', 'Neuvedeno', '00000', '+420', '000', 'Cat Club Rybnik'),
                                                                                                                                        (1056, 'Monika', 'Starzec', 'monika.starzec2@catshow.pl', 'PL', 'Neuvedeno', '00000', '+420', '000', 'Cat Club Rybnik'),
                                                                                                                                        (1057, 'Martina', 'Navrátilová', 'martina.navratilova1@catshow.cz', 'CZ', 'Neuvedeno', '00000', '+420', '997903', 'Star Cats Praha'),
                                                                                                                                        (1058, 'Martina', 'Navrátilová', 'martina.navratilova2@catshow.cz', 'CZ', 'Neuvedeno', '00000', '+420', '997903', 'Star Cats Praha'),
                                                                                                                                        (1059, 'Violetta', 'Torebko', 'violetta.torebko1@catshow.pl', 'PL', 'Neuvedeno', '00000', '+420', '14808', 'Amofeles FPL'),
                                                                                                                                        (1060, 'Violetta', 'Torebko', 'violetta.torebko2@catshow.pl', 'PL', 'Neuvedeno', '00000', '+420', '14808', 'Amofeles FPL');


-- 2. CHOVATELÉ (1001-1060)
INSERT INTO breeders (id, first_name, last_name, city) VALUES
                                                           (1001, 'Maciej', 'Cabaj', 'PL'),
                                                           (1002, 'Ladislav', 'Karšay', 'CZ'),
                                                           (1003, 'Ladislav', 'Karšay', 'CZ'),
                                                           (1004, 'Ladislav', 'Karšay', 'CZ'),
                                                           (1005, 'Pavel', 'Zrubek', 'CZ'),
                                                           (1006, 'Pavel', 'Zrubek', 'CZ'),
                                                           (1007, 'Pavel', 'Zrubek', 'CZ'),
                                                           (1008, 'Pavel', 'Zrubek', 'CZ'),
                                                           (1009, 'Irina', 'Kovalenko', 'UA'),
                                                           (1010, 'Zlat', 'Farkas', 'CZ'),
                                                           (1011, 'Miriam', 'Bednárová', 'SK'),
                                                           (1012, 'Soňa', 'Demešová', 'CZ'),
                                                           (1013, 'MVDr.Regina', 'Weiszová', 'CZ'),
                                                           (1014, 'MVDr.Regina', 'Weiszová', 'CZ'),
                                                           (1015, 'Giovanna', 'Calabrese', 'FR'),
                                                           (1016, 'Matěj', 'Mika', 'CZ'),
                                                           (1017, 'Matěj', 'Mika', 'CZ'),
                                                           (1018, 'Ivana', 'Konečná', 'CZ'),
                                                           (1019, 'Aneta', 'Dzižová', 'CZ'),
                                                           (1020, 'Matěj', 'Mika', 'CZ'),
                                                           (1021, 'Matěj', 'Mika', 'CZ'),
                                                           (1022, 'Aneta', 'Wozińska', 'PL'),
                                                           (1023, 'Pavel', 'Sedláček', 'CZ'),
                                                           (1024, 'Petra', 'Kociánová', 'CZ'),
                                                           (1025, 'Irina', 'Marschalova', 'UA'),
                                                           (1026, 'Renata', 'Drajsajtlová', 'CZ'),
                                                           (1027, 'Ivana', 'Konečná', 'CZ'),
                                                           (1028, 'Edita', 'Končová', 'CZ'),
                                                           (1029, 'Christelle', 'Vercoutere', 'FR'),
                                                           (1030, 'Saranpat', 'Shincham', 'TH'),
                                                           (1031, 'Neuvedeno', 'Neuvedeno', 'XX'),
                                                           (1032, 'Lucia', 'Lavička', 'CZ'),
                                                           (1033, 'Lucia', 'Lavička', 'CZ'),
                                                           (1034, 'Pavlína', 'Hanzlíčková', 'CZ'),
                                                           (1035, 'AGNIESZKA', 'KUCIEL', 'PL'),
                                                           (1036, 'MAKKA', 'BOBIN', 'FR'),
                                                           (1037, 'Georgina', 'Alban', 'RO'),
                                                           (1038, 'Georgina', 'Alban', 'RO'),
                                                           (1039, 'Georgina', 'Alban', 'RO'),
                                                           (1040, 'Georgina', 'Alban', 'RO'),
                                                           (1041, 'Georgina', 'Alban', 'RO'),
                                                           (1042, 'Georgina', 'Alban', 'RO'),
                                                           (1043, 'Dorota', 'Biskupska', 'PL'),
                                                           (1044, 'Dorota', 'Biskupska', 'PL'),
                                                           (1045, 'Dorota', 'Biskupska', 'PL'),
                                                           (1046, 'Dorota', 'Biskupska', 'PL'),
                                                           (1047, 'Dorota', 'Biskupska', 'PL'),
                                                           (1048, 'Dana', 'Michelova', 'CZ'),
                                                           (1049, 'Natalia', 'Lipovskaya', 'UA'),
                                                           (1050, 'Jana', 'Rajter', 'CZ'),
                                                           (1051, 'Damian', 'Wasielewski', 'PL'),
                                                           (1052, 'Damian', 'Wasielewski', 'PL'),
                                                           (1053, 'Damian', 'Wasielewski', 'PL'),
                                                           (1054, 'Jana', 'Rajter', 'CZ'),
                                                           (1055, 'Monika', 'Starzec', 'PL'),
                                                           (1056, 'Monika', 'Starzec', 'PL'),
                                                           (1057, 'Martina', 'Navrátilová', 'CZ'),
                                                           (1058, 'Alena', 'Opatrná', 'CZ'),
                                                           (1059, 'Wioletta', 'Torebko', 'PL'),
                                                           (1060, 'Wioletta', 'Torebko', 'PL');


-- 3. KOČKY (1001-1060)
INSERT INTO cats (id, user_id, title_before, cat_name, title_after, ems_code, birth_date, pedigree_number, cat_group, chip_number, breed, gender, category, father_name, mother_name) VALUES
                                                                                                                                                    (1001, 3, 'GIC', 'Marquez Olicats * PL', '', 'RAG n 03', '2021-03-22', '(CZ)ČSCH LO 409/21/RAG', '','616093900978057', 'RAG', 'MALE', 1, 'CH Daster von Werbellinsee', 'Tila Tequila Global House, PL'),
                                                                                                                                                    (1002, 4, 'JCH, KCH', 'Guinevere DB Crystal Jewels *CZ', 'JW', 'RAG n 03', '2024-08-03', '(CZ) ČSCH LO 835/24/RAG', '','900182002361516', 'RAG', 'FEMALE', 1, 'SC Dakar Nekonomicon *PL, DVM', 'CEW`24,CH,JCH Blue Crystal Jewels *CZ. JW'),
                                                                                                                                                    (1003, 5, '', 'Koko JG Crystal Jewels * CZ', '', 'RAG a 03', '2025-08-20', '(CZ) ČSCH LO 1072/25/RAG', '','993021200020245', 'RAG', 'FEMALE', 1, 'CH, JCH,KCH SE*Lil`Magics Hit the Road Jack, JW', 'JCH, KCH Guinevere DB Crystal Jewels * CZ, JW'),
                                                                                                                                                    (1004, 6, '', 'Kokopelli JG Crystal Jewels * CZ','',  'RAG a 03', '2025-08-20', '(CZ) ČSCH LO 1071/25/RAG', '','993021200020248', 'RAG', 'MALE', 1, 'CH, JCH, KCH SE*Lil`Magics Hit the Road Jack, JW', 'JCH, KCH Guinevere DB Crystal Jewels * CZ, JW'),
                                                                                                                                                    (1005, 7, '', 'Hakuna Matata of Themyscira * CZ','',  'MCO n 22', '2025-09-04', '(CZ)ČSCH LO 1750/25/MCO', '3','901001000462569', 'MCO', 'FEMALE', 2, 'Dorian Ilower*PL', 'Diana Darling of Sand Mines*CZ'),
                                                                                                                                                    (1006, 8, '', 'Heimdall of Themyscira*CZ', '', 'MCO a 23', '2025-09-04', '(CZ)ČSCH LO 1748/25/MCO', '3','901001000465553', 'MCO', 'MALE', 2, 'Dorian Ilower*PL', 'Diana Darling of Sand Mines*CZ'),
                                                                                                                                                    (1007, 9, '', 'Hellcat of Themyscira * CZ','',  'MCO n 22', '2025-09-04', '(CZ) ČSCH LO 1752/25/MCO', '3','901001000462571', 'MCO', 'FEMALE', 2, 'Dorian Ilower*PL', 'Diana Darling of Sand Mines*CZ'),
                                                                                                                                                    (1008, 10, '', 'Hakuna Matata 2 of Themyscira * CZ', '', 'MCO n 22', '2025-09-04', '(CZ) ČSCH LO 1750/25/MCO-8', '3','901001000462568', 'MCO', 'FEMALE', 2, 'Dorian Ilower*PL', 'Diana Darling of Sand Mines*CZ'),
                                                                                                                                                    (1009, 3, '', 'Arine Wisla*UA', '', 'MCO n 25', '2025-08-21', 'PENDING-1009', '3','900255003226590', 'MCO', 'FEMALE', 2, 'UA*Arine Valentino', 'UA*Arine Fortezza'),
                                                                                                                                                    (1010, 4, '', 'Chilli Coon Zlata Star*CZ', '', 'MCO n 25', '2025-10-23', '(CZ) ČSCH LO 8/26/MCO', '3','981020004701672', 'MCO', 'MALE', 2, 'Xell Bonito Lucky life *CZ', 'Esmeralda Zlata Star-CZ'),
                                                                                                                                                    (1011, 5, '', 'Corraline Golden Sands', '', 'MCO n 09 22', '2025-04-25', '(SK)FFS LO 00835-11', '4','941000029980826', 'MCO', 'FEMALE', 2, 'IC Ennio Moricone by Imagine Glamour*CZ', 'IC Fruzia Koci Zaulek*PL'),
                                                                                                                                                    (1012, 6, 'JCH, KCH', 'Living Legend of Desomena', '', 'MCO d 09', '2024-11-21', '(HU) FH LO 18675', '6','900085001719463', 'MCO', 'MALE', 2, 'Moonrisecoon Damian', 'Pawsome Giants Roxen'),
                                                                                                                                                    (1013, 7, '', 'Luisa Nonstop Naked, CZ', '', 'SPH n', '2024-09-03', '(CZ)ČSCH LO 133/24/SPH', '','953010007450362', 'SPH', 'FEMALE', 4, 'Maverick of Ankh Amulet', 'GIC Hope Trojan Hill'),
                                                                                                                                                    (1014, 8, '', 'Leia Nonstop Naked, CZ', '', 'SPH n 09', '2024-09-03', '(CZ)ČSCH LO 131/24/SPH', '','953010007676238', 'SPH', 'FEMALE', 4, 'Maverick of Ankh Amulet', 'GIC Hope Trojan Hill'),
                                                                                                                                                    (1015, 9, 'KCH', 'Alice De La Cat''Suki', '', 'BSH c 02 62', '2025-05-06', '(CZ)ČSCH LO 847/25/BSH', '','250269611411404', 'BSH', 'FEMALE', 3, 'Uggi of Black Amazon, CZ', 'Juliana of Black Amazon, CZ'),
                                                                                                                                                    (1016, 10, 'KCH', 'Avalon Furry Dynastia, CZ', '', 'BSH p 03', '2025-04-29', '(CZ)ČSCH LO 591/25/BSH', '','900085001853676', 'BSH', 'MALE', 3, 'Alexxmax Cocotello*PL', 'JCH KCH CH Hawa of Black Amazon, CZ'),
                                                                                                                                                    (1017, 3, 'KCH', 'Black Pearl Furry Dynastia, CZ', '', 'BSH n', '2025-05-03', '(CZ) ČSCH LO 598/25/BSH', '','900085001751387', 'BSH', 'FEMALE', 3, 'KCH JCH IC Cheddar of Black Amazon, CZ', 'KCH JCH Koenigsee Jocelyn'),
                                                                                                                                                    (1018, 4, '', 'Zenobia of Black Amazon, CZ', '', 'BLH h', '2025-09-06', '(CZ) ČSCH LO 110/25/BLH', '','203164000194090', 'BLH', 'FEMALE', 3, 'Alexxmax Cocotello, PL', 'JCH Cora of Black Amazon, CZ'),
                                                                                                                                                    (1019, 5, '', 'Lexy alias Skřítek', '', 'HCL', '2024-04-24', 'NO-PP-1019', '','900163000164216', 'HCL', 'FEMALE', 4, 'Unknown', 'Unknown'),
                                                                                                                                                    (1020, 6, 'KCH', 'Bambi Furry Dynastia, CZ', '', 'BSH o 03', '2025-05-03', '(CZ)ČSCH LO 596/25/BSH', '','900085001751393', 'BSH', 'MALE', 3, 'JCH KCH IC Cheddar of Black Amazon, CZ', 'JCH KCH Koenigsee Jocelyn*DE'),
                                                                                                                                                    (1021, 7, 'KCH', 'Aloha Furry Dynastia, CZ', '', 'BSH g 03', '2025-04-29', '(CZ)ČSCH LO 595/25/BSH', '','900085001853679', 'BSH', 'FEMALE', 3, 'Alexxmax Cocotello*PL', 'JCH KCH CH Hawa of Black Amazon, CZ'),
                                                                                                                                                    (1022, 8, 'IC', 'Daysy Błękitek* PL', '', 'RUS', '2017-03-07', 'ČSCH LO 107/17/RUS', '','900164001513868', 'RUS', 'FEMALE', 4, 'CH Kind of Blue Akamaru', 'Wild Yoko Błękitek, PL'),
                                                                                                                                                    (1023, 9, '', 'Makronka Antilie, CZ', '', 'BSH e', '2025-04-01', '(CZ)ČSCH LO 327/25 BSH', '3','900085001851645', 'BSH', 'FEMALE', 3, 'George Teczowy Agat*PL', 'Jolen Dowgar Hill*PL'),
                                                                                                                                                    (1024, 10, '', 'Cherine Dakkar*SK', '', 'CRX d 03 24', '2025-08-10', '(SK)FFS LO 00946', '','900085002026202', 'CRX', 'FEMALE', 4, 'Ursus Kocia Drzemka*PL', 'CH Ganga Dakkar*SK'),
                                                                                                                                                    (1025, 3, '', 'Hemilia Marelax Pride', '', 'MCO fs 03', '2025-06-14', 'UAS 7683-25/MCO', '','900255000962307', 'MCO', 'FEMALE', 2, 'JACK POT MARELAX PRIDE', 'Quincy Marelax Pride'),
                                                                                                                                                    (1026, 4, '', 'Kolja', '', 'HCS', '2018-08-24', 'NO-PP-1026', '','900267000024205', 'HCS', 'MALE', 4, 'Unknown', 'Unknown'),
                                                                                                                                                    (1027, 5, '', 'Yorick of Black Amazon, CZ', '', 'BSH e 02 62', '2025-05-15', 'LO 690/25/BSH', '','203164000194001', 'BSH', 'MALE', 3, 'IC Sebastiano of Black Amazon, CZ', 'Azalea Black Amazon, CZ'),
                                                                                                                                                    (1028, 6, '', 'Yuuto Yutani Cat''s Wine, CZ', '', 'SIB d 22', '2024-08-14', 'BCC-2025 LO/7420/SIB', '','963002000098751', 'SIB', 'MALE', 2, 'Fabien Lesnoe Tzarstvo - E', 'Hope Top*Matryoshka'),
                                                                                                                                                    (1029, 7, '', 'Altynai Notre Dame de Joye', '', 'THA n', '2025-06-12', '(CZ) ČSCH LO 5/26/THA', '','250268781763944', 'THA', 'FEMALE', 4, 'GR.CH.EUR. THONGCHAI NOTRE DAME DE JOYE', 'PINK PROUD''S ZARA ANGKANA'),
                                                                                                                                                    (1030, 8, '', 'Super Rich''s Chatmanee Si Fah', '', 'THA a', '2022-01-05', '(CZ)ČSCH LO 01/23/THA', '','900219001090155', 'THA', 'FEMALE', 4, 'Super Rich', 'Thong Yib'),
                                                                                                                                                    (1031, 9, '', 'Třešnička', '', 'HCS', '2026-06-27', 'NO-PP-1031', '','203098100594717', 'HCS', 'FEMALE', 4, 'Unknown', 'Unknown'),
                                                                                                                                                    (1032, 10, '', 'Hope Your Grace, CZ', '', 'BML ns 11 31', '2025-11-23', 'ČSCH RX 16/25/BML', '','203098100603862', 'BML', 'FEMALE', 3, 'Radek Momoiro, CZ', 'CH SE Grayscale''s Maluhia'),
                                                                                                                                                    (1033, 3, '', 'Vrh H Your Grace, CZ', '', 'BML', '2025-11-23', 'PENDING-LITTER-33', '','NO-CHIP-1033', 'BML', 'MALE', 3, 'Radek Momoiro, CZ', 'CH SE Grayscale''s Maluhia'),
                                                                                                                                                    (1034, 4, 'JCH', 'Engee Chalomon, CZ', '', 'BEN n 24', '2025-03-01', '(CZ) ČSCH LO 50/25/BEN', '3','203164000183629', 'BEN', 'FEMALE', 3, 'Valerian Felis Empire, CZ', 'Wanessa Klarisa Taylor Wild Cat, CZ'),
                                                                                                                                                    (1035, 5, '', 'ELIZA MRAUCZARY*PL', '', 'MCO a 22', '2024-01-11', '(PL)FPL LO 316284', '','NO-CHIP-1035', 'MCO', 'FEMALE', 2, 'GIC ELVIS ROSOCHATE*PL', 'CH CZAKRA BRATNIA DUSZA*PL'),
                                                                                                                                                    (1036, 6, '', 'MY GLORIOUS UMI', '', 'MCO e', '2023-08-08', '(PL)FPL LO 291218', '','NO-CHIP-1036', 'MCO', 'FEMALE', 2, 'YA-YAKUZA STAR ARK*RU', 'AMBER BLISS KRASNYI DAR*RU'),
                                                                                                                                                    (1037, 7, '', 'Fuji of Gina''s Dream * RO', '', 'MCO ns 25', '2025-04-29', 'ROFNFR LO250365', '','642090001180355', 'MCO', 'MALE', 2, 'Quantum of Solace Arbor Vitae, CZ', 'BSW`24 Zris of Gina`s Dream, JW *RO'),
                                                                                                                                                    (1038, 8, 'KCH, JCH', 'Zorba of Gina''s Dream *RO', '', 'MCO d 09 25', '2025-03-08', 'ROFNFR LO250338', '','642099001180327', 'MCO', 'MALE', 2, 'Tri-D Bloomberg', 'KCH,JCH BSW`24 Zoya of Gina`s Dream, JW'),
                                                                                                                                                    (1039, 9, '', 'Sissy of Gina''s Dream *RO', '', 'MCO as 25', '2025-09-11', 'ROFNFR LO250626', '','642090002335731', 'MCO', 'FEMALE', 2, 'NW, BSW, SC Spencer of Gina`s Dream *RO', 'KCH, JCH Fluffy of Gina`s Dream *RO'),
                                                                                                                                                    (1040, 10, '', 'Ingrid of Gina''s Dream *RO', '', 'MCO fs 22', '2025-05-20', 'ROFNFR LO250370', '','642099001178714', 'MCO', 'FEMALE', 2, 'Xman of Gina`s Dream *RO', 'Yara of Gina`s Dream *RO'),
                                                                                                                                                    (1041, 3, '', 'Precious of Gina''s Dream *RO', '', 'MCO n 25', '2025-08-10', 'ROFNFR LO250610', '3','642090002335720', 'MCO', 'FEMALE', 2, 'Wolf of Gina`s Dream *RO', 'CH, Lady Diana of Gina`s Dream *RO'),
                                                                                                                                                    (1042, 4, '', 'Sasha of Gina''s Dream *RO', '', 'MCO e 09 25', '2025-09-11', 'ROFNFR LO250630', '','642090002335730', 'MCO', 'MALE', 2, 'NW, BSW, SC Spencer of Gina`s Dream *RO', 'KCH, JCH Fluffy of Gina`s Dream *RO'),
                                                                                                                                                    (1043, 5, '', 'Forest Eyes W`Eros *PL', '', 'NFO nt 09 25', '2025-02-22', '(PL)FPL LO 322823', '','900245000023949', 'NFO', 'MALE', 2, 'SC PL* Forest Eyes Zimba DSM DVM', 'IC PL*Forest Eyes Beyla'),
                                                                                                                                                    (1044, 6, 'JCH, KCH', 'Forest Eyes Worix *PL', '', 'NFO ns', '2025-02-22', '(PL)FPL LO 322824', '','900245000023943', 'NFO', 'MALE', 2, 'SC PL* Forest Eyes Zimba DSM DVM', 'IC PL*Forest Eyes Beyla'),
                                                                                                                                                    (1045, 7, 'KCH', 'FOREST EYES X''NAMY *PL', '', 'NFO nt 22', '2025-06-20', '(PL)FPL LO 330756', '','900245000024043', 'NFO', 'FEMALE', 2, 'GIC JCH PL*FOREST EYES OLAF', 'SC PL*FOREST EYES R''NAMERA'),
                                                                                                                                                    (1046, 8, '', 'PL*FOREST EYES Z''ROSE', '', 'NFO n 09 24', '2025-04-08', '(PL)FPL LO 337477', '','900245000028538', 'NFO', 'FEMALE', 2, 'S*PYSIDA''S BLUEGRASS', 'GIC JCH PL*FOREST EYES OLET'),
                                                                                                                                                    (1047, 9, '', 'PL*FOREST EYES ZAZU', '', 'NFO ns 23', '2025-04-08', '(PL)FPL LO 337480', '','900245000023979', 'NFO', 'MALE', 2, 'S*PYSIDA''S BLUEGRASS', 'GIC JCH PL*FOREST EYES OLET'),
                                                                                                                                                    (1048, 10, '', 'DORIS ALESSANDRITE, C Z', '', 'SPH n 21 33', '2025-11-01', '(CZ)ČSCH LO 1/26/DSP', '','953010007698111', 'SPH', 'FEMALE', 4, 'ARAM LEONDRAG, CZ', 'JIMIN SDYRON, SK'),
                                                                                                                                                    (1049, 3, '', 'STAR TESH KOSMO', '', 'SPH d 03 21', '2019-06-13', '(CZ)ČSCH LO 23/23/DSP', '','992001000265617', 'SPH', 'MALE', 4, 'CHE Efrat Fantasy Jeweler of Star Tesh', 'Star Tesh Cassiopeia'),
                                                                                                                                                    (1050, 4, '', 'Desirée Charming Bella, CZ', '', 'MCO f 09', '2024-08-01', '(CZ)ČSCH LO 1823/24/MCO', '','991003002637977', 'MCO', 'FEMALE', 2, 'Abbsy Charming Bella, CZ', 'Rihanna Marvel Potštejn, CZ'),
                                                                                                                                                    (1051, 5, '', 'Xami Polarny Błękit*PL', '', 'RAG f 04 21', '2025-07-06', '(PL)FPL LO 345375', '','616024090109549', 'RAG', 'FEMALE', 1, 'IC JCH KCH PL*Ancymony Tokyo', 'Wink Deer Chen Mei Mei'),
                                                                                                                                                    (1052, 6, '', 'Xantos Polarny Błękit*PL', '', 'RAG n 03 21', '2025-07-06', '(PL)FPL LO 345374', '','616024090109546', 'RAG', 'FEMALE', 1, 'IC JCH KCH PL*Ancymony Tokyo', 'Wink Deer Chen Mei Mei'),
                                                                                                                                                    (1053, 7, '', 'Xanti Polarny Błękit*PL', '', 'RAG f 04', '2025-07-06', '(PL)FPL LO 345376', '','616024090109543', 'RAG', 'FEMALE', 1, 'IC JCH KCH PL*Ancymony Tokyo', 'Wink Deer Chen Mei Mei'),
                                                                                                                                                    (1054, 8, '', 'Fiore Charming Bella, CZ', '', 'MCO fs 23', '2025-05-07', '(CZ)ČSCH LO 1095/25/MCO', '','991003003124407', 'MCO', 'FEMALE', 2, 'JCH, CH Wild Kerry Cat Balfor, IE', 'Bagera Charming Bella, CZ'),
                                                                                                                                                    (1055, 9, '', 'Ismael White Sky *PL', '', 'NEM n', '2025-02-11', '(PL)FPL LO 344533', '','616093902854847', 'NEM', 'MALE', 2, 'CH Eden Touch of Love *CZ', 'Nicoletta Bellaneva *PL'),
                                                                                                                                                    (1056, 10, 'KCH, JCH', 'Fado White Sky *PL', '', 'NEM a 09 21', '2025-04-13', '(PL)FPL LO 324281', '','616093902854864', 'NEM', 'MALE', 2, 'Kuzma Lazorovy Jahont *RU', 'Bessa viva Hestia *PL'),
                                                                                                                                                    (1057, 3, '', 'Gift from heaven BlansCat,CZ', '', 'BML ns 11 31', '2025-07-06', '(CZ)ČSCH LO 17/25/BML', '','203164000189017', 'BML', 'FEMALE', 3, 'Fritiof Baheras', 'CEW,NW, SC Julia Momoiro,CZ, JW DSM'),
                                                                                                                                                    (1058, 4, 'KCH, JCH', 'Simone Momoiro,CZ', 'JW', 'BML ns 11 31', '2025-04-22', 'CZ) ČSCH RX 9/25/BML', '','203164000189001', 'BML', 'FEMALE', 3, 'Fritiof Baheras', 'Marilyn Momoiro,CZ'),
                                                                                                                                                    (1059, 5, 'KCH', 'Del Salandos *PL Vanilia', '', 'RAG a 03', '2025-06-09', '(PL)FPL LO 329804', '','616093902981197', 'RAG', 'FEMALE', 1, 'SC Del Salandos *PL Igor', 'GF Luna Royal Secret *PL'),
                                                                                                                                                    (1060, 6, 'KCH', 'Del Salandos *PL Yoko II', '', 'RAG a 04', '2025-08-29', '(PL)FPL LO 335550', '','616093902630538', 'RAG', 'FEMALE', 1, 'La Rocco Royal Secret *PL', 'Royal Secret *PL ADL Flora');

-- 4. PŘIHLÁŠKY (1001-1060)
INSERT INTO registrations (id, registration_number, created_at, status, show_id, user_id, owner_id, breeder_id, days, data_accuracy, gdpr_consent, amount_paid, paid_at) VALUES
                                                                                                                                                                             (1001, 'REG-REAL-01', '2026-01-01 10:00:00', 'CONFIRMED', 4, 3, 1001, 1001, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1002, 'REG-REAL-02', '2026-01-01 10:00:00', 'CONFIRMED', 4, 4, 1002, 1002, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1003, 'REG-REAL-03', '2026-01-01 10:00:00', 'CONFIRMED', 4, 5, 1003, 1003, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1004, 'REG-REAL-04', '2026-01-01 10:00:00', 'CONFIRMED', 4, 6, 1004, 1004, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1005, 'REG-REAL-05', '2026-01-01 10:00:00', 'CONFIRMED', 4, 7, 1005, 1005, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1006, 'REG-REAL-06', '2026-01-01 10:00:00', 'CONFIRMED', 4, 8, 1006, 1006, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1007, 'REG-REAL-07', '2026-01-01 10:00:00', 'CONFIRMED', 4, 9, 1007, 1007, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1008, 'REG-REAL-08', '2026-01-01 10:00:00', 'CONFIRMED', 4, 10, 1008, 1008, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1009, 'REG-REAL-09', '2026-01-01 10:00:00', 'CONFIRMED', 4, 3, 1009, 1009, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1010, 'REG-REAL-10', '2026-01-01 10:00:00', 'CONFIRMED', 4, 4, 1010, 1010, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1011, 'REG-REAL-11', '2026-01-01 10:00:00', 'CONFIRMED', 4, 5, 1011, 1011, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1012, 'REG-REAL-12', '2026-01-01 10:00:00', 'CONFIRMED', 4, 6, 1012, 1012, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1013, 'REG-REAL-13', '2026-01-01 10:00:00', 'CONFIRMED', 4, 7, 1013, 1013, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1014, 'REG-REAL-14', '2026-01-01 10:00:00', 'CONFIRMED', 4, 8, 1014, 1014, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1015, 'REG-REAL-15', '2026-01-01 10:00:00', 'CONFIRMED', 4, 9, 1015, 1015, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1016, 'REG-REAL-16', '2026-01-01 10:00:00', 'CONFIRMED', 4, 10, 1016, 1016, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1017, 'REG-REAL-17', '2026-01-01 10:00:00', 'CONFIRMED', 4, 3, 1017, 1017, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1018, 'REG-REAL-18', '2026-01-01 10:00:00', 'CONFIRMED', 4, 4, 1018, 1018, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1019, 'REG-REAL-19', '2026-01-01 10:00:00', 'CONFIRMED', 4, 5, 1019, 1019, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1020, 'REG-REAL-20', '2026-01-01 10:00:00', 'CONFIRMED', 4, 6, 1020, 1020, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1021, 'REG-REAL-21', '2026-01-01 10:00:00', 'CONFIRMED', 4, 7, 1021, 1021, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1022, 'REG-REAL-22', '2026-01-01 10:00:00', 'CONFIRMED', 4, 8, 1022, 1022, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1023, 'REG-REAL-23', '2026-01-01 10:00:00', 'CONFIRMED', 4, 9, 1023, 1023, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1024, 'REG-REAL-24', '2026-01-01 10:00:00', 'CONFIRMED', 4, 10, 1024, 1024, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1025, 'REG-REAL-25', '2026-01-01 10:00:00', 'CONFIRMED', 4, 3, 1025, 1025, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1026, 'REG-REAL-26', '2026-01-01 10:00:00', 'CONFIRMED', 4, 4, 1026, 1026, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1027, 'REG-REAL-27', '2026-01-01 10:00:00', 'CONFIRMED', 4, 5, 1027, 1027, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1028, 'REG-REAL-28', '2026-01-01 10:00:00', 'CONFIRMED', 4, 6, 1028, 1028, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1029, 'REG-REAL-29', '2026-01-01 10:00:00', 'CONFIRMED', 4, 7, 1029, 1029, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1030, 'REG-REAL-30', '2026-01-01 10:00:00', 'CONFIRMED', 4, 8, 1030, 1030, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1031, 'REG-REAL-31', '2026-01-01 10:00:00', 'CONFIRMED', 4, 9, 1031, 1031, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1032, 'REG-REAL-32', '2026-01-01 10:00:00', 'CONFIRMED', 4, 10, 1032, 1032, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1033, 'REG-REAL-33', '2026-01-01 10:00:00', 'CONFIRMED', 4, 3, 1033, 1033, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1034, 'REG-REAL-34', '2026-01-01 10:00:00', 'CONFIRMED', 4, 4, 1034, 1034, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1035, 'REG-REAL-35', '2026-01-01 10:00:00', 'CONFIRMED', 4, 5, 1035, 1035, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1036, 'REG-REAL-36', '2026-01-01 10:00:00', 'CONFIRMED', 4, 6, 1036, 1036, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1037, 'REG-REAL-37', '2026-01-01 10:00:00', 'CONFIRMED', 4, 7, 1037, 1037, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1038, 'REG-REAL-38', '2026-01-01 10:00:00', 'CONFIRMED', 4, 8, 1038, 1038, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1039, 'REG-REAL-39', '2026-01-01 10:00:00', 'CONFIRMED', 4, 9, 1039, 1039, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1040, 'REG-REAL-40', '2026-01-01 10:00:00', 'CONFIRMED', 4, 10, 1040, 1040, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1041, 'REG-REAL-41', '2026-01-01 10:00:00', 'CONFIRMED', 4, 3, 1041, 1041, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1042, 'REG-REAL-42', '2026-01-01 10:00:00', 'CONFIRMED', 4, 4, 1042, 1042, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1043, 'REG-REAL-43', '2026-01-01 10:00:00', 'CONFIRMED', 4, 5, 1043, 1043, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1044, 'REG-REAL-44', '2026-01-01 10:00:00', 'CONFIRMED', 4, 6, 1044, 1044, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1045, 'REG-REAL-45', '2026-01-01 10:00:00', 'CONFIRMED', 4, 7, 1045, 1045, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1046, 'REG-REAL-46', '2026-01-01 10:00:00', 'CONFIRMED', 4, 8, 1046, 1046, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1047, 'REG-REAL-47', '2026-01-01 10:00:00', 'CONFIRMED', 4, 9, 1047, 1047, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1048, 'REG-REAL-48', '2026-01-01 10:00:00', 'CONFIRMED', 4, 10, 1048, 1048, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1049, 'REG-REAL-49', '2026-01-01 10:00:00', 'CONFIRMED', 4, 3, 1049, 1049, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1050, 'REG-REAL-50', '2026-01-01 10:00:00', 'CONFIRMED', 4, 4, 1050, 1050, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1051, 'REG-REAL-51', '2026-01-01 10:00:00', 'CONFIRMED', 4, 5, 1051, 1051, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1052, 'REG-REAL-52', '2026-01-01 10:00:00', 'CONFIRMED', 4, 6, 1052, 1052, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1053, 'REG-REAL-53', '2026-01-01 10:00:00', 'CONFIRMED', 4, 7, 1053, 1053, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1054, 'REG-REAL-54', '2026-01-01 10:00:00', 'CONFIRMED', 4, 8, 1054, 1054, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1055, 'REG-REAL-55', '2026-01-01 10:00:00', 'CONFIRMED', 4, 9, 1055, 1055, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1056, 'REG-REAL-56', '2026-01-01 10:00:00', 'CONFIRMED', 4, 10, 1056, 1056, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1057, 'REG-REAL-57', '2026-01-01 10:00:00', 'CONFIRMED', 4, 3, 1057, 1057, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1058, 'REG-REAL-58', '2026-01-01 10:00:00', 'CONFIRMED', 4, 4, 1058, 1058, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1059, 'REG-REAL-59', '2026-01-01 10:00:00', 'CONFIRMED', 4, 5, 1059, 1059, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1060, 'REG-REAL-60', '2026-01-01 10:00:00', 'CONFIRMED', 4, 6, 1060, 1060, 'BOTH', true, true, 800, '2026-01-02 10:00:00');

-- 5. DETAIL PŘIHLÁŠKY (1001-1060)
INSERT INTO registration_entries (id, registration_id, cat_id, show_class, cage_type, neutered, catalog_number) VALUES
                                                                                                                    (1001, 1001, 1001, 'NEUTER', 'OWN_CAGE', true, 1),
                                                                                                                    (1002, 1002, 1002, 'OPEN', 'OWN_CAGE', false, 2),
                                                                                                                    (1003, 1003, 1003, 'KITTEN', 'OWN_CAGE', false, 3),
                                                                                                                    (1004, 1004, 1004, 'KITTEN', 'OWN_CAGE', false, 4),
                                                                                                                    (1005, 1005, 1005, 'KITTEN', 'OWN_CAGE', false, 5),
                                                                                                                    (1006, 1006, 1006, 'KITTEN', 'OWN_CAGE', false, 6),
                                                                                                                    (1007, 1007, 1007, 'KITTEN', 'OWN_CAGE', false, 7),
                                                                                                                    (1008, 1008, 1008, 'LITTER', 'OWN_CAGE', false, 8),
                                                                                                                    (1009, 1009, 1009, 'KITTEN', 'OWN_CAGE', false, 9),
                                                                                                                    (1010, 1010, 1010, 'KITTEN', 'OWN_CAGE', false, 10),
                                                                                                                    (1011, 1011, 1011, 'JUNIOR', 'OWN_CAGE', false, 11),
                                                                                                                    (1012, 1012, 1012, 'OPEN', 'OWN_CAGE', false, 12),
                                                                                                                    (1013, 1013, 1013, 'NEUTER', 'OWN_CAGE', true, 13),
                                                                                                                    (1014, 1014, 1014, 'NEUTER', 'OWN_CAGE', true, 14),
                                                                                                                    (1015, 1015, 1015, 'JUNIOR', 'OWN_CAGE', true, 15),
                                                                                                                    (1016, 1016, 1016, 'JUNIOR', 'OWN_CAGE', false, 16),
                                                                                                                    (1017, 1017, 1017, 'JUNIOR', 'OWN_CAGE', false, 17),
                                                                                                                    (1018, 1018, 1018, 'KITTEN', 'OWN_CAGE', false, 18),
                                                                                                                    (1019, 1019, 1019, 'DOMESTIC_CAT', 'OWN_CAGE', true, 19),
                                                                                                                    (1020, 1020, 1020, 'JUNIOR', 'OWN_CAGE', false, 20),
                                                                                                                    (1021, 1021, 1021, 'JUNIOR', 'OWN_CAGE', false, 21),
                                                                                                                    (1022, 1022, 1022, 'VETERAN', 'OWN_CAGE', true, 22),
                                                                                                                    (1023, 1023, 1023, 'OPEN', 'OWN_CAGE', false, 23),
                                                                                                                    (1024, 1024, 1024, 'KITTEN', 'OWN_CAGE', false, 24),
                                                                                                                    (1025, 1025, 1025, 'JUNIOR', 'OWN_CAGE', false, 25),
                                                                                                                    (1026, 1026, 1026, 'DOMESTIC_CAT', 'OWN_CAGE', true, 26),
                                                                                                                    (1027, 1027, 1027, 'JUNIOR', 'OWN_CAGE', false, 27),
                                                                                                                    (1028, 1028, 1028, 'OPEN', 'OWN_CAGE', false, 28),
                                                                                                                    (1029, 1029, 1029, 'JUNIOR', 'OWN_CAGE', false, 29),
                                                                                                                    (1030, 1030, 1030, 'OPEN', 'OWN_CAGE', false, 30),
                                                                                                                    (1031, 1031, 1031, 'DOMESTIC_CAT', 'OWN_CAGE', true, 31),
                                                                                                                    (1032, 1032, 1032, 'KITTEN', 'OWN_CAGE', false, 32),
                                                                                                                    (1033, 1033, 1033, 'LITTER', 'OWN_CAGE', false, 33),
                                                                                                                    (1034, 1034, 1034, 'OPEN', 'OWN_CAGE', false, 34),
                                                                                                                    (1035, 1035, 1035, 'OPEN', 'OWN_CAGE', false, 35),
                                                                                                                    (1036, 1036, 1036, 'OPEN', 'OWN_CAGE', false, 36),
                                                                                                                    (1037, 1037, 1037, 'JUNIOR', 'OWN_CAGE', false, 37),
                                                                                                                    (1038, 1038, 1038, 'OPEN', 'OWN_CAGE', false, 38),
                                                                                                                    (1039, 1039, 1039, 'KITTEN', 'OWN_CAGE', false, 39),
                                                                                                                    (1040, 1040, 1040, 'JUNIOR', 'OWN_CAGE', false, 40),
                                                                                                                    (1041, 1041, 1041, 'KITTEN', 'OWN_CAGE', false, 41),
                                                                                                                    (1042, 1042, 1042, 'KITTEN', 'OWN_CAGE', true, 42),
                                                                                                                    (1043, 1043, 1043, 'NEUTER', 'OWN_CAGE', true, 43),
                                                                                                                    (1044, 1044, 1044, 'OPEN', 'OWN_CAGE', false, 44),
                                                                                                                    (1045, 1045, 1045, 'JUNIOR', 'OWN_CAGE', false, 45),
                                                                                                                    (1046, 1046, 1046, 'JUNIOR', 'OWN_CAGE', false, 46),
                                                                                                                    (1047, 1047, 1047, 'JUNIOR', 'OWN_CAGE', false, 47),
                                                                                                                    (1048, 1048, 1048, 'KITTEN', 'OWN_CAGE', false, 48),
                                                                                                                    (1049, 1049, 1049, 'OPEN', 'OWN_CAGE', false, 49),
                                                                                                                    (1050, 1050, 1050, 'OPEN', 'OWN_CAGE', false, 50),
                                                                                                                    (1051, 1051, 1051, 'JUNIOR', 'OWN_CAGE', false, 51),
                                                                                                                    (1052, 1052, 1052, 'JUNIOR', 'OWN_CAGE', false, 52),
                                                                                                                    (1053, 1053, 1053, 'JUNIOR', 'OWN_CAGE', false, 53),
                                                                                                                    (1054, 1054, 1054, 'JUNIOR', 'OWN_CAGE', false, 54),
                                                                                                                    (1055, 1055, 1055, 'KITTEN', 'OWN_CAGE', false, 55),
                                                                                                                    (1056, 1056, 1056, 'JUNIOR', 'OWN_CAGE', false, 56),
                                                                                                                    (1057, 1057, 1057, 'KITTEN', 'OWN_CAGE', false, 57),
                                                                                                                    (1058, 1058, 1058, 'JUNIOR', 'OWN_CAGE', false, 58),
                                                                                                                    (1059, 1059, 1059, 'JUNIOR', 'OWN_CAGE', false, 59),
                                                                                                                    (1060, 1060, 1060, 'KITTEN', 'OWN_CAGE', false, 60);

-- ==========================================
-- 6. RESETOVÁNÍ SEKVENCI
-- ==========================================
SELECT setval(pg_get_serial_sequence('_user', 'id'), coalesce(max(id),0) + 1, false) FROM _user;
SELECT setval(pg_get_serial_sequence('judges', 'id'), coalesce(max(id),0) + 1, false) FROM judges;
SELECT setval(pg_get_serial_sequence('shows', 'id'), coalesce(max(id),0) + 1, false) FROM shows;
SELECT setval(pg_get_serial_sequence('owners', 'id'), coalesce(max(id),0) + 1, false) FROM owners;
SELECT setval(pg_get_serial_sequence('breeders', 'id'), coalesce(max(id),0) + 1, false) FROM breeders;
SELECT setval(pg_get_serial_sequence('cats', 'id'), coalesce(max(id),0) + 1, false) FROM cats;
SELECT setval(pg_get_serial_sequence('registrations', 'id'), coalesce(max(id),0) + 1, false) FROM registrations;
SELECT setval(pg_get_serial_sequence('registration_entries', 'id'), coalesce(max(id),0) + 1, false) FROM registration_entries;