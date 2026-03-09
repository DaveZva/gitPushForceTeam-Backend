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
                                                                                                  (10, 'Karel', 'Novotný', 'karel.novotny@email.cz', '$2a$10$8.UnVuG9HLROJOsIpiNdqO6.E9.TjT9v5xQe7/K7c0E9TjT9v5xQe7/K', false, 'USER');

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
                                                                                                                                                                                                                                            (1, 'Jarní výstava koček Praha', 'Výstava s kapacitou 50 koček', 'OPEN', 'Hala 1', 'Ulice 1', 'Praha', 'CZ', '10000', '2026-04-10 08:00:00', '2026-04-11 18:00:00', '2026-03-30 23:59:59', 'ZO Praha', 'info@praha.cz', 'www.praha.cz', 50),
                                                                                                                                                                                                                                            (2, 'Národní výstava koček Brno', 'Výstava s kapacitou 200 koček', 'OPEN', 'Hala 2', 'Ulice 2', 'Brno', 'CZ', '60000', '2026-05-10 08:00:00', '2026-05-11 18:00:00', '2026-04-30 23:59:59', 'ZO Brno', 'info@brno.cz', 'www.brno.cz', 200),
                                                                                                                                                                                                                                            (3, 'Mezinárodní výstava koček Ostrava', 'Výstava s kapacitou 400 koček', 'OPEN', 'Hala 3', 'Ulice 3', 'Ostrava', 'CZ', '70000', '2026-06-10 08:00:00', '2026-06-11 18:00:00', '2026-05-30 23:59:59', 'ZO Ostrava', 'info@ostrava.cz', 'www.ostrava.cz', 400),
                                                                                                                                                                                                                                            (4, 'Speciální reálná výstava (Ostrava)', 'Speciální výstava vytvořená pro testování přeřazování koček a BIV, s reálnými daty koček.', 'OPEN', 'Výstaviště Černá Louka', 'Černá louka', 'Ostrava', 'CZ', '70200', '2026-08-10 08:00:00', '2026-08-11 18:00:00', '2026-07-30 23:59:59', 'ZO Ostrava', 'info@ostrava.cz', 'www.ostrava.cz', 100);

INSERT INTO show_judges (show_id, judge_id) VALUES
                                                (1, 1), (1, 2),
                                                (2, 1), (2, 2), (2, 3), (2, 4), (2, 5),
                                                (3, 1), (3, 2), (3, 3), (3, 4), (3, 5), (3, 6), (3, 7), (3, 8), (3, 9), (3, 10),
                                                (4, 11), (4, 12), (4, 13), (4, 14);

-- ==========================================
-- 4. MAJITELÉ A CHOVATELÉ (owners & breeders)
-- ==========================================
INSERT INTO owners (id, first_name, last_name, email, city, address, zip, phone, owner_membership_number, owner_local_organization)
SELECT i, 'Majitel' || i, 'Příjmení' || i, 'majitel' || i || '@email.cz', 'Město ' || (i%5), 'Ulice ' || i, '10000', '+420700000' || LPAD(i::text, 3, '0'), 'CZ-' || i, 'ZO ' || (i%3)
FROM generate_series(1, 50) as i;

INSERT INTO breeders (id, first_name, last_name, city)
SELECT i, 'Chovatel' || i, 'Rodina' || i, 'Město ' || (i%5)
FROM generate_series(1, 50) as i;

-- =========================================================================================
-- ČÁST A: GENEROVANÁ DATA PRO VÝSTAVU 1 (Omezeno na 100 koček)
-- =========================================================================================
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
FROM generate_series(1, 100) as i;

INSERT INTO registrations (id, registration_number, created_at, status, show_id, user_id, owner_id, breeder_id, days, data_accuracy, gdpr_consent, amount_paid, paid_at)
SELECT
    i,
    'REG-' || LPAD(i::text, 6, '0'),
    '2026-01-01 10:00:00'::timestamp,
    'CONFIRMED',
    1,
    (i % 8) + 3,
    ((i - 1) % 50) + 1,
    ((i - 1) % 50) + 1,
    'BOTH',
    true,
    true,
    800,
    '2026-01-02 10:00:00'::timestamp
FROM generate_series(1, 100) as i;

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
FROM generate_series(1, 100) as i;

-- =========================================================================================
-- ČÁST B: REÁLNÁ DATA Z CSV (Pro speciální výstavu ID 4, kočky ID 1001-1034)
-- OPRAVENÁ UNIKÁTNÍ ČÍSLA PEDIGREE PRO DOMÁCÍ KOČKY
-- =========================================================================================

INSERT INTO cats (id, user_id, cat_name, ems_code, birth_date, pedigree_number, chip_number, breed, gender, category, father_name, mother_name) VALUES
                                                                                                                                                    (1001, 3, 'GIC Marquez Olicats * PL', 'RAG n 03', '2021-03-22', '(CZ)ČSCH LO 409/21/RAG', '616093900978057', 'RAG', 'MALE', 1, 'CH Daster von Werbellinsee', 'Tila Tequila Global House, PL'),
                                                                                                                                                    (1002, 4, 'JCH, KCH Guinevere DB Crystal Jewels *CZ, JW', 'RAG n 03', '2024-08-03', '(CZ) ČSCH LO 835/24/RAG', '900182002361516', 'RAG', 'FEMALE', 1, 'SC Dakar Nekonomicon *PL, DVM', 'CEW`24,CH,JCH Blue Crystal Jewels *CZ. JW'),
                                                                                                                                                    (1003, 5, 'Koko JG Crystal Jewels * CZ', 'RAG a 03', '2025-08-20', '(CZ) ČSCH LO 1072/25/RAG', '993021200020245', 'RAG', 'FEMALE', 1, 'CH, JCH,KCH SE*Lil`Magics Hit the Road Jack, JW', 'JCH, KCH Guinevere DB Crystal Jewels * CZ, JW'),
                                                                                                                                                    (1004, 6, 'Kokopelli JG Crystal Jewels * CZ', 'RAG a 03', '2025-08-20', '(CZ) ČSCH LO 1071/25/RAG', '993021200020248', 'RAG', 'MALE', 1, 'CH, JCH, KCH SE*Lil`Magics Hit the Road Jack, JW', 'JCH, KCH Guinevere DB Crystal Jewels * CZ, JW'),
                                                                                                                                                    (1005, 7, 'Hakuna Matata of Themyscira * CZ', 'MCO n 22', '2025-09-04', '(CZ)ČSCH LO 1750/25/MCO', '901001000462569', 'MCO', 'FEMALE', 2, 'Dorian Ilower*PL', 'Diana Darling of Sand Mines*CZ'),
                                                                                                                                                    (1006, 8, 'Heimdall of Themyscira*CZ', 'MCO a 23', '2025-09-04', '(CZ)ČSCH LO 1748/25/MCO', '901001000465553', 'MCO', 'MALE', 2, 'Dorian Ilower*PL', 'Diana Darling of Sand Mines*CZ'),
                                                                                                                                                    (1007, 9, 'Hellcat of Themyscira * CZ', 'MCO n 22', '2025-09-04', '(CZ) ČSCH LO 1752/25/MCO', '901001000462571', 'MCO', 'FEMALE', 2, 'Dorian Ilower*PL', 'Diana Darling of Sand Mines*CZ'),
                                                                                                                                                    (1008, 10, 'Hakuna Matata 2 of Themyscira * CZ', 'MCO n 22', '2025-09-04', '(CZ) ČSCH LO 1643/25/MCO', '901001200462569', 'MCO', 'FEMALE', 2, 'Dorian Ilower*PL', 'Diana Darling of Sand Mines*CZ'),
                                                                                                                                                    (1009, 3, 'UA*Arine Wisla', 'MCO n 25', '2025-08-21', 'zažádáno-1009', '900255003226590', 'MCO', 'FEMALE', 2, 'UA*Arine Valentino', 'UA*Arine Fortezza'),
                                                                                                                                                    (1010, 4, 'Chilli Coon Zlata Star*CZ', 'MCO n 25', '2025-10-23', '(CZ) ČSCH LO 8/26/MCO', '981020004701672', 'MCO', 'MALE', 2, 'Xell Bonito Lucky life *CZ', 'Esmeralda Zlata Star-CZ'),
                                                                                                                                                    (1011, 5, 'Corraline Golden Sands', 'MCO n 09 22', '2025-04-25', '(SK)FFS LO 00835', '941000029980826', 'MCO', 'FEMALE', 2, 'IC Ennio Moricone by Imagine Glamour*CZ', 'IC Fruzia Koci Zaulek*PL'),
                                                                                                                                                    (1012, 6, 'JCH, KCH Living Legend of Desomena', 'MCO d 09', '2024-11-21', '(HU) FH LO 18675', '900085001719463', 'MCO', 'MALE', 2, 'Moonrisecoon Damian', 'Pawsome Giants Roxen'),
                                                                                                                                                    (1013, 7, 'Luisa Nonstop Naked, CZ', 'SPH n', '2024-09-03', '(CZ)ČSCH LO 133/24/SPH', '953010007450362', 'SPH', 'FEMALE', 4, 'Maverick of Ankh Amulet', 'GIC Hope Trojan Hill'),
                                                                                                                                                    (1014, 8, 'Leia Nonstop Naked, CZ', 'SPH n 09', '2024-09-03', '(CZ)ČSCH LO 131/24/SPH', '953010007676238', 'SPH', 'FEMALE', 4, 'Maverick of Ankh Amulet', 'GIC Hope Trojan Hill'),
                                                                                                                                                    (1015, 9, 'KCH Alice De La Cat''Suki', 'BSH c 02 62', '2025-05-06', '(CZ)ČSCH LO 847/25/BSH', '250269611411404', 'BSH', 'FEMALE', 3, 'Uggi of Black Amazon, CZ', 'Juliana of Black Amazon, CZ'),
                                                                                                                                                    (1016, 10, 'KCH Avalon Furry Dynastia, CZ', 'BSH p 03', '2025-04-29', '(CZ)ČSCH LO 591/25/BSH', '900085001853676', 'BSH', 'MALE', 3, 'Alexxmax Cocotello*PL', 'JCH KCH CH Hawa of Black Amazon, CZ'),
                                                                                                                                                    (1017, 3, 'KCH Black Pearl Furry Dynastia, CZ', 'BSH n', '2025-05-03', '(CZ) ČSCH LO 598/25/BSH', '900085001751387', 'BSH', 'FEMALE', 3, 'KCH JCH IC Cheddar of Black Amazon, CZ', 'KCH JCH Koenigsee Jocelyn'),
                                                                                                                                                    (1018, 4, 'Zenobia of Black Amazon, CZ', 'BLH h', '2025-09-06', '(CZ) ČSCH LO 110/25/BLH', '203164000194090', 'BLH', 'FEMALE', 3, 'Alexxmax Cocotello, PL', 'JCH Cora of Black Amazon, CZ'),
                                                                                                                                                    (1019, 5, 'Lexy alias Skřítek', 'HCL', '2024-04-24', 'NO-PP-1019', '900163000164216', 'HCL', 'FEMALE', 4, '', ''),
                                                                                                                                                    (1020, 6, 'KCH Bambi Furry Dynastia, CZ', 'BSH o 03', '2025-05-03', '(CZ)ČSCH LO 596/25/BSH', '900085001751393', 'BSH', 'MALE', 3, 'JCH KCH IC Cheddar of Black Amazon, CZ', 'JCH KCH Koenigsee Jocelyn*DE'),
                                                                                                                                                    (1021, 7, 'KCH Aloha Furry Dynastia, CZ', 'BSH g 03', '2025-04-29', '(CZ)ČSCH LO 595/25/BSH', '900085001853679', 'BSH', 'FEMALE', 3, 'Alexxmax Cocotello*PL', 'JCH KCH CH Hawa of Black Amazon, CZ'),
                                                                                                                                                    (1022, 8, 'IC Daysy Błękitek* PL', 'RUS', '2017-03-07', 'ČSCH LO 107/17/RUS', '900164001513868', 'RUS', 'FEMALE', 4, 'CH Kind of Blue Akamaru', 'Wild Yoko Błękitek, PL'),
                                                                                                                                                    (1023, 9, 'Makronka Antilie, CZ', 'BSH e', '2025-04-01', '(CZ)ČSCH LO 327/25 BSH', '900085001851645', 'BSH', 'FEMALE', 3, 'George Teczowy Agat*PL', 'Jolen Dowgar Hill*PL'),
                                                                                                                                                    (1024, 10, 'Cherine Dakkar*SK', 'CRX d 03 24', '2025-08-10', '(SK)FFS LO 00946', '900085002026202', 'CRX', 'FEMALE', 4, 'Ursus Kocia Drzemka*PL', 'CH Ganga Dakkar*SK'),
                                                                                                                                                    (1025, 3, 'Hemilia Marelax Pride', 'MCO fs 03', '2025-06-14', 'UAS 7683-25/MCO', '900255000962307', 'MCO', 'FEMALE', 2, 'JACK POT MARELAX PRIDE', 'Quincy Marelax Pride'),
                                                                                                                                                    (1026, 4, 'Kolja', 'HCS', '2018-08-24', 'NO-PP-1026', '900267000024205', 'HCS', 'MALE', 4, '', ''),
                                                                                                                                                    (1027, 5, 'Yorick of Black Amazon, CZ', 'BSH e 02 62', '2025-05-15', 'LO 690/25/BSH', '203164000194001', 'BSH', 'MALE', 3, 'IC Sebastiano of Black Amazon, CZ', 'Azalea Black Amazon, CZ'),
                                                                                                                                                    (1028, 6, 'Yuuto Yutani Cat`s Wine, CZ', 'SIB d 22', '2024-08-14', 'BCC-2025 LO/7420/SIB', '963002000098751', 'SIB', 'MALE', 2, 'Fabien Lesnoe Tzarstvo - E', 'Hope Top*Matryoshka'),
                                                                                                                                                    (1029, 7, 'Altynai Notre Dame de Joye', 'THA n', '2025-06-12', '(CZ) ČSCH LO 5/26/THA', '250268781763944', 'THA', 'FEMALE', 4, 'GR.CH.EUR. THONGCHAI NOTRE DAME DE JOYE', 'PINK PROUD´S ZARA ANGKANA'),
                                                                                                                                                    (1030, 8, 'Super Rich''s Chatmanee Si Fah', 'THA a', '2022-01-05', '(CZ)ČSCH LO 01/23/THA', '900219001090155', 'THA', 'FEMALE', 4, 'Super Rich', 'Thong Yib'),
                                                                                                                                                    (1031, 9, 'Třešnička', 'HCS', '2024-06-27', 'NO-PP-1031', '203098100594717', 'HCS', 'FEMALE', 4, '', ''),
                                                                                                                                                    (1032, 10, 'Hope Your Grace, CZ', 'BML ns 11 31', '2025-11-23', 'ČSCH RX 16/25/BML', '203098100603862', 'BML', 'FEMALE', 3, 'Radek Momoiro, CZ', 'CH SE Grayscale´s Maluhia'),
                                                                                                                                                    (1033, 3, 'Vrh H Your Grace, CZ', 'BML', '2025-11-23', 'LITTER-1033', 'NO-CHIP-1033', 'BML', 'MALE', 3, 'Radek Momoiro, CZ', 'CH SE Grayscale´s Maluhia'),
                                                                                                                                                    (1034, 4, 'JCH Engee Chalomon, CZ', 'BEN n 24', '2025-03-01', '(CZ) ČSCH LO 50/25/BEN', '203164000183629', 'BEN', 'FEMALE', 3, 'Valerian Felis Empire, CZ', 'Wanessa Klarisa Taylor Wild Cat, CZ');

INSERT INTO registrations (id, registration_number, created_at, status, show_id, user_id, owner_id, breeder_id, days, data_accuracy, gdpr_consent, amount_paid, paid_at) VALUES
                                                                                                                                                                             (1001, 'REG-REALL-01', '2026-01-01 10:00:00', 'CONFIRMED', 4, 3, 1, 1, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1002, 'REG-REALL-02', '2026-01-01 10:00:00', 'CONFIRMED', 4, 4, 2, 2, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1003, 'REG-REALL-03', '2026-01-01 10:00:00', 'CONFIRMED', 4, 5, 3, 3, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1004, 'REG-REALL-04', '2026-01-01 10:00:00', 'CONFIRMED', 4, 6, 4, 4, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1005, 'REG-REALL-05', '2026-01-01 10:00:00', 'CONFIRMED', 4, 7, 5, 5, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1006, 'REG-REALL-06', '2026-01-01 10:00:00', 'CONFIRMED', 4, 8, 6, 6, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1007, 'REG-REALL-07', '2026-01-01 10:00:00', 'CONFIRMED', 4, 9, 7, 7, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1008, 'REG-REALL-08', '2026-01-01 10:00:00', 'CONFIRMED', 4, 10, 8, 8, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1009, 'REG-REALL-09', '2026-01-01 10:00:00', 'CONFIRMED', 4, 3, 9, 9, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1010, 'REG-REALL-10', '2026-01-01 10:00:00', 'CONFIRMED', 4, 4, 10, 10, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1011, 'REG-REALL-11', '2026-01-01 10:00:00', 'CONFIRMED', 4, 5, 11, 11, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1012, 'REG-REALL-12', '2026-01-01 10:00:00', 'CONFIRMED', 4, 6, 12, 12, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1013, 'REG-REALL-13', '2026-01-01 10:00:00', 'CONFIRMED', 4, 7, 13, 13, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1014, 'REG-REALL-14', '2026-01-01 10:00:00', 'CONFIRMED', 4, 8, 14, 14, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1015, 'REG-REALL-15', '2026-01-01 10:00:00', 'CONFIRMED', 4, 9, 15, 15, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1016, 'REG-REALL-16', '2026-01-01 10:00:00', 'CONFIRMED', 4, 10, 16, 16, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1017, 'REG-REALL-17', '2026-01-01 10:00:00', 'CONFIRMED', 4, 3, 17, 17, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1018, 'REG-REALL-18', '2026-01-01 10:00:00', 'CONFIRMED', 4, 4, 18, 18, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1019, 'REG-REALL-19', '2026-01-01 10:00:00', 'CONFIRMED', 4, 5, 19, 19, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1020, 'REG-REALL-20', '2026-01-01 10:00:00', 'CONFIRMED', 4, 6, 20, 20, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1021, 'REG-REALL-21', '2026-01-01 10:00:00', 'CONFIRMED', 4, 7, 21, 21, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1022, 'REG-REALL-22', '2026-01-01 10:00:00', 'CONFIRMED', 4, 8, 22, 22, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1023, 'REG-REALL-23', '2026-01-01 10:00:00', 'CONFIRMED', 4, 9, 23, 23, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1024, 'REG-REALL-24', '2026-01-01 10:00:00', 'CONFIRMED', 4, 10, 24, 24, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1025, 'REG-REALL-25', '2026-01-01 10:00:00', 'CONFIRMED', 4, 3, 25, 25, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1026, 'REG-REALL-26', '2026-01-01 10:00:00', 'CONFIRMED', 4, 4, 26, 26, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1027, 'REG-REALL-27', '2026-01-01 10:00:00', 'CONFIRMED', 4, 5, 27, 27, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1028, 'REG-REALL-28', '2026-01-01 10:00:00', 'CONFIRMED', 4, 6, 28, 28, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1029, 'REG-REALL-29', '2026-01-01 10:00:00', 'CONFIRMED', 4, 7, 29, 29, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1030, 'REG-REALL-30', '2026-01-01 10:00:00', 'CONFIRMED', 4, 8, 30, 30, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1031, 'REG-REALL-31', '2026-01-01 10:00:00', 'CONFIRMED', 4, 9, 31, 31, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1032, 'REG-REALL-32', '2026-01-01 10:00:00', 'CONFIRMED', 4, 10, 32, 32, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1033, 'REG-REALL-33', '2026-01-01 10:00:00', 'CONFIRMED', 4, 3, 33, 33, 'BOTH', true, true, 800, '2026-01-02 10:00:00'),
                                                                                                                                                                             (1034, 'REG-REALL-34', '2026-01-01 10:00:00', 'CONFIRMED', 4, 4, 34, 34, 'BOTH', true, true, 800, '2026-01-02 10:00:00');

INSERT INTO registration_entries (id, registration_id, cat_id, show_class, cage_type, neutered, catalog_number) VALUES
                                                                                                                    (1001, 1001, 1001, 'GRANT_INTER_CHAMPION', 'OWN_CAGE', true, 1001),
                                                                                                                    (1002, 1002, 1002, 'JUNIOR', 'OWN_CAGE', false, 1002),
                                                                                                                    (1003, 1003, 1003, 'OPEN', 'OWN_CAGE', false, 1003),
                                                                                                                    (1004, 1004, 1004, 'OPEN', 'OWN_CAGE', false, 1004),
                                                                                                                    (1005, 1005, 1005, 'OPEN', 'OWN_CAGE', false, 1005),
                                                                                                                    (1006, 1006, 1006, 'OPEN', 'OWN_CAGE', false, 1006),
                                                                                                                    (1007, 1007, 1007, 'OPEN', 'OWN_CAGE', false, 1007),
                                                                                                                    (1008, 1008, 1008, 'OPEN', 'OWN_CAGE', false, 1008),
                                                                                                                    (1009, 1009, 1009, 'OPEN', 'OWN_CAGE', false, 1009),
                                                                                                                    (1010, 1010, 1010, 'OPEN', 'OWN_CAGE', false, 1010),
                                                                                                                    (1011, 1011, 1011, 'OPEN', 'OWN_CAGE', false, 1011),
                                                                                                                    (1012, 1012, 1012, 'JUNIOR', 'OWN_CAGE', false, 1012),
                                                                                                                    (1013, 1013, 1013, 'NEUTER', 'OWN_CAGE', true, 1013),
                                                                                                                    (1014, 1014, 1014, 'NEUTER', 'OWN_CAGE', true, 1014),
                                                                                                                    (1015, 1015, 1015, 'NEUTER', 'OWN_CAGE', true, 1015),
                                                                                                                    (1016, 1016, 1016, 'JUNIOR', 'OWN_CAGE', false, 1016),
                                                                                                                    (1017, 1017, 1017, 'JUNIOR', 'OWN_CAGE', false, 1017),
                                                                                                                    (1018, 1018, 1018, 'OPEN', 'OWN_CAGE', false, 1018),
                                                                                                                    (1019, 1019, 1019, 'DOMESTIC_CAT', 'OWN_CAGE', true, 1019),
                                                                                                                    (1020, 1020, 1020, 'JUNIOR', 'OWN_CAGE', false, 1020),
                                                                                                                    (1021, 1021, 1021, 'JUNIOR', 'OWN_CAGE', false, 1021),
                                                                                                                    (1022, 1022, 1022, 'INTERNATIONAL_PREMIER', 'OWN_CAGE', true, 1022),
                                                                                                                    (1023, 1023, 1023, 'OPEN', 'OWN_CAGE', false, 1023),
                                                                                                                    (1024, 1024, 1024, 'OPEN', 'OWN_CAGE', false, 1024),
                                                                                                                    (1025, 1025, 1025, 'OPEN', 'OWN_CAGE', false, 1025),
                                                                                                                    (1026, 1026, 1026, 'DOMESTIC_CAT', 'OWN_CAGE', true, 1026),
                                                                                                                    (1027, 1027, 1027, 'OPEN', 'OWN_CAGE', false, 1027),
                                                                                                                    (1028, 1028, 1028, 'OPEN', 'OWN_CAGE', false, 1028),
                                                                                                                    (1029, 1029, 1029, 'OPEN', 'OWN_CAGE', false, 1029),
                                                                                                                    (1030, 1030, 1030, 'OPEN', 'OWN_CAGE', false, 1030),
                                                                                                                    (1031, 1031, 1031, 'DOMESTIC_CAT', 'OWN_CAGE', true, 1031),
                                                                                                                    (1032, 1032, 1032, 'OPEN', 'OWN_CAGE', false, 1032),
                                                                                                                    (1033, 1033, 1033, 'LITTER', 'OWN_CAGE', false, 1033),
                                                                                                                    (1034, 1034, 1034, 'JUNIOR', 'OWN_CAGE', false, 1034);

SELECT setval(pg_get_serial_sequence('_user', 'id'), coalesce(max(id),0) + 1, false) FROM _user;
SELECT setval(pg_get_serial_sequence('judges', 'id'), coalesce(max(id),0) + 1, false) FROM judges;
SELECT setval(pg_get_serial_sequence('shows', 'id'), coalesce(max(id),0) + 1, false) FROM shows;
SELECT setval(pg_get_serial_sequence('owners', 'id'), coalesce(max(id),0) + 1, false) FROM owners;
SELECT setval(pg_get_serial_sequence('breeders', 'id'), coalesce(max(id),0) + 1, false) FROM breeders;
SELECT setval(pg_get_serial_sequence('cats', 'id'), coalesce(max(id),0) + 1, false) FROM cats;
SELECT setval(pg_get_serial_sequence('registrations', 'id'), coalesce(max(id),0) + 1, false) FROM registrations;
SELECT setval(pg_get_serial_sequence('registration_entries', 'id'), coalesce(max(id),0) + 1, false) FROM registration_entries;