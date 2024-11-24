package gb;

import java.time.format.DateTimeFormatter;

public final class Constants {
    private  Constants() {} // Restrict instantiation
    public static boolean verbose = false;

    public static final String DATE_PATTERN = "dd/MM/yyyy";
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern(DATE_PATTERN);
    // ANSI codes
    // Reset
    public static final String RESET = "\033[0m";  // Text Reset

    // Regular Colors
    public static final String RED = "\033[0;31m";     // RED
    public static final String GREEN = "\033[0;32m";   // GREEN
    public static final String YELLOW = "\033[0;33m";  // YELLOW
}