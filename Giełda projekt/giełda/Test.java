package giełda;
//testy junit dla czytania inputu

import main.Main;


import static org.junit.jupiter.api.Assertions.*;
public class Test {


    @org.junit.Test
    public void testCzytajInputPoprawneDane() {
        String[] args = {"src/test/data.txt", "1000"};
        assertDoesNotThrow(() -> Main.main(args));
    }
    @org.junit.Test
    public void testCzytajInputDługaNazwaAkcji() {
        String[] args = {"src/test/długaNazwaAkcji.txt", "1000"};
        assertThrows(IllegalArgumentException.class, () -> {
            Main.main(args);
        });
    }

    @org.junit.Test
    public void testCzytajInputZłaNazwaAkcji() {
        String[] args = {"src/test/złaNazwaAkcji.txt", "1000"};
        assertThrows(IllegalArgumentException.class, () -> {
            Main.main(args);
        });
    }
    @org.junit.Test
    public void testCzytajInputNielegalnyTypInwestora() {
        String[] args = {"src/test/nielegalnyTypInwestora.txt", "1000"};
        assertThrows(IllegalArgumentException.class, () -> {
            Main.main(args);
    });
    }

    @org.junit.Test
    public void testCzytajInputBrakArgumentów() {
        String[] args = {"src/test/złanazwa.txt"};
        assertThrows(AssertionError.class, () -> {
            Main.main(args);
        });
    }
}

