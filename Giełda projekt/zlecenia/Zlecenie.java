package zlecenia;

import akcje.Akcja;
import inwestorzy.Inwestor;

public abstract class Zlecenie {
    private Akcja akcja;
    private int liczbaAkcji;
    private Inwestor inwestor;
    private int cena;
    //dla każdej akcji oddzielnie numerowane zlecenia od 0. Pozwala ocenić które zlecenie jest wcześniejsze
    private int numerZlecenia;
    public int getNumerZlecenia() {
        return numerZlecenia;
    }
    //czy zlecenie powinno zostać usunięte z puli zleceń
    public abstract boolean czyWygasło();
    //odpowiedź zlecenia na pytanie, czy dany typ zlecenia powinien zostać zrealizowany przy danych warunkach transakcji
    public boolean czyRealizujemy(int możliwyRozmiarRealizacji, int rozmiarZlecenia) {
        return możliwyRozmiarRealizacji>0;
    }
    public Zlecenie(Akcja akcja, int liczbaAkcji, Inwestor inwestor, int cena){
        this.akcja=akcja;
        this.liczbaAkcji=liczbaAkcji;
        this.inwestor=inwestor;
        this.cena=cena;
        this.numerZlecenia=akcja.getNumerZlecenia();
        akcja.zwiększNumerZlecenia();
    }

    public int getCena() {
        return cena;
    }

    public Inwestor getInwestor() {
        return inwestor;
    }

    public int getliczbaAkcji() {
        return liczbaAkcji;
    }

    public Akcja getAkcja() {
        return akcja;
    }
    public void zmniejszLiczbęAkcji(int liczba){
        liczbaAkcji-=liczba;
    }
    public boolean czyWymagaSprawdzenia(){
        return false;
    }
}
