public class Pessoa {
    public long cpf;
    public long rg;
    public String nome;   
    public String nasc;
    public String cidade;

    public Pessoa(long cpf, long rg, String nome, String nasc, String cidade) {
        this.cpf = cpf;
        this.rg = rg;
        this.nome = nome;
        this.nasc = nasc;
        this.cidade = cidade;
    }

    public String toString() {
        return "CPF: " + cpf + "\nRG: " + rg +
        "\nNome: " + nome + "\nData de Nascimento: " + nasc + "\nCidade: " + cidade;
    }
}
