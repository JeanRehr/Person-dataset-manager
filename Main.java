import java.util.Scanner;
import java.io.FileReader;
import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

/*
 * Load file of people's data, put them in an array/list
 * create a pair tree for each data (cpf, nome, dataNasc), key being the index of the array where object.cpf is
 */

public class Main {
    public static void main(String[] args) {
        // load csv file first, create an array of objects
        // cpf, rg, nome, datanasc, cidade
        ArrayList<Pessoa> pessoas = new ArrayList<Pessoa>();
        Scanner scanner = new Scanner(System.in);
        AVLTreeGeneric<Pair<Integer, Long>> cpfTree = new AVLTreeGeneric<>();
        AVLTreeGeneric<Pair<Integer, String>> nomeTree = new AVLTreeGeneric<>();
        AVLTreeGeneric<Pair<Integer, String>> nascTree = new AVLTreeGeneric<>();

        /*
        try {
            int i = 0;
            Set<Long> cpfSet = new HashSet<>();
            String line = "";
            BufferedReader reader = new BufferedReader(new FileReader("test1.csv"));
            while((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                
                long cpf = Long.parseLong(values[0]);

                if (!cpfSet.contains(cpf)) {
                    cpfSet.add(cpf);

                    Pair<Integer, Long> cpfPair = new Pair<>(i, cpf);
                    cpfTree.insert(cpfPair);

                    long rg = Long.parseLong(values[1]);
                    
                    String nome = values[2];
                    Pair<Integer, String> nomePair = new Pair<>(i, nome);
                    nomeTree.insert(nomePair);

                    String nasc = values[3];
                    Pair<Integer, String> nascPair = new Pair<>(i, nasc);
                    nascTree.insert(nascPair);

                    String cidade = values[4];

                    Pessoa pessoa = new Pessoa(cpf, rg, nome, nasc, cidade);
                    pessoas.add(pessoa);
                    i++;
                } else {
                    System.out.println("CPF already exists on CSV.");
                }
            }
            reader.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        */
        loadCSV(cpfTree, nomeTree, nascTree, pessoas);

        // testing
        for (Pessoa pessoa : pessoas) {
            System.out.println(pessoa);
        }

        System.out.println("***CPF TREE***");
        cpfTree.printTree(cpfTree.getRoot());

        System.out.println("***NOME TREE***");
        nomeTree.printTree(nomeTree.getRoot());

        System.out.println("***NASCIMENTO TREE***");
        nascTree.printTree(nascTree.getRoot());

        short userOpt = 100;
        Text text = new Text();
        int index = -1;

        while (userOpt != 6) {
            text.options();

            userOpt = text.getUserOption((short) 1, (short) 7);

            switch (userOpt) {
            case 1: // Consultar CPF
                System.out.print("Enter CPF to search> ");
                long cpf = text.getLong();

                index = cpfTree.getKeyBasedOnValue(cpf);

                if (index == -1) {
                    System.out.print("CPF não existe.");
                    break;
                }

                System.out.println("Details:\n" + pessoas.get(index));

                break;
            case 2: // Consultar Nome
                System.out.print("Enter name to search> ");
                String nome = scanner.nextLine();

                index = nomeTree.getKeyBasedOnValue(nome);

                if (index == -1) {
                    System.out.print("Nome não existe.");
                    break;
                }

                System.out.println("Details:\n" + pessoas.get(index));
                break;
            case 3: // Consultar Data de Nascimento
                break;
            case 4: // Carregar arquivo
                loadCSV(cpfTree, nomeTree, nascTree, pessoas);
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

    public static void loadCSV(
        AVLTreeGeneric<Pair<Integer, Long>> cpfTree,
        AVLTreeGeneric<Pair<Integer, String>> nomeTree,
        AVLTreeGeneric<Pair<Integer, String>> nascTree,
        ArrayList<Pessoa> pessoas
    ) {
        try {
            int i = 0;
            Set<Long> cpfSet = new HashSet<>();
            String line = "";
            BufferedReader reader = new BufferedReader(new FileReader("test1.csv"));
            while((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                
                long cpf = Long.parseLong(values[0]);

                if (!cpfSet.contains(cpf)) {
                    cpfSet.add(cpf);

                    Pair<Integer, Long> cpfPair = new Pair<>(i, cpf);
                    cpfTree.insert(cpfPair);

                    long rg = Long.parseLong(values[1]);
                    
                    String nome = values[2];
                    Pair<Integer, String> nomePair = new Pair<>(i, nome);
                    nomeTree.insert(nomePair);

                    String nasc = values[3];
                    Pair<Integer, String> nascPair = new Pair<>(i, nasc);
                    nascTree.insert(nascPair);

                    String cidade = values[4];

                    Pessoa pessoa = new Pessoa(cpf, rg, nome, nasc, cidade);
                    pessoas.add(pessoa);
                    i++;
                } else {
                    System.out.println("CPF already exists on CSV.");
                }
            }
            reader.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
