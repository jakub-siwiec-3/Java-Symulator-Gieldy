package portfel;
import akcje.Akcja;

import java.util.*;

public class Portfel {
    //zbiór akcji i pieniędzy Inwestora
    private int budżet;
    private Map<Akcja, Integer> portfolio;

    public void printPortfolio() {
        System.out.print(budżet);
        for (Map.Entry<Akcja, Integer> entry : portfolio.entrySet()) {
            Akcja akcja = entry.getKey();
            int ilość = entry.getValue();
            System.out.print(" " + akcja.getNazwaAkcji() + ": " + ilość);
        }
        System.out.print("\n");

    }

    public int getLiczbaAkcji(Akcja akcja) {
        return portfolio.get(akcja);
    }

    //konstruktor
    public Portfel(Akcja[] akcje, int[] liczbaAkcji, int budźet) {
        this.budżet = budźet;
        this.portfolio = new TreeMap<>();
        for (int i = 0; i < akcje.length; i++) {
            portfolio.put(akcje[i], liczbaAkcji[i]);
        }
    }

    public void usuńAkcje(Akcja akcja, int liczbaAkcji) {
        portfolio.put(akcja, portfolio.get(akcja) - liczbaAkcji);
    }

    public int getBudżet() {
        return budżet;
    }

    public void dodajAkcje(Akcja akcja, int liczbaAkcji) {
        portfolio.put(akcja, portfolio.get(akcja) + liczbaAkcji);
    }

    public List<Akcja> weźSpermutowanąListęAkcji() {
        //losuje permutację
        List<Akcja> akcje = new ArrayList<>(portfolio.keySet());
        Collections.shuffle(akcje);
        return akcje;
    }

    public Map<Akcja, Integer> weźKopięPortfolio() {
        //kopiuje portfolio; metoda przydatna do badania możliwości realizacji zlecenia kupna
        return new TreeMap<>(this.portfolio);
    }

    public Portfel(Map<Akcja, Integer> portfolio) {
        this.portfolio = portfolio;
    }

    //obsługa przelewów gotówkowych
    public void przelewPrzychodzący(int kwota) {
        budżet += kwota;
    }

    public void przelewWychodzący(int kwota) {
        budżet -= kwota;
    }

}
