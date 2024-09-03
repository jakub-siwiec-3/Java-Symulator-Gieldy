package zlecenia;

import akcje.Akcja;
import inwestorzy.Inwestor;

public class ZlecenieBezTerminu extends Zlecenie{
    @Override
    public boolean czyWygasło() {
        //usuwane tylko gdy wypełnione w całości
        return this.getliczbaAkcji()==0;
    }
    public ZlecenieBezTerminu(Akcja akcja, int liczbaAkcji, Inwestor inwestor, int cena){
        super(akcja, liczbaAkcji, inwestor, cena);
    }
}
