import java.util.Scanner;

public class Text {
    Scanner scanner = new Scanner(System.in);

    public void clearConsole() {
        /* ANSI CODE */
        // System.out.println("\033[2J\033[;H"); /* Works but not always */
        final String ANSI_CLS = "\u001b[2J"; /* Clear screen. */
        final String ANSI_HOME = "\u001b[H"; /* Cursor to the top right. */
        System.out.print(ANSI_CLS + ANSI_HOME);
        System.out.flush();
    }

    public void options() {
        System.out.print(
            "-------------------------------------------------\n" +
            "Opções: [1] Consultar CPF.\n" +
            "        [2] Consultar Nome.\n" +
            "        [3] Consultar Data de Nascimento.\n" +
            "        [4] Carregar arquivo.\n" +
            "        [5] Help.\n" +
            "        [6] Exit.\n" +
            "        [7] Clear screen.\n" +
            "-------------------------------------------------\n"
        );
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

    // Tried a wrapper for nextLine, doesn't work
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

    public short getShort() {
        while (!scanner.hasNextShort()) {
            System.out.print("Invalid number.\n> ");
            scanner.next();
        }
        return scanner.nextShort();
    }

    public short getUserOption(short low, short high) {
        System.out.print("Option> ");
        short userOpt = getShort();
        while (userOpt > high || userOpt < low) {
            System.out.print("Invalid option.\n> ");
            userOpt = getShort();
        }
        return userOpt;
    }
}