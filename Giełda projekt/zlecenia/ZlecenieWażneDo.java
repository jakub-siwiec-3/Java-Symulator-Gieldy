package zlecenia;

import akcje.Akcja;
import giełda.Giełda;
import inwestorzy.Inwestor;

public class ZlecenieWażneDo extends Zlecenie{
    private int rundaWygaśnięcia;
    @Override
    public boolean czyWygasło() {
        //wygasło gdy zostało zrealizowane lub minęła tura wygaśnięcia
        return Giełda.getAktualnyNumerTury()>=rundaWygaśnięcia || this.getliczbaAkcji()==0;
    }

    public ZlecenieWażneDo(Akcja akcja, int liczbaAkcji, Inwestor inwestor, int rundaWygaśnięcia, int cena){
        super(akcja, liczbaAkcji, inwestor, cena);
        this.rundaWygaśnięcia=rundaWygaśnięcia;
    }

}
