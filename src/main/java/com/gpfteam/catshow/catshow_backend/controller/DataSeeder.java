package com.gpfteam.catshow.catshow_backend.controller;

import com.gpfteam.catshow.catshow_backend.model.*;
import com.gpfteam.catshow.catshow_backend.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ShowRepository showRepository;
    private final JudgeRepository judgeRepository;
    private final CatRepository catRepository;
    private final OwnerRepository ownerRepository;
    private final BreederRepository breederRepository;
    private final RegistrationRepository registrationRepository;
    private final RegistrationEntryRepository registrationEntryRepository;
    private final PasswordEncoder passwordEncoder;

    private final Random random = new Random();

    // Pomocná data pro generování
    private static final String[] FIRST_NAMES = {"Petr", "Jana", "Pavel", "Eva", "Martin", "Lucie", "Tomáš", "Kateřina", "Jan", "Anna", "Jiří", "Lenka"};
    private static final String[] LAST_NAMES = {"Novák", "Svobodová", "Dvořák", "Černá", "Procházka", "Kučerová", "Veselý", "Horáková", "Němec", "Marek"};
    private static final String[] CAT_NAMES = {"Mourrek", "Luna", "Garfield", "Minda", "Simba", "Bella", "Leo", "Lilly", "Loki", "Nala", "Micinka", "Felix"};
    private static final String[] BREEDS = {"MCO", "RAG", "BSH", "SIA", "PER", "SPH", "RUS", "BEN"};
    private static final String[] EMS_COLORS = {"n", "a", "d", "e", "f", "g", "w"};
    private static final String[] CITIES = {"Praha", "Brno", "Ostrava", "Plzeň", "Liberec", "Olomouc"};

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Kontrola, zda už data neexistují, aby se neduplikovala
        if (userRepository.count() > 0) {
            log.info("Databáze již obsahuje data. Seeding přeskočen.");
            return;
        }

        log.info("Začínám generovat testovací data...");

        // 1. Vytvoření uživatelů (Admin, Secretariat, Users)
        createUsers();
        List<User> users = userRepository.findAll();
        User defaultUser = users.stream().filter(u -> u.getRole() == Role.USER).findFirst().orElseThrow();

        // 2. Vytvoření posuzovatelů (10+)
        createJudges();
        List<Judge> judges = judgeRepository.findAll();

        // 3. Vytvoření výstavy
        Show show = createShow(judges);

        // 4. Generování registrací a koček (100+)
        createRegistrations(show, users);

        log.info("Seeding dokončen! Databáze je naplněna.");
    }

    private void createUsers() {
        // Admin
        User admin = User.builder()
                .firstName("Admin")
                .lastName("Hlavní")
                .email("admin@test.com")
                .password(passwordEncoder.encode("Heslo123+"))
                .role(Role.ADMIN)
                .build();
        userRepository.save(admin);

        // Secretariat
        User secretariat = User.builder()
                .firstName("Sekretariát")
                .lastName("Výstavní")
                .email("secretariat@test.com")
                .password(passwordEncoder.encode("Heslo123+"))
                .role(Role.SECRETARIAT)
                .build();
        userRepository.save(secretariat);

        // Běžní uživatelé (vytvoříme jich 20, aby se kočky rozprostřely)
        for (int i = 1; i <= 20; i++) {
            User user = User.builder()
                    .firstName(getRandom(FIRST_NAMES))
                    .lastName(getRandom(LAST_NAMES))
                    .email("user" + i + "@test.com")
                    .password(passwordEncoder.encode("Heslo123+"))
                    .role(Role.USER)
                    .build();
            userRepository.save(user);
        }
        log.info("Vytvořeno 22 uživatelů.");
    }

    private void createJudges() {
        for (int i = 1; i <= 10; i++) {
            Judge judge = Judge.builder()
                    .firstName("Judge" + i)
                    .lastName(getRandom(LAST_NAMES))
                    .email("judge" + i + "@catshow.com")
                    .country(i % 2 == 0 ? "CZ" : "PL")
                    .validGroups(List.of("1", "2", "3", "4"))
                    .build();
            judgeRepository.save(judge);
        }
        log.info("Vytvořeno 10 posuzovatelů.");
    }

    private Show createShow(List<Judge> judges) {
        Show show = Show.builder()
                .name("Mezinárodní výstava koček " + LocalDateTime.now().getYear())
                .description("Velká jarní výstava všech plemen.")
                .status(Show.ShowStatus.OPEN) // Otevřeno pro registrace nebo úpravy
                .venueName("Výstaviště Holešovice")
                .venueAddress("Výstaviště 67")
                .venueCity("Praha")
                .venueState("CZ")
                .venueZip("17000")
                .startDate(LocalDateTime.now().plusMonths(1))
                .endDate(LocalDateTime.now().plusMonths(1).plusDays(1))
                .registrationDeadline(LocalDateTime.now().plusWeeks(2))
                .organizerName("ZO Kočky Praha")
                .organizerContactEmail("info@kockypraha.cz")
                .organizerWebsiteUrl("https://kockypraha.cz")
                .maxCats(300)
                .judges(judges) // Přiřadíme všechny vytvořené posuzovatele
                .build();

        return showRepository.save(show);
    }

    private void createRegistrations(Show show, List<User> users) {
        int catsToCreate = 100;
        List<User> regularUsers = users.stream().filter(u -> u.getRole() == Role.USER).toList();

        // Mapa pro "kešování" již vytvořených majitelů, abychom je nevytvářeli duplicitně
        Map<String, Owner> existingOwners = new HashMap<>();

        for (int i = 0; i < catsToCreate; i++) {
            User ownerUser = regularUsers.get(random.nextInt(regularUsers.size()));

            // 1. Získání nebo vytvoření Ownera (aby nebyl duplicitní email)
            Owner regOwner = existingOwners.get(ownerUser.getEmail());

            if (regOwner == null) {
                // Pokud majitel pro tento email ještě v tomto běhu neexistuje, vytvoříme ho
                regOwner = Owner.builder()
                        .firstName(ownerUser.getFirstName())
                        .lastName(ownerUser.getLastName())
                        .email(ownerUser.getEmail())
                        .city(getRandom(CITIES))
                        .address("Ulice " + random.nextInt(100))
                        .zip("100 00")
                        .phone("+420 123 456 789")
                        .ownerMembershipNumber("ZO-" + random.nextInt(9999))
                        .ownerLocalOrganization("ZO Praha")
                        .build();

                regOwner = ownerRepository.save(regOwner);
                existingOwners.put(ownerUser.getEmail(), regOwner);
            }

            // 2. Vytvořit kočku
            Cat cat = createCat(ownerUser, i);
            catRepository.save(cat);

            // 3. Vytvořit Breedera (Chovatele může být víc stejných, tam unique email obvykle nevadí,
            // ale pro jistotu generujeme náhodně bez emailu nebo unikátní)
            Breeder breeder = Breeder.builder()
                    .firstName(getRandom(FIRST_NAMES))
                    .lastName(getRandom(LAST_NAMES))
                    .city(getRandom(CITIES))
                    .build();
            breederRepository.save(breeder);

            // 4. Vytvořit Registraci
            Registration registration = Registration.builder()
                    .registrationNumber(UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                    .status(Registration.RegistrationStatus.CONFIRMED)
                    .show(show)
                    .user(ownerUser)
                    .owner(regOwner) // Použijeme buď nového nebo existujícího ownera
                    .breeder(breeder)
                    .days("BOTH")
                    .dataAccuracy(true)
                    .gdprConsent(true)
                    .amountPaid(1000L)
                    .paidAt(LocalDateTime.now())
                    .build();

            registrationRepository.save(registration);

            // 5. Vytvořit položku registrace (Entry)
            RegistrationEntry entry = RegistrationEntry.builder()
                    .registration(registration)
                    .cat(cat)
                    .showClass(getRandomShowClass())
                    .cageType(RegistrationEntry.CageType.OWN_CAGE)
                    .neutered(false)
                    .catalogNumber(i + 1)
                    .build();

            registrationEntryRepository.save(entry);
        }
        System.out.println(" -> Vytvořeno " + catsToCreate + " registrací (použito " + existingOwners.size() + " unikátních majitelů).");
    }

    private Cat createCat(User owner, int index) {
        String breed = getRandom(BREEDS);
        String gender = random.nextBoolean() ? "MALE" : "FEMALE";

        return Cat.builder()
                .ownerUser(owner)
                .catName(getRandom(CAT_NAMES) + " z " + getRandom(CITIES))
                .emsCode(breed + " " + getRandom(EMS_COLORS) + " 0" + (random.nextInt(3) + 1)) // např. MCO n 03
                .birthDate("202" + random.nextInt(4) + "-05-20")
                .pedigreeNumber("CSCH LO " + (1000 + index) + "/23/" + breed)
                .chipNumber("900123456" + String.format("%06d", index))
                .breed(breed)
                .gender(Cat.Gender.valueOf(gender))
                .category(random.nextInt(4) + 1)
                .fatherName("Father " + index)
                .motherName("Mother " + index)
                .build();
    }

    private RegistrationEntry.ShowClass getRandomShowClass() {
        RegistrationEntry.ShowClass[] classes = RegistrationEntry.ShowClass.values();
        return classes[random.nextInt(classes.length)];
    }

    private String getRandom(String[] array) {
        return array[random.nextInt(array.length)];
    }
}