package zlecenia;

import akcje.Akcja;
import inwestorzy.Inwestor;

public class ZlecenieNatychmiastowe extends Zlecenie{
    @Override
    public boolean czyWygasło() {
        //pytanie czy wygasło zadawane pod koniec tury zawsze otrzyma twierdzącą odpowiedź
        return true;
    }

    public ZlecenieNatychmiastowe(Akcja akcja, int liczbaAkcji, Inwestor inwestor, int cena){
        super(akcja, liczbaAkcji, inwestor, cena);
    }
}
