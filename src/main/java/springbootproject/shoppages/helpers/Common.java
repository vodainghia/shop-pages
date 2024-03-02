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

    public static boolean isTableEmpty(JpaRepository<?, ?> repository) {
        long count = repository.count();
        return count == 0;
    }

}
