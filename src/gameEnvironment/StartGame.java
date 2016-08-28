package gameEnvironment;

/**
 * Created by Juviro on 26.08.2016.
 */
public class StartGame {

    public static void main(String args[]){
        //test();
        int numberOfGames = 1;
        String startingSetup = "2200033220103300111000111110001110033010223300022";
        new StartGame(numberOfGames, startingSetup);
    }


    public static void test() {
        for (int i = 0; i < 100; i++) {
            System.out.println((int) (Math.random() * 16) > 15);
        }
    }

    public StartGame(int numberOfGames, String startingSetup) {
        boolean whiteStarts = true;
        for (int i = 0; i < numberOfGames; i++) {
            new GameProcessor(startingSetup, whiteStarts);
            whiteStarts = !whiteStarts;
        }
    }
}
