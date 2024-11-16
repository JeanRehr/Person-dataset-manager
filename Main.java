import java.util.Scanner;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.ZoneOffset;
import java.time.ZoneId;

/*
 * Load file of people's data, put them in an array/list
 * create a pair tree for each data (cpf, name, birthdate)
 * key being the index of the array where a particular object is
 */

public class Main {
    public static void main(String[] args) {
        // load csv file first, create an array of objects
        // cpf, rg, name, birthdate, city
        Scanner scanner = new Scanner(System.in);

        ArrayList<Person> persons = new ArrayList<Person>();
        Set<Long> cpfSet = new HashSet<>(); // Keep track of CPFs already added on the program
        AVLTreeGeneric<Pair<Integer, Long>> cpfTree = new AVLTreeGeneric<>();
        AVLTreeGeneric<Pair<Integer, String>> nameTree = new AVLTreeGeneric<>();
        AVLTreeGeneric<Pair<Integer, Long>> birthDateTree = new AVLTreeGeneric<>();

        // Defining date format here as it is always the same throughout the program
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        loadCSV(cpfTree, nameTree, birthDateTree, persons, cpfSet, scanner);

        // testing
        //for (Person person : persons) {
        //    System.out.println(person);
        //}

        short userOpt = 100;

        ConsoleHandler consoleHandler = new ConsoleHandler(scanner);
        while (userOpt != 9) {
            int index = -1;
            consoleHandler.options();

            System.out.print("Option> ");
            userOpt = consoleHandler.getUserOption((short) 1, (short) 9);

            switch (userOpt) {
            case 1: // Consultar CPF
                System.out.print("Enter CPF to search> ");
                long cpfSearch = consoleHandler.getLong();

                index = cpfTree.getKeyByValue(cpfSearch);

                if (index == -1) {
                    System.out.println("Non-existent CPF.");
                    break;
                }

                System.out.println("Details:\n" + persons.get(index));

                break;
            case 2: // Consultar Nome
                System.out.print("Enter name to search> ");
                String nameSearch = scanner.nextLine();

                ArrayList<String> nodeValues = new ArrayList<>();

                nodeValues = nameTree.prefixMatchPair(nameSearch);
                //System.out.println(nodeValues);

                // HashSet to not allow for duplicate key values
                Set<Integer> indexesName = new HashSet<>();

                for (String nodeValue : nodeValues) {
                    Set<Integer> keys = nameTree.getKeyByValueDup(nodeValue);
                    indexesName.addAll(keys);
                }

                if (indexesName.size() == 0) {
                    System.out.println("No person with this name.");
                    break;
                }

                //System.out.println(indexesName);

                System.out.println("Details:");
                for (int key : indexesName) {
                    //System.out.println(key);
                    System.out.println(persons.get(key) + "\n");
                }
                
                /*
                System.out.println("Details:");
                for (String values : nodeValues) {
                    index = nameTree.getKeyByValueDup(values);
                    System.out.println("INDEX = " + index);
                    if (index == -1) {
                        System.out.println("Name non-existent");
                        break;
                    } 
                    System.out.println(persons.get(index) + "\n");

                }
                */
                
                //nameTree.printTree(stringNode);

                //index = nameTree.getKeyByValue(name);

                //if (index == -1) {
                //    System.out.print("Non-existent name.");
                //    break;
                //}

                //System.out.println("Details:\n" + persons.get(index));
                break;
            case 3: // Consultar Data de Nascimento                
                System.out.print("Date of birth to search from> ");
                String input = null;
                LocalDate firstDateObj = getInputDate(input, dateFormat, scanner);

                System.out.print("Date of birth to search to> ");
                LocalDate lastDateObj = getInputDate(input, dateFormat, scanner);
                
                long firstDateInputLong = firstDateObj
                    .atStartOfDay(ZoneId.of("UTC"))
                    .toInstant()
                    .toEpochMilli();

                long lastDateInputLong = lastDateObj
                    .atStartOfDay(ZoneId.of("UTC"))
                    .toInstant()
                    .toEpochMilli();

                //System.out.println(firstDateInputLong + "\n" + lastDateInputLong);

                //Set<Integer> indexesBirth = birthDateTree.getKeyOfAllLongsBetween(firstDateInputLong, firstDateInputLong);
                
                Set<Integer> indexesBirth = 
                    birthDateTree.getKeyOfAllLongsBetween(firstDateInputLong, lastDateInputLong);

                if (indexesBirth.size() == 0) {
                    System.out.println("No birth dates between these two dates.");
                    break;
                }

                System.out.println("Details:");
                for (int key : indexesBirth) {
                    //System.out.println(key);
                    System.out.println(persons.get(key) + "\n");
                }

                break;
            case 4: // Carregar arquivo
                loadCSV(cpfTree, nameTree, birthDateTree, persons, cpfSet, scanner);
                break;
            case 5: // Delete
                long cpfDel = 0;
                String cpfStringDel = null;
                while (cpfStringDel == null) {
                    System.out.print("CPF to delete> ");
                    cpfStringDel = scanner.nextLine();
                    try {
                        cpfDel = Long.parseLong(cpfStringDel);
                    } catch (Exception e) {
                        cpfStringDel = null;
                        System.out.println("Only numbers for CPF.");
                    }
                }

                long startTime = System.nanoTime();

                int indexDel = -1;
                int currentPos = 0;

                String stringBirthDateDel = null;
                String nameDel = null;

                // find and store the variables to delete
                for (Person person : persons) {
                    if (cpfStringDel.equals(person.getCpf())) {
                        indexDel = currentPos;
                        stringBirthDateDel = person.getBirthDate();
                        nameDel = person.getName();
                        break;
                    }
                    currentPos++;
                }

                if (indexDel == -1) {
                    System.out.println("CPF doesn't exist.");
                    break;
                }

                // delete
                persons.remove(indexDel);

                LocalDate birthDateDel = parseDateInput(stringBirthDateDel, dateFormat);

                long birthDateUnixEpochDel = birthDateDel
                    .atStartOfDay(ZoneId.of("UTC"))
                    .toInstant()
                    .toEpochMilli();

                // Delete the vars birthDateUnixEpochDel, cpfDel, nameDel from the trees
                // with key indexDel
                Pair<Integer, Long> cpfPairDel = new Pair<>(indexDel, cpfDel);
                cpfTree.remove(cpfPairDel);

                Pair<Integer, String> namePairDel = new Pair<>(indexDel, nameDel);
                nameTree.remove(namePairDel);

                Pair<Integer, Long> birthDatePairDel = new Pair<>(indexDel, birthDateUnixEpochDel);
                birthDateTree.remove(birthDatePairDel);

                // Once the key/value is removed from the tree, we update the key on the rest of
                // the key value pair on the trees based on the index of the arraylist.
                
                currentPos = 0;
                for (Person person : persons) {
                    //System.out.println("INDEX: " + currentPos + " CPF: " + person.getCpf());
                    cpfTree.updateKeyOfValue(currentPos, Long.parseLong(person.getCpf()));
                    nameTree.updateKeyOfValue(currentPos, person.getName());
                    String birthDateUpd = person.getBirthDate();

                    LocalDate birthDatePersonUpd = parseDateInput(birthDateUpd, dateFormat);

                    long birthDateUnixEpochUpd = birthDatePersonUpd
                        .atStartOfDay(ZoneId.of("UTC"))
                        .toInstant()
                        .toEpochMilli();
                    
                    birthDateTree.updateKeyOfValue(currentPos, birthDateUnixEpochUpd);
                    currentPos++;
                }
                
                /*
                long cpfDel = 0;
                String cpfStringDel = null;
                while (cpfStringDel == null) {
                    System.out.print("CPF to delete> ");
                    cpfStringDel = scanner.nextLine();
                    try {
                        cpfDel = Long.parseLong(cpfStringDel);
                    } catch (Exception e) {
                        cpfStringDel = null;
                        System.out.println("Only numbers for CPF.");
                    }
                }

                long startTime = System.nanoTime();

                int indexDel = -1;
                int currentPos = 0;

                for (Person person : persons) {
                    if (cpfStringDel.equals(person.getCpf())) {
                        indexDel = currentPos;
                        break;
                    }
                    currentPos++;
                }

                if (indexDel == -1) {
                    System.out.println("CPF doesn't exist.");
                    break;
                }

                // delete
                persons.remove(indexDel);

                // delete everything from the tree, as once something is deleted in the arraylist
                // all the key values on the trees are wrong
                cpfTree.massRemove(cpfTree.getRoot().data);
                nameTree.massRemove(nameTree.getRoot().data);
                birthDateTree.massRemove(birthDateTree.getRoot().data);

                // insert everything again
                currentPos = 0;
                for (Person person : persons) {
                    long cpfIns = Long.parseLong(person.getCpf());
                    Pair<Integer, Long> cpfPair = new Pair<>(currentPos, cpfIns);
                    cpfTree.insert(cpfPair);
            
                    String nameIns = person.getName();
                    Pair<Integer, String> namePair = new Pair<>(currentPos, nameIns);
                    nameTree.insertDupAllow(namePair);

                    String birthDate = person.getBirthDate();

                    LocalDate birthDatePerson = parseDateInput(birthDate, dateFormat);

                    long birthDateUnixEpoch = birthDatePerson
                        .atStartOfDay(ZoneId.of("UTC"))
                        .toInstant()
                        .toEpochMilli();

                    Pair<Integer, Long> birthDatePair = new Pair<>(currentPos, birthDateUnixEpoch);
                    birthDateTree.insertDupAllow(birthDatePair);
                    currentPos++;
                }
                */

                long endTime = System.nanoTime();
                System.out.println("Duration: " + ((endTime - startTime) / 1000000) + "ms");
                break;
            case 6: // Help
                System.out.println(
                    "This program loads CSVs of data that has information about (no headers): \n" +
                    "CPF,RG,Name,Birthdade,City.\n" +
                    "Example: 01234567890,9876543210,Name Test,23/12/1988,Westford.\n" +
                    "And does a fast search by CPF, Name prefixes, and range of birthdates."
                );
                break;
            case 7: // Clear screen
                consoleHandler.clearConsole();
                break;
            case 8: // Print tree
                System.out.print(
                    "Print:\n" +
                    "1 - CPF tree.\n" +
                    "2 - Name tree.\n" +
                    "3 - Birthdate tree.\n" +
                    "4 - Person ArrayList.\n"
                );

                System.out.print("Option> ");
                short printWhichTree = consoleHandler.getUserOption((short) 1, (short) 4);

                if (printWhichTree == 1) {
                    cpfTree.printTree(cpfTree.getRoot());
                } else if (printWhichTree == 2) {
                    nameTree.printTree(nameTree.getRoot());
                } else if (printWhichTree == 3) {
                    birthDateTree.printTree(birthDateTree.getRoot());
                } else if (printWhichTree == 4) {
                    int sizeOfPersons = persons.size();
                    for (int i = 0; i < sizeOfPersons; i++) {
                        System.out.println("INDEX: " + i);
                        System.out.println(persons.get(i));
                    }
                }

                break;
            case 9: // Exit
                break;
            }
        }
        scanner.close();
    }

    public static void loadCSV(
        AVLTreeGeneric<Pair<Integer, Long>> cpfTree,
        AVLTreeGeneric<Pair<Integer, String>> nameTree,
        AVLTreeGeneric<Pair<Integer, Long>> birthDateTree,
        ArrayList<Person> persons,
        Set<Long> cpfSet,
        Scanner scanner
    ) {
        try {
            System.out.print("CSV file name> ");
            String filePath = scanner.nextLine();
            BufferedReader reader = new BufferedReader(new FileReader(filePath));

            System.out.print("CSV value separator> ");
            String separator = scanner.nextLine();

            String line = "";
            
            /* 
             * when adding more CSVs, the indices that were in the list needs
             * to be i plus the size of the arraylist getting passed on the function
             * example: first csv loaded has 5 lines, 0-4, that correctly gets added to the trees
             * and arraylist, calling this function again for another csv, i will be equal 0 again
             * and add that as the key on the trees, however, the arraylist will have another
             * index number for that new person, as it gets added to the last position on the list
             */
            int offset = persons.size();
            int i = 0;
            while((line = reader.readLine()) != null) {
                String[] values = line.split(separator);

                int index = offset + i;
    
                long cpf = Long.parseLong(values[0]);
                String cpfString = values[0];

                if (cpfSet.contains(cpf)) {
                    System.out.println("***CPF: " + cpf + " already exists on CSV, skipping.***");
                    continue;
                }

                cpfSet.add(cpf); // To not allow same CPFs, but allow same names/birthdates

                Pair<Integer, Long> cpfPair = new Pair<>(index, cpf);
                cpfTree.insert(cpfPair);

                long rg = Long.parseLong(values[1]);
                String rgString = values[1];
        
                String name = values[2];
                Pair<Integer, String> namePair = new Pair<>(index, name);
                nameTree.insertDupAllow(namePair);

                String birthDate = values[3];

                DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate birthDatePerson = parseDateInput(birthDate, dateFormat);
                
                // Birthdates are transformed to unix epoch longs, to be easier
                // to compare and balance
                long birthDateUnixEpoch = birthDatePerson
                    .atStartOfDay(ZoneId.of("UTC"))
                    .toInstant()
                    .toEpochMilli();

                Pair<Integer, Long> birthDatePair = new Pair<>(index, birthDateUnixEpoch);
                birthDateTree.insertDupAllow(birthDatePair);

                String city = values[4];

                Person person = new Person(cpfString, rgString, name, birthDate, city);
                persons.add(person);
                i++;
            }
            reader.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static LocalDate getInputDate(String input, DateTimeFormatter dateFormat, Scanner scanner) {
        LocalDate dateObj = null;

        while (dateObj == null) {
            input = scanner.nextLine();
            dateObj = parseDateInput(input, dateFormat);
            if (dateObj == null) {
                System.out.print("Date> ");
            }
        }

        return dateObj;
    }
    
    // Returns null if input is in the wrong format
    public static LocalDate parseDateInput(String input, DateTimeFormatter dateFormat) {
        LocalDate dateObj = null;

        try {
            dateObj = LocalDate.parse(input, dateFormat);
        } catch (DateTimeParseException e) {
            System.out.println("Wrong format. Correct format is: " + dateFormat.toString());
        }

        return dateObj;
    }
}
