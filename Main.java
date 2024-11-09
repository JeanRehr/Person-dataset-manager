import java.util.Scanner;
import java.io.FileReader;
import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
 * Load file of people's data, put them in an array/list
 * create a pair tree on string data, key being the index of the array where object.cpf is
 */

public class Main {
    public static void main(String[] args) {
        // load csv file first
        // cpf, rg, nome, datanasc, cidade
        ArrayList<Pessoa> pessoa = new ArrayList<Pessoa>();
        /*
        for (CSVRecord record : records) {
            pessoa.add(new Pessoa(record.get(0), record.get(1), record.get(2), record.get(3), record.get(4));
        }
        */


        Text text = new Text();
        Scanner scanner = new Scanner(System.in);

        AVLTreePessoa<Integer> cpfTree = new AVLTreePessoa<>();
        //AVLTreePessoa<Map> nomeTree = new AVLTreePessoa<>();

        T userValue = null;
        short userOpt = 100;

        while (userOpt != 6) {
            text.options();

            userOpt = text.getUserOption((short) 1, (short) 7);

            switch (userOpt) {
            case 1: // Consultar CPF
                break;
            case 2: // Consultar Nome
                break;
            case 3: // Consultar Data de Nascimento
                break;
            case 4: // Carregar arquivo
                break;
            case 5: // Help
                break;
            case 6: // Exit
                break;
            case 7: // Clear screen
                text.clearConsole();
                break;
            }
        }
        scanner.close();
    }

    /*
    private static <T extends Comparable<T>> void interactWithTree(
        Scanner scanner,
        Text text
    ) {

    }
    */

    public static <T extends Comparable<T>> void massInsertNumbers(
        Text text,
        AVLTreePessoa<T> tree,
        Class<T> type
    ) {

        T valueToBeInserted = null;
        System.out.print("Start the insertion at what number> ");
        int startNumber = text.getInt();
        System.out.print("Max number to stop insertions> ");
        int endNumber = text.getInt();
        int intervals = 0;
        while (intervals <= 0) {
            System.out.print("Intervals in numbers of> ");
            intervals = text.getInt();
            if (intervals <= 0) {
                System.out.println("Intervals can't be <= 0.");
            }
        }
        System.out.println(
            "Inserting " + startNumber +
            " to " + endNumber +
            " in intervals of " + intervals + "."
        );
        for (int i = startNumber; i <= endNumber; i = i + intervals) {
            valueToBeInserted = text.parseValue(String.valueOf(i), type);
            tree.insert(valueToBeInserted);
        }
    }

    public static <T extends Comparable<T>> void massInsertChar(
        Text text,
        AVLTreePessoa<T> tree,
        Class<T> type
    ) {
        T valueToBeInserted = null;

        System.out.print(
                "Insert:\n" +
                        "1 - Uppercase.\n" +
                        "2 - Lowercase.\n");
        int opt = text.getUserOption((short) 1, (short) 2);
        if (opt == 1) {
            for (int asciiValue = 65; asciiValue < 91; asciiValue++) {
                char asciiChar = (char) asciiValue;
                valueToBeInserted = text.parseValue(String.valueOf(asciiChar), type);
                tree.insert(valueToBeInserted);
            }
        } else if (opt == 2) {
            for (int asciiValue = 97; asciiValue < 123; asciiValue++) {
                char asciiChar = (char) asciiValue;
                valueToBeInserted = text.parseValue(String.valueOf(asciiChar), type);
                tree.insert(valueToBeInserted);
            }
        }
    }

    public static <T extends Comparable<T>> void massInsertWords(
        Text text,
        AVLTreePessoa<T> tree,
        Class<T> type,
        Scanner scanner
    ) {
        if (!String.class.isAssignableFrom(type)) {
            System.out.println("Only String Trees are accepted for this operation.");
            return;
        }

        String path;

        System.out.println(
            "Load 274926 English words?\n" +
            "1 - Yes (this is not a fast operation).\n" +
            "2 - No (will load the words that starts with a and b).\n" +
            "3 - Load a file of words (must be separated by new lines).\n" +
            "4 - Cancel."
        );

        int opt = text.getUserOption((short) 1, (short) 4);

        if (opt == 1) {
            path = "all_words_less.txt";
        } else if (opt == 2) {
            path = "a_b_english.txt";
        } else if (opt == 3) {
            System.out.print("File name> ");
            path = scanner.nextLine();
        } else {
            return;
        }

        long startTime = System.nanoTime();

        try (Stream<String> lines = Files.lines(Paths.get(path))) {
            lines.forEach(word -> tree.insert((T) word));
        } catch (Exception e) {
            System.out.println("File not found or unable to read file.");
        }
        /*
         * try (BufferedReader bf = new BufferedReader(new FileReader("enw.txt"))) {
         * long startTime = System.nanoTime();
         * String word;
         * while ((word = bf.readLine()) != null) {
         * tree.insert((T) word);
         * }
         * } catch (Exception e) {
         * System.out.println("File not found.");
         * }
         */
        // unacceptable performance with scanner
        /*
         * try (Scanner sc = new Scanner(new File("words.txt"))) {
         * long startTime = System.nanoTime();
         * sc.useDelimiter("(\\n)");
         * while(sc.hasNext()) {
         * String word = sc.next();
         * if (word.length() > 0) {
         * tree.insert((T) word);
         * }
         * }
         * } catch (Exception e) {
         * System.out.println("File not found.");
         * }
         */
        long endTime = System.nanoTime();
        System.out.println("Duration: " + ((endTime - startTime) / 1000000) + "ms");
    }
}