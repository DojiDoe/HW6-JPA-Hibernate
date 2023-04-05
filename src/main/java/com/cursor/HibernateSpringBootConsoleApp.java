package com.cursor;

import com.cursor.entity.User;
import com.cursor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class HibernateSpringBootConsoleApp implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(HibernateSpringBootConsoleApp.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        final String menu = """
                ---Menu---
                1. Create new User - /add
                2. Delete User by id - /delete
                3. Get all users - /list
                4. Get user by id - /user_by_id
                5. Get user by email - /user_by_email
                6. Update user info - /update
                7. Exit - /exit""";

        System.out.println(menu);
        do {
            Scanner scanner = new Scanner(System.in);
            String option = scanner.next();
            switch (option) {
                case "/add" -> {
                    System.out.println("Enter User name:");
                    scanner.nextLine();
                    String name = scanner.next();
                    System.out.println("Enter User email:");
                    String email = scanner.next();
                    System.out.println("Enter User age:");
                    int age = scanner.nextInt();
                    User user = new User(name, email, age);
                    userRepository.upsert(user);
                    System.out.println("User: " + user + " has been added");
                }
                case "/delete" -> {
                    System.out.println("Enter User id:");
                    Long id = scanner.nextLong();
                    userRepository.deleteById(id);
                    System.out.println("User has been deleted");
                }
                case "/list" -> {
                    List<User> list = userRepository.getAll();
                    if (list.isEmpty()) {
                        System.out.println("List is empty");
                    } else {
                        list.forEach(System.out::println);
                    }
                }
                case "/user_by_id" -> {
                    System.out.println("Enter User id:");
                    Long id = scanner.nextLong();
                    if (userRepository.getById(id) == null) {
                        System.out.println("No such User");
                    } else {
                        System.out.println("Find User -> " + userRepository.getById(id));
                    }
                }
                case "/user_by_email" -> {
                    System.out.println("Enter User email:");
                    String email = scanner.next();
                    List<User> list = userRepository.getByEmail(email);
                    if (list.isEmpty()) {
                        System.out.println("Don't find such User");
                    } else {
                        System.out.println("Find User -> " + list.get(0));
                    }
                }
                case "/update" -> {
                    System.out.println("Enter User id:");
                    Long id = scanner.nextLong();
                    User user = userRepository.getById(id);
                    if (user == null) {
                        System.out.println("No such User to update");
                    } else {
                        scanner.nextLine();
                        System.out.println("Enter fieldName: ");
                        String fieldName = scanner.nextLine();
                        System.out.println("Enter fieldValue: ");
                        String fieldValue = scanner.nextLine();
                        try {
                            Field field = User.class.getDeclaredField(fieldName);
                            field.setAccessible(true);
                            if (fieldValue.matches("\\d+")) {
                                field.set(user, Integer.valueOf(fieldValue));
                            } else {
                                field.set(user, fieldValue);
                            }
                            userRepository.upsert(user);
                        } catch (NoSuchFieldException ex) {
                            System.out.println("Field not found");
                        }
                        System.out.println("User is successfully updated!");
                    }
                }
                case "/exit" -> {
                    System.exit(0);
                }
            }
        } while (true);
    }
}
