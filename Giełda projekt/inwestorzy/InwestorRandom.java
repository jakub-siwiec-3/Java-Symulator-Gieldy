package inwestorzy;

import akcje.Akcja;
import giełda.Giełda;
import losowanie.Losowanie;

public class InwestorRandom extends Inwestor {

    //podejmuje losowe decyzje - kupno/sprzedaż, kwota, liczba akcji, rodzaj zlecenia
    @Override
    public void podejmijDecyzję() {
        int kupno;
        kupno = Losowanie.losuj(0, 1);
        Akcja losowaAkcja = Giełda.wylosujAkcję();
        int losowaLiczbaAkcji;
        if (kupno == 0) {
            //inwestor sprzedaje akcje
            int limitLiczbyAkcji = getPortfel().getLiczbaAkcji(losowaAkcja);
            if(limitLiczbyAkcji==0){
                return;
            }
            int cenaZlecenia = losujCenę(losowaAkcja);
            losowaLiczbaAkcji = Losowanie.losuj(1, limitLiczbyAkcji);
            super.złóżOfertęSprzedaży(losowaAkcja, losowaLiczbaAkcji, cenaZlecenia);
        } else {
            //inwestor kupuje akcje
            int cenaZlecenia = losujCenę(losowaAkcja);
            int limitLiczbyAkcji = getPortfel().getBudżet()/cenaZlecenia;
            if(limitLiczbyAkcji==0){
                return;
            }
            losowaLiczbaAkcji=Losowanie.losuj(1, limitLiczbyAkcji);
            super.złóżOfertęKupna(losowaAkcja, losowaLiczbaAkcji, cenaZlecenia);
        }
    }
    //losowanie ceny akcji na podstawie ostatniej ceny akcji
    public int losujCenę(Akcja losowaAkcja){
        int ostatniaCenaAkcji = losowaAkcja.getOstatniaCena();
        int cenaZlecenia;
        if(ostatniaCenaAkcji<=10){
            cenaZlecenia=Losowanie.losuj(1, ostatniaCenaAkcji+10);
        } else{
            cenaZlecenia=Losowanie.losuj(ostatniaCenaAkcji-10, ostatniaCenaAkcji+10);
        }
        return cenaZlecenia;
    }

}
