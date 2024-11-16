public class Person {
    private String cpf;
    private String rg;
    private String name;
    private String birthDate;
    private String city;

    public Person(String cpf, String rg, String name, String birthDate, String city) {
        this.cpf = cpf;
        this.rg = rg;
        this.name = name;
        this.birthDate = birthDate;
        this.city = city;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String toString() {
        return "CPF: " + cpf + "\nRG: " + rg +
        "\nNome: " + name + "\nData de Nascimento: " + birthDate + "\nCidade: " + city;
    }
}
