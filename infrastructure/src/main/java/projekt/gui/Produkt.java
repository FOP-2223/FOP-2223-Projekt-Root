package projekt.gui;

public class Produkt {

    int nummer;
    String name;
    double preis;


    public Produkt(int nummer, String name, double preis) {
        this.nummer = nummer;
        this.name = name;
        this.preis = preis;
    }

    public int getNummer() {
        return nummer;
    }

    public String getName() {
        return name;
    }

    public double getPreis() {
        return preis;
    }
}
