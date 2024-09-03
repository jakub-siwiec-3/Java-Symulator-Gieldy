package akcje;
import giełda.Giełda;

import java.util.LinkedList;

public class Akcja implements Comparable<Akcja> {
    //klasa reprezentuje akcje jednej spółki. w tym rozumieniu Akcja - klasa abstrakcji obiektów Zlecenie, które operują
    //na akcjach tej samej spółki
    private String nazwaAkcji;
    //cena ostatniej transakcji
    private int ostatniaCena;
    //do wyliczania SMA używamy LinkedListy którą cyklicznie przesuwamy o 1 co turę
    private double sma5;
    private LinkedList<Integer> SMA10;
    //aktualizowany przy dodaniu nowego zlecenia z Akcją tego typu. Pozwala ocenić, które zlecenie jest starsze
    private int numerZlecenia;
    private double sma10;
    private boolean sygnałKupna;
    private boolean sygnałSprzedaży;
    public Akcja(String nazwaAkcji, int ostatniaCena){
        if (nazwaAkcji == null || nazwaAkcji.isEmpty() || nazwaAkcji.length() > 5) {
            throw new IllegalArgumentException("Nazwa akcji: " + nazwaAkcji + " nieprawidłowej długości");
        }
        if (ostatniaCena <= 0) {
            throw new IllegalArgumentException("Niepoprawna cena: " + ostatniaCena + " akcji " + nazwaAkcji);
        }
        this.nazwaAkcji=nazwaAkcji;
        this.ostatniaCena=ostatniaCena;
        SMA10=new LinkedList<>();
        SMA10.add(ostatniaCena);
        sma5=-1;
        sma10=-1;
        numerZlecenia=0;
        sygnałKupna=false;
        sygnałSprzedaży=false;
    }
    public int getNumerZlecenia() {
        return numerZlecenia;
    }
    public void zwiększNumerZlecenia(){
        numerZlecenia++;
    }

    public String getNazwaAkcji(){
        return nazwaAkcji;
    }

    //stan portfela na koniec ma być wypisywany leksykograficznie, stąd ta metoda
    @Override
    public int compareTo(Akcja o) {
        return this.nazwaAkcji.compareTo(o.nazwaAkcji);
    }

    public int getOstatniaCena(){
        return ostatniaCena;
    }
    //liczy SMA
    public void policzSMA(){
        double suma10 = 0;
        double suma5 = 0;
        int k=0;
        for(Integer wartość : SMA10){
            suma10+=wartość;
            if(k>=5){
                suma5+=wartość;
            }
            k++;
        }
        sma5=suma5/5;
        sma10=suma10/10;
    }
    //pod koniec tury aktualizujemy SMA
    public void zaktualizujSMA(){
        if(Giełda.getAktualnyNumerTury()<8){
            SMA10.add(ostatniaCena);
        } else {
            //w turze 0 dodajemy wartość z pliku, a pod koniec 0 i każdej kolejnej rundy dodajemy wartość ostatniej transakcji
            if(Giełda.getAktualnyNumerTury()==8){
                SMA10.add(ostatniaCena);
                assert SMA10.size()==10 : "SMA10 liczone dla złej liczby cen";
                policzSMA();
            } else{
                //sma już zostały kiedyś policzone, sprawdzamy czy wykresy się przecinają
                double stareSMA5 = sma5;
                double stareSMA10 = sma10;
                SMA10.removeFirst();
                SMA10.add(ostatniaCena);
                policzSMA();
                //jeśli mamy przecięcie to zmieniamy sygnał
                if(stareSMA10>stareSMA5 && sma5>sma10){
                    sygnałKupna=true;
                    sygnałSprzedaży=false;
                } else if(stareSMA10<stareSMA5 && sma5< sma10){
                    sygnałSprzedaży=true;
                    sygnałKupna=false;
                } else{
                    sygnałKupna=false;
                    sygnałSprzedaży=false;
                }
            }

        }
    }

    public boolean getSygnałKupna() {
        return sygnałKupna;
    }

    public boolean getSygnałSprzedaży() {
        return sygnałSprzedaży;
    }

    public void setOstatniaCena(int ostatniaCena) {
        this.ostatniaCena = ostatniaCena;
    }
}
