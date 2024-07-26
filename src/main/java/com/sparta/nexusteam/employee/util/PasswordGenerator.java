package com.sparta.nexusteam.employee.util;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PasswordGenerator {

    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIALS = "@$!%*?&";
    private static final String ALL = LOWERCASE + DIGITS + SPECIALS;
    private static final int PASSWORD_LENGTH = 8;
    private static final SecureRandom random = new SecureRandom();

    public static String generatePassword() {
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);

        // Add one random character from each category
        password.append(LOWERCASE.charAt(random.nextInt(LOWERCASE.length())));
        password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        password.append(SPECIALS.charAt(random.nextInt(SPECIALS.length())));

        // Fill remaining spots with random characters from all categories
        for (int i = 3; i < PASSWORD_LENGTH; i++) {
            password.append(ALL.charAt(random.nextInt(ALL.length())));
        }

        // Shuffle the characters to avoid predictable patterns
        return shuffleString(password.toString());
    }

    private static String shuffleString(String string) {
        List<Character> characters = string.chars()
                .mapToObj(e -> (char) e)
                .collect(Collectors.toList());
        Collections.shuffle(characters);
        StringBuilder shuffledString = new StringBuilder();
        characters.forEach(shuffledString::append);
        return shuffledString.toString();
    }
}
