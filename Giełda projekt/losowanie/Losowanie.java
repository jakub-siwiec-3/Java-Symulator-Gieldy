package losowanie;
import java.util.Random;
public class Losowanie {
    //losuje liczbę całkowitą w zakresie <dolna, górna>
    public static int losuj (int dolna, int gorna){
        Random random = new Random();
        return random.nextInt(gorna-dolna+1) + dolna;
    }
}