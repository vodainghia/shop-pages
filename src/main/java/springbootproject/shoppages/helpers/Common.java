package springbootproject.shoppages.helpers;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.Random;

public class Common {
    private Common() {
    }

    public static void dump(Object variable) {
        System.err.println("Variable value: " + variable);
        throw new RuntimeException("Execution stopped.");
    }

    public static String[] getEnumNames(Class<? extends Enum<?>> e) {
        return Arrays.stream(e.getEnumConstants()).map(Enum::name).toArray(String[]::new);
    }

    public static boolean isTableEmpty(JpaRepository<?, ?> repository) {
        long count = repository.count();
        return count == 0;
    }

    public static String generateRandomChars(int length, String candidateChars) {
        String chars = candidateChars == null ? "qwertyuiopasdfghjklzxcvbnm1234567890" : candidateChars;

        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars
                    .length())));
        }

        return sb.toString();
    }
}
