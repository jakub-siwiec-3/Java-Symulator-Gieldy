package inwestorzy;
import akcje.Akcja;
import losowanie.Losowanie;
import zlecenia.*;
import portfel.Portfel;
import giełda.Giełda;

public abstract class Inwestor {
    private Portfel portfel;

    public void setPortfel(Portfel portfel) {
        this.portfel = portfel;
    }

    public Portfel getPortfel() {
        return portfel;
    }
    public int spytajONumerTury(){
        return Giełda.getAktualnyNumerTury();
    }
    //rodzaj zlecenia losujemy, zgodnie z treścią zadania
    public void złóżOfertęKupna(Akcja akcja, int liczbaAkcji, int cena){
        int rodzajZlecenia = Losowanie.losuj(0,3);
        Zlecenie noweZlecenie;
        switch (rodzajZlecenia) {
            case 0:
                noweZlecenie = new ZlecenieNatychmiastowe(akcja, liczbaAkcji, this, cena);
                Giełda.dodajZlecenieKupna(noweZlecenie);
                break;
            case 1:
                noweZlecenie = new ZlecenieBezTerminu(akcja, liczbaAkcji, this, cena);
                Giełda.dodajZlecenieKupna(noweZlecenie);
                break;
            case 2:
                noweZlecenie = new ZlecenieWykonajLubAnuluj(akcja, liczbaAkcji, this, cena);
                Giełda.dodajZlecenieKupna(noweZlecenie);
                break;
            case 3:
                noweZlecenie = new ZlecenieWażneDo(akcja, liczbaAkcji, this,
                        Losowanie.losuj(spytajONumerTury(), Giełda.getLiczbaTur()), cena);
                Giełda.dodajZlecenieKupna(noweZlecenie);
                break;
        }
    }
    public void złóżOfertęSprzedaży(Akcja akcja, int liczbaAkcji, int cena){
        int rodzajZlecenia = Losowanie.losuj(0,3);
        Zlecenie noweZlecenie;
        switch (rodzajZlecenia) {
            case 0:
                noweZlecenie = new ZlecenieNatychmiastowe(akcja, liczbaAkcji, this, cena);
                Giełda.dodajZlecenieSprzedaży(noweZlecenie);
                break;
            case 1:
                noweZlecenie = new ZlecenieBezTerminu(akcja, liczbaAkcji, this, cena);
                Giełda.dodajZlecenieSprzedaży(noweZlecenie);
                break;
            case 2:
                noweZlecenie = new ZlecenieWykonajLubAnuluj(akcja, liczbaAkcji, this, cena);
                Giełda.dodajZlecenieSprzedaży(noweZlecenie);
                break;
            case 3:
                noweZlecenie = new ZlecenieWażneDo(akcja, liczbaAkcji, this,
                        Losowanie.losuj(spytajONumerTury(), Giełda.getLiczbaTur()), cena);
                Giełda.dodajZlecenieSprzedaży(noweZlecenie);
                break;
        }
    }
    //inwestor podejmuje decyzję czy składa jakieś zlecenie kupna/sprzedaży
    public abstract void podejmijDecyzję();

    protected void złóżOfertęKupnaNatychmiastowego(Akcja akcja, int liczbaAkcji, int cena) {
        Zlecenie noweZlecenie = new ZlecenieNatychmiastowe(akcja, liczbaAkcji, this, cena);
        Giełda.dodajZlecenieKupna(noweZlecenie);
    }

    protected void złóżOfertęSprzedażyNatychmiastowej(Akcja akcja, int liczbaAkcji, int cena) {
        Zlecenie noweZlecenie = new ZlecenieNatychmiastowe(akcja, liczbaAkcji, this, cena);
        Giełda.dodajZlecenieSprzedaży(noweZlecenie);
    }
}
