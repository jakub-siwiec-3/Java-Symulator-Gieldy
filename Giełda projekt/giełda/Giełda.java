package giełda;

import inwestorzy.Inwestor;
import inwestorzy.InwestorRandom;
import inwestorzy.InwestorSMA;
import losowanie.Losowanie;
import portfel.Portfel;
import zlecenia.Zlecenie;
import akcje.Akcja;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Giełda {
    //Giełda przechowuje informacje o zarejestrowanych inwestorach, akcjach w obiegu, numerze tury, a także listę ze
    //zleceniami kupna oraz sprzedaży. Tutaj przeprowadzamy symulację.
    private List<Inwestor> inwestorzy;
    private static Akcja[] akcje;
    private static int aktualnyNumerTury;
    private static int liczbaTur;
    private static ArrayList<Zlecenie> zleceniaKupna;
    private static ArrayList<Zlecenie> zleceniaSprzedaży;

    public static int getAktualnyNumerTury() {
        return aktualnyNumerTury;
    }

    public static int getLiczbaTur() {
        return liczbaTur;
    }

    //funkcja zamieniająca input z pliku na odpowiednie dane klasy;
    public void czytajInput(String nazwaPliku, int liczbaTur) {
        Giełda.liczbaTur = liczbaTur;
        int licznikZnaczącychWierszy = 0;
        try (Scanner scanner = new Scanner(new File(nazwaPliku))) {
            while (scanner.hasNextLine()) {
                String wiersz = scanner.nextLine();
                //ignorujemy wiersze od # się zaczynające
                if (!(wiersz.charAt(0) == '#')) {
                    String[] słowaWWierszu = wiersz.split("\\s+");
                    if (licznikZnaczącychWierszy == 0) {
                        Inwestor[] inwestorzy = new Inwestor[słowaWWierszu.length];
                        int j = 0;
                        for (String słowo : słowaWWierszu) {
                            if (słowo.equals("R")) {
                                inwestorzy[j] = new InwestorRandom();
                            } else if (słowo.equals("S")) {
                                inwestorzy[j] = new InwestorSMA();
                            } else {
                                //nie dopuszczamy innych typów inwestorów
                                throw new IllegalArgumentException("Nielegalny typ inwestora");
                            }
                            j++;
                        }
                        //używamy listy, żeby móc łatwo funkcję z kolekcji uzyskać permutację inwestorów
                        this.inwestorzy = Arrays.asList(inwestorzy);
                    } else if (licznikZnaczącychWierszy == 1) {
                        Akcja[] akcje = new Akcja[słowaWWierszu.length];
                        int j = 0;
                        for (String słowo : słowaWWierszu) {
                            //para Akcja:ostatniaCena
                            String[] dwójkaUporządkowana = słowo.split(":");
                            if (dwójkaUporządkowana[0].isEmpty() || dwójkaUporządkowana[0].length() > 5) {
                                throw new IllegalArgumentException("Błędna długość nazwy akcji");
                            }
                            akcje[j] = new Akcja(dwójkaUporządkowana[0], Integer.parseInt(dwójkaUporządkowana[1]));
                            j++;
                        }
                        this.akcje = akcje;
                    } else if (licznikZnaczącychWierszy == 2) {
                        int budżet = Integer.parseInt(słowaWWierszu[0]);
                        Akcja[] akcjeDoPortfela = new Akcja[słowaWWierszu.length - 1];
                        int[] liczbaAkcji = new int[słowaWWierszu.length - 1];
                        for (int j = 1; j < słowaWWierszu.length; j++) {
                            String[] dwójkaUporządkowana = słowaWWierszu[j].split(":");
                            int k = 0;
                            while (k < this.akcje.length) {
                                if (this.akcje[k].getNazwaAkcji().equals(dwójkaUporządkowana[0])) {
                                    akcjeDoPortfela[j - 1] = this.akcje[k];
                                    break;
                                }
                                k++;
                            }
                            if (k == this.akcje.length) {
                                throw new IllegalArgumentException("Akcja wrzucona do portfela nie istnieje");

                            }
                            liczbaAkcji[j - 1] = Integer.parseInt(dwójkaUporządkowana[1]);
                        }
                        for (Inwestor inwestor : inwestorzy) {
                            Portfel portfel = new Portfel(akcjeDoPortfela, liczbaAkcji, budżet);
                            inwestor.setPortfel(portfel);
                        }
                    }
                    licznikZnaczącychWierszy++;
                }


            }
            if (licznikZnaczącychWierszy > 3) {
                throw new InputSizeException();
            }

        } catch (FileNotFoundException e) {
            System.out.println("No file " + nazwaPliku);
            System.exit(1);
        } catch (InputSizeException e) {
            System.out.println("Zbyt wiele wierszy z informacjami w pliku");
            System.exit(1);
        }
        zleceniaSprzedaży = new ArrayList<>();
        zleceniaKupna = new ArrayList<>();
    }

    //chcemy sortować zlecenia kupna malejąco względem ceny
    static class ComparatorZlecenieKupna implements Comparator<Zlecenie> {
        @Override
        public int compare(Zlecenie z1, Zlecenie z2) {
            if (z1.getCena() > z2.getCena()) return 1;
            else if (z1.getCena() < z2.getCena()) return -1;
            else return 0;
        }
    }

    //a zlecenia sprzedaży rosnąco
    static class ComparatorZlecenieSprzedaży implements Comparator<Zlecenie> {
        @Override
        public int compare(Zlecenie z1, Zlecenie z2) {
            if (z1.getCena() < z2.getCena()) return 1;
            else if (z1.getCena() > z2.getCena()) return -1;
            else return 0;
        }
    }

    public void przeprowadźSymulację() {
        aktualnyNumerTury = 0;
        while (aktualnyNumerTury < liczbaTur) {
            //kolejność składania zleceń losowa
            List<Inwestor> inwestorzySpermutowani = new ArrayList<>(inwestorzy);
            //permutujemy
            Collections.shuffle(inwestorzySpermutowani);
            //zbieramy decyzje od inwestorów
            for (Inwestor inwestor : inwestorzySpermutowani) {
                inwestor.podejmijDecyzję();
            }
            zleceniaSprzedaży.sort(new ComparatorZlecenieSprzedaży());
            zleceniaKupna.sort(new ComparatorZlecenieKupna());
            //obsługa zleceń kupna
            //zlecenia które zostały w pełni zrealizowane zostawiamy w arrayliście z wartością pola liczbaAkcji=0;
            //usuniemy je pod koniec tury
            for (Zlecenie zlecenie : zleceniaKupna) {
                if (nieMaCzegoKupić(zlecenie.getAkcja(), zlecenie.getInwestor())) {
                    continue;
                }
                //niektóre typy zleceń (tylko Wykonaj lub anuluj w tym przypadku) mogą wymagać sprawdzenia ile akcji
                //może zostać zakupionych
                boolean czyWymagaSprawdzenia = zlecenie.czyWymagaSprawdzenia();
                boolean możnaRealizować = true;
                if (czyWymagaSprawdzenia) {
                    int możliwyRozmiarRealizacji = sprawdźMożliwośćRealizacji(zlecenie);
                    //zlecenie WaA może zostać zrealizowane, bo zostanie wypełnione w całości
                    if (!zlecenie.czyRealizujemy(możliwyRozmiarRealizacji, zlecenie.getliczbaAkcji())) {
                        możnaRealizować = false;
                    }
                }
                if (możnaRealizować) {
                    //czyli założenia specyficzne dla rodzaju zlecenia zostały spełnione
                    //zasada realizacji: kupujemy ile się da(minimum z liczby akcji w zleceniu i liczby akcji w
                    //portfelu sprzedającego) i tak aby kupującego było stać,
                    //kupujemy po najniższej cenie, czyli wcześniejszym miejscu w arrayliście zleceniaSprzedaży
                    //uwzględniamy pierwszeństwo zlecenia sprawdzając numerZlecenia (które zlecenie bylo wcześniejsze - kupna czy sprzedaży)
                    for (Zlecenie zlecenieSprzedaży : zleceniaSprzedaży) {
                        if (zlecenieSprzedaży.getCena() <= zlecenie.getCena()) {
                            if (zlecenie.getliczbaAkcji() == 0) {
                                break;
                            }
                            if (zlecenieSprzedaży.getliczbaAkcji() == 0) {
                                //zlecenie sprzedaży obsłużone wcześniej, obecnie jest puste
                                continue;
                            }
                            //dopadowanie między akcjami sprzedawanymi i kupowanymi
                            if (zlecenieSprzedaży.getAkcja().equals(zlecenie.getAkcja())) {
                                //czy akcje są jeszcze w portfelu
                                if (zlecenieSprzedaży.getInwestor().getPortfel().getLiczbaAkcji(zlecenieSprzedaży.getAkcja()) == 0) {
                                    continue;
                                }
                                //kupujemy ile się da, uwzględniając budżet kupującego, portfel sprzedającego, dane ze zleceń
                                int liczbaKupowanychAkcji = Math.min(zlecenieSprzedaży.getliczbaAkcji(),
                                        zlecenieSprzedaży.getInwestor().getPortfel().getLiczbaAkcji(zlecenieSprzedaży.getAkcja()));
                                int cenaAkcjiDoKupienia;
                                //wybieramy cenę wcześniejszego zlecenia
                                if (zlecenieSprzedaży.getNumerZlecenia() < zlecenie.getNumerZlecenia()) {
                                    cenaAkcjiDoKupienia = zlecenieSprzedaży.getCena();
                                } else {
                                    cenaAkcjiDoKupienia = zlecenie.getCena();
                                }
                                liczbaKupowanychAkcji = Math.min(zlecenie.getInwestor().getPortfel().getBudżet() / cenaAkcjiDoKupienia, liczbaKupowanychAkcji);
                                int cenaTransakcji = liczbaKupowanychAkcji * cenaAkcjiDoKupienia;
                                //przemieszczamy akcje i pieniądze między portfelami, a także aktualizujemy zlecenia
                                zlecenieSprzedaży.getInwestor().getPortfel().usuńAkcje(zlecenieSprzedaży.getAkcja(), liczbaKupowanychAkcji);
                                zlecenieSprzedaży.getInwestor().getPortfel().przelewPrzychodzący(cenaTransakcji);
                                zlecenieSprzedaży.zmniejszLiczbęAkcji(liczbaKupowanychAkcji);
                                zlecenie.getInwestor().getPortfel().dodajAkcje(zlecenie.getAkcja(), liczbaKupowanychAkcji);
                                zlecenie.getInwestor().getPortfel().przelewWychodzący(cenaTransakcji);
                                zlecenie.zmniejszLiczbęAkcji(liczbaKupowanychAkcji);
                                //System.out.println("Inwestor " + zlecenie.getInwestor() + " kupuje " + liczbaKupowanychAkcji + " akcji " + zlecenie.getAkcja().getNazwaAkcji() + " po cenie " + cenaAkcjiDoKupienia + " od " + zlecenieSprzedaży.getInwestor());
                                zlecenie.getAkcja().setOstatniaCena(cenaAkcjiDoKupienia);
                            }
                        } else {
                            //minęliśmy cenę umożliwiającą dopasowanie zleceń, a zlecenia sprzedaży są posortowane rosnąco,
                            //więc już nie ma co przeglądać ich dalej
                            break;
                        }
                    }

                }
            }
            //blok na koniec tury
            usuńWygasłeZlecenia();
            zaktualizujSMA();
            aktualnyNumerTury++;
        }
    }

    //aby uniknąć sprawdzania zleceń niemożliwych do realizacji z powodu zmonopolizowania danej Akcji przez inwestora kupującego stosujemy tę metodę
    private boolean nieMaCzegoKupić(Akcja akcja, Inwestor kupujący) {
        for (Inwestor inwestor : inwestorzy) {
            if (!inwestor.equals(kupujący)) {
                if (inwestor.getPortfel().getLiczbaAkcji(akcja) > 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public int sprawdźMożliwośćRealizacji(Zlecenie zlecenie) {
        //zwraca liczbę akcji do sprzedania o mniejszej/równej cenie co oferta kupna przekazana w argumencie, uwzględniając budżet kupującego
        int kosztZakupu = 0;
        int budżetKupującego = zlecenie.getInwestor().getPortfel().getBudżet();
        int rozmiarZlecenia = zlecenie.getliczbaAkcji();
        int liczbaZaspokojonychAkcji = 0;
        Akcja pożądanaAkcja = zlecenie.getAkcja();
        //tworzymy kopię portfeli inwestorów i zmieniamy tam wartości żeby obliczyć potencjalną liczbę akcji, które można kupić
        //(może się zdarzyć że w zleceniach sprzedaży są zlecenia, których sprzedający nie zrealizują przez brak akcji)
        Map<Inwestor, Portfel> kopiePortfolio = new HashMap<>();
        for (Inwestor inwestor : inwestorzy) {
            kopiePortfolio.put(inwestor, new Portfel(inwestor.getPortfel().weźKopięPortfolio()));
        }
        for (Zlecenie zlecenieSprzedaży : zleceniaSprzedaży) {
            if (zlecenieSprzedaży.getAkcja().equals(pożądanaAkcja)) {
                //zlecenie sprzedaży puste
                if (zlecenieSprzedaży.getliczbaAkcji() == 0) {
                    continue;
                }
                //inwestorzy mogą sie dogadać
                if (zlecenieSprzedaży.getCena() <= zlecenie.getCena()) {
                    int cenaAkcjiDoKupienia;
                    if (zlecenieSprzedaży.getNumerZlecenia() < zlecenie.getNumerZlecenia()) {
                        cenaAkcjiDoKupienia = zlecenieSprzedaży.getCena();
                    } else {
                        cenaAkcjiDoKupienia = zlecenie.getCena();
                    }
                    if (budżetKupującego - kosztZakupu < zlecenie.getCena()) {
                        break;
                    }
                    //sprawdzamy ile akcji można kupić
                    int liczbaAkcji = Math.min(zlecenieSprzedaży.getliczbaAkcji(), kopiePortfolio.get(zlecenieSprzedaży.getInwestor()).getLiczbaAkcji(zlecenieSprzedaży.getAkcja()));
                    if (liczbaAkcji == 0) {
                        continue;
                    }
                    if (liczbaZaspokojonychAkcji + liczbaAkcji < rozmiarZlecenia) {
                        //kupujemy wszystkie akcje albo tyle na ile starczy pieniędzy
                        liczbaAkcji = Math.min(liczbaAkcji, (budżetKupującego - kosztZakupu) / cenaAkcjiDoKupienia);
                        liczbaZaspokojonychAkcji += liczbaAkcji;
                        kosztZakupu += (liczbaAkcji * cenaAkcjiDoKupienia);
                        kopiePortfolio.get(zlecenieSprzedaży.getInwestor()).usuńAkcje(zlecenieSprzedaży.getAkcja(), liczbaAkcji);

                    } else {
                        liczbaAkcji = Math.min(rozmiarZlecenia - liczbaZaspokojonychAkcji, (budżetKupującego - kosztZakupu) / cenaAkcjiDoKupienia);
                        liczbaZaspokojonychAkcji += liczbaAkcji;
                        kosztZakupu += liczbaAkcji * cenaAkcjiDoKupienia;
                        break;
                    }
                } else {
                    //cena umożliwiająca porozumienie nie wystąpi już w tej liście
                    break;
                }
            }
        }
        return liczbaZaspokojonychAkcji;
    }


    //ciąg funkcji o oczywistym działaniu
    public void usuńWygasłeZlecenia() {
        zleceniaKupna.removeIf(Zlecenie::czyWygasło);
        zleceniaSprzedaży.removeIf(Zlecenie::czyWygasło);
    }

    public static Akcja wylosujAkcję() {
        return akcje[Losowanie.losuj(0, akcje.length - 1)];
    }

    public void drukujStanKońcowy() {
        for (Inwestor inwestor : this.inwestorzy) {
            inwestor.getPortfel().printPortfolio();
        }
    }

    public static void dodajZlecenieKupna(Zlecenie zlecenie) {
        zleceniaKupna.add(zlecenie);
    }

    public static void dodajZlecenieSprzedaży(Zlecenie zlecenie) {
        zleceniaSprzedaży.add(zlecenie);
    }

    public void zaktualizujSMA() {
        for (Akcja akcja : akcje) {
            akcja.zaktualizujSMA();
        }
    }
}
