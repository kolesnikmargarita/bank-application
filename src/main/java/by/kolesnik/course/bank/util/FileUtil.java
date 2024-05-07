package by.kolesnik.course.bank.util;

import java.util.Optional;

public class FileUtil {
    
    private FileUtil() {
    }

    public static String getFileExtension(String filename) {
        Optional<String> extension = Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".")));

        return extension.orElse("");
    }
}

