package inwestorzy;
import akcje.Akcja;
import giełda.Giełda;

import java.util.List;

public class InwestorSMA extends Inwestor {

    @Override
    public void podejmijDecyzję() {
        //wcześniej brak możliwości obliczenia SMA10. uwzględniamy cenę z danych do SMA
        if (Giełda.getAktualnyNumerTury() >= 9) {
            List<Akcja> akcje = getPortfel().weźSpermutowanąListęAkcji();
            for (Akcja akcja : akcje) {
                //szukamy akcji do kupienia/sprzedaży w losowej kolejności
                if (akcja.getSygnałKupna()) {
                    //kupno
                    int ostatniaCena = akcja.getOstatniaCena();
                    int liczbaAkcji = getPortfel().getBudżet() / ostatniaCena;
                    if (liczbaAkcji > 0) {
                        //składa natychmiastowe, bo nie było sprecyzowane w treści jaki typ zleceń ma składać,
                        //a zlecenia natychmiastowe najbardziej pasują do tego typu strategii inwestowania
                        super.złóżOfertęKupnaNatychmiastowego(akcja, liczbaAkcji, ostatniaCena);
                        return;
                    }
                } else if (akcja.getSygnałSprzedaży()) {
                    //sprzedaż
                    int liczbaAkcji = getPortfel().getLiczbaAkcji(akcja);
                    if (liczbaAkcji > 0) {
                        super.złóżOfertęSprzedażyNatychmiastowej(akcja, liczbaAkcji, akcja.getOstatniaCena());
                        return;
                    }
                }
                //if brak sygnału to sprawdzamy kolejną akcję z listy akcji
            }
        }
    }
}
