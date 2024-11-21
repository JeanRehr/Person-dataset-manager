package gb;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

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
        /*
        int indexDel = cpfTree.getKeyByValue(cpf);
        if (indexDel == -1) {
            System.out.println("CPF doesn't exist.");
            return;
        }

        // remove from the arraylist
        Person personDel = persons.remove(indexDel);

        // remove from the cpfset to allow it to be added again
        cpfSet.remove(cpf);
        
        LocalDate birthDateDel = LocalDate.parse(personDel.getBirthDate(), dateFormat);
        long birthDateMillisDel = birthDateDel
            .atStartOfDay(ZoneId.of("UTC"))
            .toInstant()
            .toEpochMilli();

        cpfTree.remove(new Pair<>(indexDel, cpf));
        nameTree.remove(new Pair<>(indexDel, personDel.getName()));
        birthDateTree.remove(new Pair<>(indexDel, birthDateMillisDel));

        // update the key on the trees starting from the index deleted
        int personSize = persons.size();
        for (int i = indexDel; i < personSize; i++) {
            Person updatedPerson = persons.get(i);
            cpfTree.updateKeyOfValue(i, parseStrCpfToLong(updatedPerson.getCpf()));
            nameTree.updateKeyOfValue(i, updatedPerson.getName());
            
            long updatedBirthDateMillis = 
                LocalDate.parse(updatedPerson.getBirthDate(), dateFormat)
                .atStartOfDay(ZoneId.of("UTC"))
                .toInstant()
                .toEpochMilli();

            birthDateTree.updateKeyOfValue(i, updatedBirthDateMillis);
        }
        */

        /*
        int indexDel = cpfTree.getKeyByValue(cpf);

        if (indexDel == -1) {
            System.out.println("CPF doesn't exist.");
            return;
        }

        Person personDel = persons.remove(indexDel);
        */

        Person personDel = getPersonByCpf(persons, cpfTree, cpf);

        if (personDel == null) {
            System.out.println("CPF doesn't exist.");
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
            System.out.println("No Person found with given CPF.");
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

    public static long parseStrCpfToLongSimple(String cpf) throws NumberFormatException {
        return Long.parseLong(cpf);
    }

    public static long parseStrRgToLong(String rg) throws NumberFormatException {
        return Long.parseLong(rg);
    }

    public static long parseStrCpfToLong(String cpf) throws NumberFormatException {
        return Long.parseLong(cpf.replaceAll("[^\\d]", ""));
    }

}
