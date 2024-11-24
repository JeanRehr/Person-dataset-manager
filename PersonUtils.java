package gb;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PersonUtils {
    public static void addNewPerson(
        AVLTreeGeneric<Pair<Integer, Long>> cpfTree,
        AVLTreeGeneric<Pair<Integer, String>> nameTree,
        AVLTreeGeneric<Pair<Integer, Long>> birthDateTree,
        List<Person> persons,
        Set<Long> cpfSet,
        String cpfString,
        long cpfLong,
        String rg,
        String name,
        String birthDate,
        long birthDateMillis,
        String city,
        int index
    ) {
        Pair<Integer, Long> cpfPair = new Pair<>(index, cpfLong);
        cpfTree.insert(cpfPair);

        Pair<Integer, String> namePair = new Pair<>(index, name);
        nameTree.insertDupAllow(namePair);

        Pair<Integer, Long> birthDatePair = new Pair<>(index, birthDateMillis);
        birthDateTree.insertDupAllow(birthDatePair);

        Person person = new Person(cpfString, rg, name, birthDate, city);
        persons.add(person);

        cpfSet.add(cpfLong);
    }

    public static void deletePersonByCpf(
        AVLTreeGeneric<Pair<Integer, Long>> cpfTree,
        AVLTreeGeneric<Pair<Integer, String>> nameTree,
        AVLTreeGeneric<Pair<Integer, Long>> birthDateTree,
        List<Person> persons,
        Set<Long> cpfSet,
        DateTimeFormatter dateFormat,
        long cpf
    ) {
        Person personDel = getPersonByCpf(persons, cpfTree, cpf);

        if (personDel == null) {
            return;
        }

        persons.remove(personDel);

        cpfSet.remove(cpf);

        // delete everything from the tree, as once something is deleted in the arraylist
        // all the key values on the trees are wrong
        cpfTree.massRemove(cpfTree.getRoot().data);
        nameTree.massRemove(nameTree.getRoot().data);
        birthDateTree.massRemove(birthDateTree.getRoot().data);

        int personsSize = persons.size();
        // insert everything again
        for (int i = 0; i < personsSize; i++) {
            cpfTree.insert(new Pair<>(i, parseStrCpfToLong(persons.get(i).getCpf())));
    
            nameTree.insertDupAllow(new Pair<>(i, persons.get(i).getName()));

            String birthDate = persons.get(i).getBirthDate();

            LocalDate birthDatePerson = DateUtils.parseDateInput(birthDate, dateFormat);

            long birthDateMillis = DateUtils.convertDateToUnixEpoch(birthDatePerson, "UTC");

            birthDateTree.insertDupAllow(new Pair<>(i, birthDateMillis));
        }
    }

    public static void updatePerson(
        AVLTreeGeneric<Pair<Integer, String>> nameTree,
        AVLTreeGeneric<Pair<Integer, Long>> birthDateTree,
        List<Person> persons,
        int index,
        String newRg,
        String newName,
        String newBirthDate,
        String newCity,
        DateTimeFormatter dateFormat
    ) {
        if (index < 0 || index >= persons.size()) {
            System.out.println("Invalid index.");
            return;
        }

        Person person = persons.get(index);
        String oldName = person.getName();
        String oldBirthDate = person.getBirthDate();

        person.setRg(newRg);
        person.setName(newName);
        person.setBirthDate(newBirthDate);
        person.setCity(newCity);

        // Update name tree if the name has changed
        if (!newName.equals(oldName)) {
            nameTree.remove(new Pair<>(index, oldName));
            nameTree.insertDupAllow(new Pair<>(index, newName));
        }

        // Update birthDate tree if the birthdate has changed
        if (!newBirthDate.equals(oldBirthDate)) {
            LocalDate oldBirthDateObj = DateUtils.parseDateInput(oldBirthDate, dateFormat);
            long oldBirthDateMillis = DateUtils.convertDateToUnixEpoch(oldBirthDateObj, "UTC");

            birthDateTree.remove(new Pair<>(index, oldBirthDateMillis));

            LocalDate newBirthDateObj = DateUtils.parseDateInput(newBirthDate, dateFormat);
            long newBirthDateMillis = DateUtils.convertDateToUnixEpoch(newBirthDateObj, "UTC");

            birthDateTree.insertDupAllow(new Pair<>(index, newBirthDateMillis));
        }
    }

    public static Person getPersonByCpf(
        List<Person> persons,
        AVLTreeGeneric<Pair<Integer, Long>> cpfTree,
        long cpf
    ) {
        int index = cpfTree.getKeyByValue(cpf);
        if (index == -1) {
            return null;
        }
        return persons.get(index);
    }

    public static List<Person> getPersonsByName(
        List<Person> persons,
        AVLTreeGeneric<Pair<Integer, String>> nameTree,
        String prefix
    ) {
        List<String> names = nameTree.prefixMatchPair(prefix);

        Set<Integer> indexes = new HashSet<>(); // HashSet to not allow for duplicate keys

        for (String name : names) {
            indexes.addAll(nameTree.getKeyByValueDup(name));
        }

        List<Person> foundPersons = new ArrayList<>();

        for (int index : indexes) {
            foundPersons.add(persons.get(index));
        }
        return foundPersons;
    }

    public static List<Person> getPersonsByBirthRange(
        List<Person> persons,
        AVLTreeGeneric<Pair<Integer, Long>> birthDateTree,
        long low, 
        long high
        ) {
        Set<Integer> indexes = new HashSet<>();

        indexes = birthDateTree.getKeyOfAllLongsBetween(low, high);

        List<Person> foundPersons = new ArrayList<>();

        for (int index : indexes) {
            foundPersons.add(persons.get(index));
        }

        return foundPersons;
    }

    public static void printPersonDetails(Person person) {
        if (person == null) {
            System.out.println("No details to print.");
            return;
        }
        System.out.println(person.toString());
    }

    public static long parseStrRgToLong(String rg) {
        return Long.parseLong(rg);
    }

    public static boolean isCpfExpectedFormat(String cpf) throws IllegalArgumentException {
        String regexCpfFormatFull = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}";
        String regexCpfFormatNum = "\\d{11}";

        Pattern patternFormatFull = Pattern.compile(regexCpfFormatFull);
        Pattern patternFormatNum = Pattern.compile(regexCpfFormatNum);

        Matcher matcherFormatFull = patternFormatFull.matcher(cpf);
        Matcher matcherFormatNum = patternFormatNum.matcher(cpf);

        if (!matcherFormatFull.matches() && !matcherFormatNum.matches()) {
            throw new IllegalArgumentException("CPF not in the expected format.");
        }

        return true;
    }

    public static long parseStrCpfToLong(String cpf) throws IllegalArgumentException {
        if (!isCpfExpectedFormat(cpf)) {
            throw new IllegalArgumentException("CPF not in the expected format.");
        }

        return Long.parseLong(convertCpfFullToString(cpf));
    }

    public static String parseStrCpfToFull(String cpf) throws IllegalArgumentException {
        if (!isCpfExpectedFormat(cpf)) {
            throw new IllegalArgumentException("CPF not in the expected format.");
        }

        // remove any dots and dash
        String cpfFull = convertCpfFullToString(cpf);

        // insert dots and dash again to guarantee that they have it correctly
        cpfFull = 
        cpfFull.substring(0, 3) + "." + 
        cpfFull.substring(3, 6) + "." + 
        cpfFull.substring(6, 9) + "-" + 
        cpfFull.substring(9, 11);

        return cpfFull;
    }

    private static String convertCpfFullToString(String cpfFull) throws IllegalArgumentException {
        return cpfFull.replace(".", "").replace("-", "");
    }
}