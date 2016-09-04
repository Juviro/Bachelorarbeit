package gameenvironment;

public class StartGame {

    public static void main(String args[]){
        int numberOfGames = 2;
        String startingSetup = "2200033220103300111000111110001110033010223300022";
        new StartGame(numberOfGames, startingSetup, true);
    }

    private StartGame(int numberOfGames, String startingSetup, boolean whiteStarts) {
        int wonGamesWhite = 0;
        int wonGamesBlack = 0;
        for (int i = 0; i < numberOfGames; i++) {
            GameProcessor currentGame = new GameProcessor(startingSetup, whiteStarts, (i + 1));
            whiteStarts = !whiteStarts;
            switch(currentGame.winner) {
                case 2: wonGamesWhite++;break;
                case 3: wonGamesBlack++;break;
            }
        }
        System.out.println("\nwonGamesWhite = " + wonGamesWhite);
        System.out.println("wonGamesBlack = " + wonGamesBlack);
    }
}
