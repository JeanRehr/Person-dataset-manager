package gb;

import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ConsoleHandler {
    private final Scanner scanner;

    public ConsoleHandler(Scanner scanner) {
        this.scanner = scanner;
    }

    public void clearConsole() {
        /* ANSI CODE */
        // System.out.println("\033[2J\033[;H"); /* Works but not always */
        final String ANSI_CLS = "\u001b[2J"; /* Clear screen. */
        final String ANSI_HOME = "\u001b[H"; /* Cursor to the top right. */
        System.out.print(ANSI_CLS + ANSI_HOME);
        System.out.flush();
    }

    public static void displayMenu(String menuName, String[] menuItems) {
        System.out.println("----------------------------------------");
        System.out.println(menuName);
        System.out.println("----------------------------------------");
        
        for (int i = 0; i < menuItems.length; i++) {
            System.out.printf("[%1d] %s\n", i+1, menuItems[i]);
        }
    }

    public <T> T parseValue(String input, Class<T> type) {
        try {
            if (type == Integer.class) {
                return type.cast(Integer.parseInt(input));
            } else if (type == String.class) {
                return type.cast(input);
            } else if (type == Character.class) {
                return type.cast(input.charAt(0));
            } else if (type == Double.class) {
                return type.cast(Double.parseDouble(input));
            } else {
                System.out.println("Type not available.");
                return null;
            }
        } catch (Exception e) {
            System.out.println("Not able to parse input.");
            return null;
        }
    }

    public String getNextLine() {
        try {
            return scanner.nextLine();
        } catch (Exception e) {
            System.err.println("Error reading input: " + e.getMessage());
            return "";
        }
    }

    public int getInt() {
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid number.\n> ");
            scanner.next();
        }
        return scanner.nextInt();
    }

    public long getLong() {
        while (!scanner.hasNextLong()) {
            System.out.print("Invalid number.\n> ");
            scanner.next();
        }
        return scanner.nextLong();
    }

    public short getShort() {
        while (!scanner.hasNextShort()) {
            System.out.print("Invalid number.\n> ");
            scanner.next();
        }
        return scanner.nextShort();
    }

    public int getUserOption(int low, int high) {
        int userOpt = getInt();
        while (userOpt > high || userOpt < low) {
            System.out.print("Invalid option.\n> ");
            userOpt = getInt();
        }
        return userOpt;
    }

    public void displayHelp() {
        System.out.println(
"+========================================================================================+\n" + 
"|                             HELP - CSV Person Data Manager                             |\n" +
"+========================================================================================+\n" +
"| This program allows you to manage and query a dataset of persons using CSV files.      |\n" +
"| Each person has the following fields: CPF, RG, Name, Birthdate, and City.              |\n" +
"| The expected CSV format is: CPF,RG,Name,Birthdate,City (no headers).                   |\n" +
"| Example: 012.345.678-90;9876543210;John Doe;23/12/1988;Westford.                       |\n" +
"+----------------------------------------------------------------------------------------+\n" +
"| OPTIONS:                                                                               |\n" +
"| [1] Consult by.           - Access submenu to search by CPF, Name, or Birthdate range. |\n" +
"| [2] Load.                 - Access submenu to load from a file or directory.           |\n" +
"| [3] Update or add.        - Updates or adds a new person manually.                     |\n" +
"| [4] Save to file.         - Saves the current dataset to a CSV file.                   |\n" +
"| [5] Delete.               - Access submenu to delete a person by CPF or everything.    |\n" +
"| [6] Print.                - Access submenu to print size, person, or tree details.     |\n" +
"| [7] Help.                 - Display this help message.                                 |\n" +
"| [8] Clear console.        - Clears the screen.                                         |\n" +
"| [9] Exit.                 - Exit the program.                                          |\n" +
"| [10] Verbose mode.        - Will print CPFs that were updated when loading a file.     |\n" +
"+----------------------------------------------------------------------------------------+\n" +
"| INPUT FORMATS:                                                                         |\n" +
"| CPF                       - Only numeric or in the format ###.###.###-##.              |\n" +
"| RG                        - Only numeric.                                              |\n" +
"| Name                      - Alphanumeric, case-insensitive.                            |\n" +
"| Birthdate                 - Format: DD/MM/YYYY.                                        |\n" +
"+========================================================================================+\n" +
"|                                  Program by Jean Rehr                                  |\n" +
"+========================================================================================+"
        );
    }
}