package gameenvironment;

public class StartGame {

    public static void main(String args[]){
        int numberOfGames = 3;
        String startingSetup = "2200033220103300111000111110001110033010223300022";
        new StartGame(numberOfGames, startingSetup);
    }

    private StartGame(int numberOfGames, String startingSetup) {
        boolean whiteStarts = true;
        for (int i = 0; i < numberOfGames; i++) {
            new GameProcessor(startingSetup, whiteStarts, (i + 1));
            whiteStarts = !whiteStarts;
        }
    }
}
