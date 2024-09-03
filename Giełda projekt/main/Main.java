package main;
import giełda.Giełda;

public class Main {
    public static void main(String[] args) {
        //wywołujemy symulację na argumentach z wiersza poleceń
        assert args.length==2 : "Zła liczba argumentów. Oczekiwane: 2. Dostarczone: " + args.length;
        String nazwaPliku = args[0];
        int liczbaRund = Integer.parseInt(args[1]);
        Giełda giełda = new Giełda();
        giełda.czytajInput(nazwaPliku, liczbaRund);
        giełda.przeprowadźSymulację();
        giełda.drukujStanKońcowy();
    }
}