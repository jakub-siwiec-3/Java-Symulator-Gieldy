package zlecenia;

import akcje.Akcja;
import inwestorzy.Inwestor;

public class ZlecenieWykonajLubAnuluj extends Zlecenie{
    @Override
    public boolean czyWygasło() {
        //wyga zawsze pod koniec tury
        return true;
    }
    @Override
    public boolean czyRealizujemy(int możliwyRozmiarRealizacji, int rozmiarZlecenia){
        //realizujemy tylko gdy można to zrobic w całości
        return rozmiarZlecenia==możliwyRozmiarRealizacji;
    }
    public ZlecenieWykonajLubAnuluj(Akcja akcja, int liczbaAkcji, Inwestor inwestor, int cena){
        super(akcja, liczbaAkcji, inwestor, cena);
    }
    @Override
    public boolean czyWymagaSprawdzenia(){
        return true;
    }
}
