package gb;

/*
 * Program made by Jean Rehr
 */

import java.util.Scanner;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.ZoneId;

import static gb.Constants.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Person> persons = new ArrayList<>();
        Set<Long> cpfSet = new HashSet<>(); // Keep track of CPFs already added on the program
        AVLTreeGeneric<Pair<Integer, Long>> cpfTree = new AVLTreeGeneric<>();
        AVLTreeGeneric<Pair<Integer, String>> nameTree = new AVLTreeGeneric<>();
        AVLTreeGeneric<Pair<Integer, Long>> birthDateTree = new AVLTreeGeneric<>();

        ConsoleHandler consoleHandler = new ConsoleHandler(scanner);

        String[] mainMenuItems = {
            "Consult by.",
            "Load.",
            "Update or add.",
            "Save to csv file.",
            "Delete.",
            "Print.",
            "Help.",
            "Clear console.",
            "Exit.",
            "Verbose mode."
        };

        while (true) {
            consoleHandler.displayMenu("Main Menu", mainMenuItems);
            System.out.print("Option> ");
            int userOpt = consoleHandler.getUserOption(0, mainMenuItems.length);
            consoleHandler.getNextLine(); // Consuming \n
            if (userOpt == 9) {
                break;
            }
            handleMainMenuOption(
                userOpt,
                cpfTree,
                nameTree,
                birthDateTree,
                persons,
                cpfSet,
                consoleHandler,
                DATE_FORMAT
            );
        }
        scanner.close();
    }

    private static void handleMainMenuOption(
        int userOpt,
        AVLTreeGeneric<Pair<Integer, Long>> cpfTree,
        AVLTreeGeneric<Pair<Integer, String>> nameTree,
        AVLTreeGeneric<Pair<Integer, Long>> birthDateTree,
        List<Person> persons,
        Set<Long> cpfSet,
        ConsoleHandler consoleHandler, 
        DateTimeFormatter dateFormat
    ) {
        switch (userOpt) {
        case 1: // Consult
            handleConsultMenuOptions(consoleHandler, cpfTree, nameTree, birthDateTree, persons, dateFormat);
            break;
        case 2: // Load
            handleLoadMenuOptions(consoleHandler, cpfTree, nameTree, birthDateTree, persons, cpfSet, dateFormat);
            break;
        case 3: // Add or Update Person
            inputAddOrUpdPerson(cpfTree, nameTree, birthDateTree, persons, cpfSet, dateFormat, consoleHandler);
            break;
        case 4: // Save csv
            saveToFileInput(persons, consoleHandler);
            break;
        case 5: // Delete
            handleDeleteMenuOptions(consoleHandler, cpfTree, nameTree, birthDateTree, persons, cpfSet, dateFormat);
            break;
        case 6: // Print
            handlePrintMenuOptions(consoleHandler, cpfTree, nameTree, birthDateTree, persons, cpfSet);
            break;
        case 7: // Help
            consoleHandler.displayHelp();
            break;
        case 8: // Clear console
            consoleHandler.clearConsole();
            break;
        case 9: // Exit
            break;
        case 10: // Enable vebose
            verbose = !verbose;
            if (verbose) {
                System.out.println("Verbose mode: " + GREEN + verbose + RESET);
                break;
            }
            System.out.println("Verbose mode: " + RED + verbose + RESET);
            break;
        };
    }
    
    private static void handleConsultMenuOptions(
        ConsoleHandler consoleHandler,
        AVLTreeGeneric<Pair<Integer, Long>> cpfTree,
        AVLTreeGeneric<Pair<Integer, String>> nameTree,
        AVLTreeGeneric<Pair<Integer, Long>> birthDateTree,
        List<Person> persons,
        DateTimeFormatter dateFormat
    ) {
        if (persons.isEmpty()) {
            System.out.println(RED + "Program is empty." + RESET);
            return;
        }

        String[] consultMenuItems = {
            "Consult by CPF.",
            "Consult by name.",
            "Consult by birthdate range.",
            "Cancel."
        };

        consoleHandler.displayMenu("Consult Menu", consultMenuItems);
        System.out.print("Option> ");
        int userOpt = consoleHandler.getUserOption(1, consultMenuItems.length);
        consoleHandler.getNextLine(); // Consuming \n

        switch (userOpt) {
        case 1:
            consultCpf(cpfTree, persons, consoleHandler);
            break;
        case 2:
            consultName(nameTree, persons, consoleHandler);
            break;
        case 3:
            consultBirthdate(birthDateTree, persons, dateFormat, consoleHandler);
            break;
        case 4:
            break;
        }
    }

    private static void handleLoadMenuOptions(
        ConsoleHandler consoleHandler,
        AVLTreeGeneric<Pair<Integer, Long>> cpfTree,
        AVLTreeGeneric<Pair<Integer, String>> nameTree,
        AVLTreeGeneric<Pair<Integer, Long>> birthDateTree,
        List<Person> persons,
        Set<Long> cpfSet,
        DateTimeFormatter dateFormat
    ) {
        String[] loadMenuItems = {
            "Load a file name.",
            "Load a directory.",
            "Cancel."
        };

        consoleHandler.displayMenu("Load Menu", loadMenuItems);
        System.out.print("Option> ");
        int userOpt = consoleHandler.getUserOption(1, loadMenuItems.length);
        consoleHandler.getNextLine(); // Consuming \n

        switch (userOpt) {
        case 1:
            loadCsvFile(
                cpfTree,
                nameTree,
                birthDateTree,
                persons,
                cpfSet,
                dateFormat,
                consoleHandler
            );
            break;
        case 2:
            loadFilesFromDirectory(
                cpfTree,
                nameTree,
                birthDateTree,
                persons,
                cpfSet,
                dateFormat,
                consoleHandler
            );
            break;
        case 3:
            break;
        }
    }

    private static void handleDeleteMenuOptions(
        ConsoleHandler consoleHandler,
        AVLTreeGeneric<Pair<Integer, Long>> cpfTree,
        AVLTreeGeneric<Pair<Integer, String>> nameTree,
        AVLTreeGeneric<Pair<Integer, Long>> birthDateTree,
        List<Person> persons,
        Set<Long> cpfSet,
        DateTimeFormatter dateFormat
    ) {
        if (persons.isEmpty()) {
            System.out.println(RED + "Program is empty." + RESET);
            return;
        }

        String[] deletePersonMenuItems = {
            "Delete person by CPF.",
            "Delete everything.",
            "Cancel."
        };

        consoleHandler.displayMenu("Delete Menu", deletePersonMenuItems);
        System.out.print("Option> ");
        int userOpt = consoleHandler.getUserOption(1, deletePersonMenuItems.length);
        consoleHandler.getNextLine(); // Consuming \n

        switch (userOpt) {
        case 1:
            deletePerson(cpfTree, nameTree, birthDateTree, persons, cpfSet, dateFormat, consoleHandler);
            break;
        case 2:
            deleteEverything(cpfTree, nameTree, birthDateTree, persons, cpfSet, consoleHandler);
            break;
        case 3:
            break;
        }
    }

    private static void handlePrintMenuOptions(
        ConsoleHandler consoleHandler,
        AVLTreeGeneric<Pair<Integer, Long>> cpfTree,
        AVLTreeGeneric<Pair<Integer, String>> nameTree,
        AVLTreeGeneric<Pair<Integer, Long>> birthDateTree,
        List<Person> persons,
        Set<Long> cpfSet
    ) {
        if (persons.isEmpty()) {
            System.out.println(RED + "Program is empty." + RESET);
            return;
        }

        String[] printMenuItems = {
            "Size info.",
            "Person ArrayList.",
            "CPF tree.",
            "Name tree.",
            "Birthdate tree.",
            "Cancel."
        };

        consoleHandler.displayMenu("Print Menu", printMenuItems);
        System.out.print("Option> ");
        int userOpt = consoleHandler.getUserOption(1, printMenuItems.length);
        consoleHandler.getNextLine(); // Consuming \n

        switch (userOpt) {
        case 1:
            System.out.println(
                "Arraylist size: " + persons.size() + "\n" +
                "CPF Set size: " + cpfSet.size() + "\n" +
                "CPF total nodes: " + cpfTree.getTotalNodes(cpfTree.getRoot()) + "\n" +
                "Name total nodes: " + nameTree.getTotalNodes(nameTree.getRoot()) + "\n" +
                "Birthdate total nodes: " + birthDateTree.getTotalNodes(birthDateTree.getRoot())
            );
            break;
        case 2:
            int sizeOfPersons = persons.size();
            for (int i = 0; i < sizeOfPersons; i++) {
                System.out.println("INDEX: " + i + "\n" + persons.get(i) + "\n");
            }
            System.out.println("Number of persons: " + sizeOfPersons);
            break;
        case 3:
            cpfTree.printTree(cpfTree.getRoot());
            System.out.println("Nodes: " + cpfTree.getTotalNodes(cpfTree.getRoot()));
            break;
        case 4:
            nameTree.printTree(nameTree.getRoot());
            System.out.println("Nodes: " + nameTree.getTotalNodes(nameTree.getRoot()));
            break;
        case 5:
            birthDateTree.printTree(birthDateTree.getRoot());
            System.out.println("Nodes: " + birthDateTree.getTotalNodes(birthDateTree.getRoot()));
            break;
        case 6:
            break;
        }
    }

    private static void consultCpf(
        AVLTreeGeneric<Pair<Integer, Long>> cpfTree,
        List<Person> persons,
        ConsoleHandler consoleHandler
    ) {
        if (persons.isEmpty()) {
            System.out.println(RED + "Nothing is loaded." + RESET);
            return;
        }

        long cpfSearch = -1;
        System.out.print("Enter CPF to search> ");
        try {
            cpfSearch = PersonUtils.parseStrCpfToLong(consoleHandler.getNextLine());
        } catch (IllegalArgumentException iae) {
            System.out.println(
                RED + "CPF does not match any of the accepted formats.\n" + RESET +
                "Use only 11 numbers or ###.###.###-##."
            );
            return;
        }

        Person personConsult = PersonUtils.getPersonByCpf(persons, cpfTree, cpfSearch);

        if (personConsult == null) {
            System.out.println(RED + "CPF doesn't exist." + RESET);
            return;
        }

        System.out.println("Details:");
        PersonUtils.printPersonDetails(personConsult);
    }

    private static void consultName(
        AVLTreeGeneric<Pair<Integer, String>> nameTree,
        List<Person> persons,
        ConsoleHandler consoleHandler
    ) {
        if (persons.isEmpty()) {
            System.out.println(RED + "Nothing is loaded." + RESET);
            return;
        }

        System.out.print("Enter name to search> ");
        String nameSearch = consoleHandler.getNextLine();

        List<Person> foundPersons = PersonUtils.getPersonsByName(persons, nameTree, nameSearch);  ;
    
        if (foundPersons.isEmpty()) {
            System.out.println(RED + "No persons found with given prefix." + RESET);
            return;
        }

        System.out.println("Details:");
        for (Person person : foundPersons) {
            PersonUtils.printPersonDetails(person);
            System.out.println();
        }
    }

    private static void consultBirthdate(
        AVLTreeGeneric<Pair<Integer, Long>> birthDateTree,
        List<Person> persons,
        DateTimeFormatter dateFormat,
        ConsoleHandler consoleHandler
    ) {
        if (persons.isEmpty()) {
            System.out.println(RED + "Nothing is loaded." + RESET);
            return;
        }

        LocalDate firstDateObj = null;
        LocalDate lastDateObj = null;

        try {
            System.out.print("Search birth date from: ");
            firstDateObj = DateUtils.parseDateInput(consoleHandler.getNextLine(), dateFormat);
            System.out.print("To: ");
            lastDateObj = DateUtils.parseDateInput(consoleHandler.getNextLine(), dateFormat);
        } catch (DateTimeParseException dtpe) {
            System.out.println(
                RED + "Wrong format.\n" + RESET +
                "Correct format is: " + DATE_PATTERN +
                RED + "\nAborting." + RESET
            );
            return;
        }

        long firstDateInput = DateUtils.convertDateToUnixEpoch(firstDateObj, "UTC");
        long lastDateInput = DateUtils.convertDateToUnixEpoch(lastDateObj, "UTC");

        if (firstDateInput > lastDateInput) {
            long temp = firstDateInput;
            firstDateInput = lastDateInput;
            lastDateInput = temp;
        }

        List<Person> foundPersons = PersonUtils.getPersonsByBirthRange(persons, birthDateTree, firstDateInput, lastDateInput);

        if (foundPersons.isEmpty()) {
            System.out.println(RED + "No birthdates between " + firstDateObj + " and " + lastDateObj + RESET);
            return;
        }

        System.out.println("Details:");
        for (Person person : foundPersons) {
            PersonUtils.printPersonDetails(person);
            System.out.println();
        }
    }

    private static void parseCsv(
        AVLTreeGeneric<Pair<Integer, Long>> cpfTree,
        AVLTreeGeneric<Pair<Integer, String>> nameTree,
        AVLTreeGeneric<Pair<Integer, Long>> birthDateTree,
        List<Person> persons,
        Set<Long> cpfSet,
        DateTimeFormatter dateFormat,
        String filePath,
        String sep
    ) {
        int i = 0;
        // Skip an added line when a same cpf is in the csv, as it is updated instead of a new person
        int skips = 0;
        List<Integer> updatedIndex = new ArrayList<>();
        // offset when adding to the list when it already has people
        int offset = persons.size();
        boolean updatePerson = false;
        boolean dataValid = true;

        List<Person> tempPersons = new ArrayList<>();
        Set<Long> tempCpfSet = new HashSet<>();

        AVLTreeGeneric<Pair<Integer, Long>> tempCpfTree = new AVLTreeGeneric<>();
        AVLTreeGeneric<Pair<Integer, String>> tempNameTree = new AVLTreeGeneric<>();
        AVLTreeGeneric<Pair<Integer, Long>> tempBirthDateTree = new AVLTreeGeneric<>();

        for (Pair<Integer, Long> pair : cpfTree.inOrderTraversal()) {
            tempCpfTree.insert(pair);
        }
        for (Pair<Integer, String> pair : nameTree.inOrderTraversal()) {
            tempNameTree.insertDupAllow(pair);
        }
        for (Pair<Integer, Long> pair : birthDateTree.inOrderTraversal()) {
            tempBirthDateTree.insertDupAllow(pair);
        }

        tempPersons.addAll(persons);
        tempCpfSet.addAll(cpfSet);

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while((line = reader.readLine()) != null) {
                String[] values = line.split(sep);

                int index = offset + i;
                i++;

                String cpfString = PersonUtils.parseStrCpfToFull(values[0]);
                long cpfLong = PersonUtils.parseStrCpfToLong(cpfString);

                if (tempCpfSet.contains(cpfLong)) {
                    // update existing person
                    skips++;
                    updatePerson = true;
                }

                // test if rg is indeed a number
                PersonUtils.parseStrRgToLong(values[1]);

                String birthDate = values[3];

                LocalDate birthDatePerson = DateUtils.parseDateInput(birthDate, dateFormat);
                long birthDateMillis = DateUtils.convertDateToUnixEpoch(birthDatePerson, "UTC");

                if (updatePerson) {
                    Person existingPerson = PersonUtils.getPersonByCpf(tempPersons, tempCpfTree, cpfLong);
                    if (existingPerson != null) {
                        int existingIndex = tempPersons.indexOf(existingPerson);
                        PersonUtils.updatePerson(tempNameTree, tempBirthDateTree, tempPersons, existingIndex, values[1], values[2], birthDate, values[4], dateFormat);
                        updatedIndex.add(existingIndex);
                        continue;
                    }
                }
                PersonUtils.addNewPerson(tempCpfTree, tempNameTree, tempBirthDateTree, tempPersons, tempCpfSet, cpfString, cpfLong, values[1], values[2], birthDate, birthDateMillis, values[4], index);
            }
            // Commit all changes if no errors occurred
            if (dataValid) {
                persons.clear();
                persons.addAll(tempPersons);
                cpfSet.clear();
                cpfSet.addAll(tempCpfSet);
    
                // Clear and rebuild original trees from temp trees
                if (cpfTree.getRoot() != null) {
                    cpfTree.massRemove(cpfTree.getRoot().data);
                    nameTree.massRemove(nameTree.getRoot().data);
                    birthDateTree.massRemove(birthDateTree.getRoot().data);
                }

                for (Pair<Integer, Long> pair : tempCpfTree.inOrderTraversal()) {
                    cpfTree.insert(pair);
                }
                for (Pair<Integer, String> pair : tempNameTree.inOrderTraversal()) {
                    nameTree.insertDupAllow(pair);
                }
                for (Pair<Integer, Long> pair : tempBirthDateTree.inOrderTraversal()) {
                    birthDateTree.insertDupAllow(pair);
                }
            }
        } catch (FileNotFoundException fnfe) {
            System.out.println(RED + "*** " + filePath + ": No such file or directory. ***" + RESET);
            dataValid = false;
        } catch (NumberFormatException nfe) {
            System.out.println(
                RED + "*** RG on line " + i + " is not in the correct format. ***\n" + RESET +
                "Use only numbers for RG."
            );
            dataValid = false;
        } catch (IllegalArgumentException iae) {
            System.out.println(
                RED + "*** CPF on line " + i + " does not match any of the accepted formats. ***\n" + RESET +
                "Use 11 numbers or format ###.###.###-##, or check the separator used."
            );
            dataValid = false;
        } catch (DateTimeParseException dtpe) {
            System.out.println(
                RED + "*** Fault in birthdate parsing. ***\n" + RESET +
                "Check file " + filePath + " for incorrect birthdates on line " + i + ".\n" +
                "Correct format is: " + DATE_PATTERN + ".\n"
            );
            dataValid = false;
        } catch(Exception e) {
            e.printStackTrace();
            dataValid = false;
        } finally {
            if (i == 0 || !dataValid) {
                System.out.println(RED + "No lines were loaded from file: " + filePath + "." + RESET);
            } else {
                System.out.println(GREEN + "Loaded " + (i - skips) + " lines " + RESET + "from file: " + filePath + " from a total of " + i + " lines");
            }
            if (!updatedIndex.isEmpty()) {
                System.out.println(YELLOW + updatedIndex.size() + " persons were updated." + RESET);
                if (verbose) {
                    System.out.println("Updated CPFs:");
                    for (int indexUpd : updatedIndex) {
                        System.out.println(persons.get(indexUpd).getCpf());
                    }
                }
            }
        }
    }

    private static void parseDir(
        AVLTreeGeneric<Pair<Integer, Long>> cpfTree,
        AVLTreeGeneric<Pair<Integer, String>> nameTree,
        AVLTreeGeneric<Pair<Integer, Long>> birthDateTree,
        List<Person> persons,
        Set<Long> cpfSet,
        DateTimeFormatter dateFormat,
        String dirPath,
        String sep
    ) {
        File path = new File(dirPath);
        File[] files = path.listFiles();

        if (files == null) {
            System.out.println(RED + "*** " + dirPath + ": No such file or directory. ***" + RESET);
            return;
        }

        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile() && files[i].getName().toLowerCase().endsWith(".csv")) {
                System.out.println(GREEN + "Loading persons from file " + files[i].getAbsolutePath() + "." + RESET + "\n...");
                parseCsv(
                    cpfTree,
                    nameTree,
                    birthDateTree,
                    persons,
                    cpfSet,
                    dateFormat,
                    files[i].getAbsolutePath(),
                    sep
                );
            }
        }
    }

    private static void loadCsvFile(
        AVLTreeGeneric<Pair<Integer, Long>> cpfTree,
        AVLTreeGeneric<Pair<Integer, String>> nameTree,
        AVLTreeGeneric<Pair<Integer, Long>> birthDateTree,
        List<Person> persons,
        Set<Long> cpfSet,
        DateTimeFormatter dateFormat,
        ConsoleHandler consoleHandler
    ) {
        System.out.print("CSV file name> ");
        String filePath = consoleHandler.getNextLine();
        System.out.print("CSV value separator> ");
        String sep = consoleHandler.getNextLine();

        parseCsv(cpfTree, nameTree, birthDateTree, persons, cpfSet, dateFormat, filePath, sep);
    }

    private static void loadFilesFromDirectory(
        AVLTreeGeneric<Pair<Integer, Long>> cpfTree,
        AVLTreeGeneric<Pair<Integer, String>> nameTree,
        AVLTreeGeneric<Pair<Integer, Long>> birthDateTree,
        List<Person> persons,
        Set<Long> cpfSet,
        DateTimeFormatter dateFormat,
        ConsoleHandler consoleHandler
    ) {
        System.out.print("Directory where CSVs are located> ");
        String dirPath = consoleHandler.getNextLine();
        System.out.print("CSVs value separator> ");
        String sep = consoleHandler.getNextLine();

        parseDir(cpfTree, nameTree, birthDateTree, persons, cpfSet, dateFormat, dirPath, sep);
    }

    private static void inputAddOrUpdPerson(
        AVLTreeGeneric<Pair<Integer, Long>> cpfTree,
        AVLTreeGeneric<Pair<Integer, String>> nameTree,
        AVLTreeGeneric<Pair<Integer, Long>> birthDateTree,
        List<Person> persons,
        Set<Long> cpfSet,
        DateTimeFormatter dateFormat,
        ConsoleHandler consoleHandler
    ) {
        System.out.println("Enter no value to cancel anytime.");
        System.out.println("Enter an existent CPF to update a person.");
        System.out.print("CPF> ");
        String cpfString = consoleHandler.getNextLine().trim();

        if (cpfString.isEmpty()) {
            System.out.println(RED + "Cancelling." + RESET);
            return;
        }

        long cpfLong;

        try {
            cpfString = PersonUtils.parseStrCpfToFull(cpfString);
            cpfLong = PersonUtils.parseStrCpfToLong(cpfString);
        } catch (IllegalArgumentException iae) {
            System.out.println(
                RED + "CPF does not match any of the accepted formats.\n" + RESET +
                "Use only 11 numbers or ###.###.###-##."
            );
            return;
        }

        boolean updatePerson = false;
        int index = cpfTree.getKeyByValue(cpfLong);
        if (index != -1) {
            System.out.println(
                RED + "CPF already exists.\n" + RESET +
                "Person has the following values:\n\n"  +
                persons.get(index) + "\n\nEnter the values to" +
                GREEN + " update" + RESET + ":"
            );
            updatePerson = true;
        } else {
            System.out.println("CPF" + RED + " not found." + RESET + "\nAdding person.");
        }

        System.out.print("RG> ");
        String rgString = consoleHandler.getNextLine().trim();;

        if (rgString.isEmpty()) {
            System.out.println(RED + "Cancelling." + RESET);
            return;
        }

        // test if rg is indeed a number
        try {
            PersonUtils.parseStrRgToLong(rgString);
        } catch (NumberFormatException nfe) {
            System.out.println(RED + "*** Wrong format for RG, use only numbers. ***" + RESET);
            return;
        }

        System.out.print("Name> ");
        String name = consoleHandler.getNextLine().trim();

        if (name.isEmpty()) {
            System.out.println(RED + "Cancelling." + RESET);
            return;
        }

        System.out.print("Birthdate> ");

        String birthDateStr = consoleHandler.getNextLine().trim();;

        if (birthDateStr.isEmpty()) {
            System.out.println(RED + "Cancelling." + RESET);
            return;
        }

        LocalDate birthDateObj = null;
        try {
            birthDateObj = DateUtils.parseDateInput(birthDateStr, dateFormat);
        } catch (DateTimeParseException dtpe) {
            System.out.println(
                RED + "*** Incorrect birthdate format. ***\n" + RESET +
                "Correct format is: " + DATE_PATTERN + RED + "\nAborting." + RESET
            );
            return;
        }

        long birthDateMillis = DateUtils.convertDateToUnixEpoch(birthDateObj, "UTC");

        System.out.print("City> ");
        String city = consoleHandler.getNextLine().trim();;

        if (city.isEmpty()) {
            System.out.println(RED + "Cancelling." + RESET);
            return;
        }

        if (updatePerson) {
            PersonUtils.updatePerson(
                nameTree,
                birthDateTree,
                persons,
                index,
                rgString,
                name,
                birthDateStr,
                city,
                dateFormat
            );
            System.out.println(GREEN + "Update successful." + RESET);
            return;
        }
        PersonUtils.addNewPerson(
            cpfTree,
            nameTree,
            birthDateTree,
            persons,
            cpfSet,
            cpfString,
            cpfLong,
            rgString,
            name,
            birthDateStr,
            birthDateMillis,
            city,
            persons.size()
        );
        System.out.println(GREEN + "Add successful." + RESET);
    }

    private static void saveToFileInput(List<Person> persons, ConsoleHandler consoleHandler) {
        if (persons.isEmpty()) {
            System.out.println(RED + "Nothing is loaded." + RESET);
            return;
        }

        System.out.print("Directory to save> ");
        String dir = consoleHandler.getNextLine();
        System.out.print("File name to save> ");
        String fileName = consoleHandler.getNextLine();
        System.out.print("Separator> ");
        String sep = consoleHandler.getNextLine();

        File dirPath = new File(dir);
        if (!dirPath.exists()) {
            if (dirPath.mkdirs()) {
                System.out.println(GREEN + "Directory created." + RESET);
            } else {
                System.out.println(RED + "Failed to create directory." + RESET);
                return;
            }
        }

        File file = new File(dirPath, fileName);

        if(file.exists()) {
            System.out.println(
                RED + "File exists in the directory.\n" + RESET +
                "Do you want to overwrite it?"
            );
            System.out.print("[Y]es.\n[N]o.\nOption> ");
            String delAll = consoleHandler.getNextLine();
            if (Character.toLowerCase(delAll.charAt(0)) != 'y') {
                System.out.println(RED + "Aborting." + RESET);
                return;
            }
        }
        saveToFile(persons, file.getAbsolutePath(), sep);
    }

    private static void saveToFile(List<Person> persons, String fileName, String sep) {
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName + ".csv"))) {
            for (Person person : persons) {
                writer.write(
                    person.getCpf() + sep +
                    person.getRg() + sep +
                    person.getName() + sep +
                    person.getBirthDate() + sep +
                    person.getCity() + "\n"
                );
            }
            System.out.println(GREEN + "File saved successfully." + RESET);
        } catch (IOException ioe) {
            System.out.println(RED + "*** Could not save file. ***" + RESET);
            return;
        }
    }

    private static void deletePerson(
        AVLTreeGeneric<Pair<Integer, Long>> cpfTree,
        AVLTreeGeneric<Pair<Integer, String>> nameTree,
        AVLTreeGeneric<Pair<Integer, Long>> birthDateTree,
        List<Person> persons,
        Set<Long> cpfSet,
        DateTimeFormatter dateFormat,
        ConsoleHandler consoleHandler
    ) {
        if (persons.isEmpty()) {
            System.out.println(RED + "There is nothing to delete." + RESET);
            return;
        }
        
        String cpfStringDel = null;
        System.out.print("CPF to delete (Enter [c] to [C]ancel)> ");
        cpfStringDel = consoleHandler.getNextLine().trim();

        if (Character.toLowerCase(cpfStringDel.charAt(0)) == 'c') {
            System.out.println(RED + "Cancelling." + RESET);
            return;
        }

        long cpfDel;
        try {
            cpfDel = PersonUtils.parseStrCpfToLong(cpfStringDel);
        } catch (IllegalArgumentException iae) {
            System.out.println(
                RED + "CPF does not match any of the accepted formats.\n" + RESET +
                "Use only 11 numbers or ###.###.###-##."
            );
            return;
        }

        Person personDel = PersonUtils.getPersonByCpf(persons, cpfTree, cpfDel);

        if (personDel == null) {
            System.out.println(RED + "CPF does not exist in the dataset." + RESET);
            return;
        }

        System.out.println("Deleting...");

        long startTime = System.nanoTime();

        PersonUtils.deletePersonByCpf(cpfTree, nameTree, birthDateTree, persons, cpfSet, dateFormat, cpfDel);

        long endTime = System.nanoTime();
        System.out.println(GREEN + "Deletion successful." + RESET);
        System.out.println("Duration: " + ((endTime - startTime) / 1000000) + "ms");
    }

    private static void deleteEverything(
        AVLTreeGeneric<Pair<Integer, Long>> cpfTree,
        AVLTreeGeneric<Pair<Integer, String>> nameTree,
        AVLTreeGeneric<Pair<Integer, Long>> birthDateTree,
        List<Person> persons,
        Set<Long> cpfSet,
        ConsoleHandler consoleHandler
    ) {
        if (persons.isEmpty()) {
            System.out.println(RED + "There is nothing to delete." + RESET);
            return;
        }

        System.out.print(
            "Proceed with " + RED + "deleting everything?\n" + RESET +
            "[Y]ES.\n" +
            "[N]O.\n" +
            "Option> "
        );
        String delAll = consoleHandler.getNextLine();
        if (Character.toLowerCase(delAll.charAt(0)) != 'y') {
            System.out.println(RED + "Aborting." + RESET);
            return;
        }

        persons.clear();
        cpfSet.clear();
        cpfTree.massRemove(cpfTree.getRoot().data);
        nameTree.massRemove(nameTree.getRoot().data);
        birthDateTree.massRemove(birthDateTree.getRoot().data);
        System.out.println(GREEN + "Deletion successful." + RESET);
    }

    private static void printTrees(
        AVLTreeGeneric<Pair<Integer, Long>> cpfTree,
        AVLTreeGeneric<Pair<Integer, String>> nameTree,
        AVLTreeGeneric<Pair<Integer, Long>> birthDateTree,
        List<Person> persons,
        Set<Long> cpfSet,
        ConsoleHandler consoleHandler
    ) {
        if (persons.isEmpty()) {

            System.out.println(RED + "There is nothing to print." + RESET);
            return;
        }

        System.out.print(
            "Print:\n" +
            "[1] Size info.\n" +
            "[2] Person ArrayList.\n" +
            "[3] CPF tree.\n" +
            "[4] Name tree.\n" +
            "[5] Birthdate tree.\n"
        );

        System.out.print("Option> ");
        int printWhichTree = consoleHandler.getUserOption(1, 6);
    }
}