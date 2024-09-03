package inwestorzy;
import inwestorzy.Inwestor;
import inwestorzy.InwestorRandom;
import inwestorzy.InwestorSMA;
public class FabrykaInwestorów {
    //generujemy inwestorów zgodnie z treścią pliku
        public static Inwestor createInwestor(boolean random) {
            if(random){
                return new InwestorRandom();
            }
            return new InwestorSMA();
        }

}
