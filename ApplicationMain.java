import java.util.Scanner;

public class ApplicationMain {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        SimplifiedOkeyGame game = new SimplifiedOkeyGame();

        System.out.print("Please enter your name: ");
        String playerName = sc.next();

        game.setPlayerName(0, playerName);
        game.setPlayerName(1, "John");
        game.setPlayerName(2, "Jane");
        game.setPlayerName(3, "Ted");

        game.createTiles();
        game.shuffleTiles();
        game.distributeTilesToPlayers();

        // developer mode is used for seeing the computer players hands, to be used for debugging
        System.out.print("Play in developer's mode with other player's tiles visible? (Y/N): ");
        char devMode = sc.next().charAt(0);
        boolean devModeOn = devMode == 'Y';
        
        boolean firstTurn = true;
        boolean gameContinues = true;
        int playerChoice = -1;

        while(gameContinues) {
            
            int currentPlayer = game.getCurrentPlayerIndex();
            System.out.println(game.getCurrentPlayerName() + "'s turn.");
            
            if(currentPlayer == 0) {
                // this is the human player's turn
                game.displayCurrentPlayersTiles();
                game.displayDiscardInformation();

                System.out.println("What will you do?");

                if(!firstTurn) {
                    // after the first turn, player may pick from tile stack or last player's discard
                    System.out.println("1. Pick From Tiles");
                    System.out.println("2. Pick From Discard");
                }
                else{
                    // on first turn the starting player does not pick up new tile
                    System.out.println("1. Discard Tile");
                }

                System.out.print("Your choice: ");
                playerChoice = sc.nextInt();

                // after the first turn we can pick up
                if(!firstTurn) {
                    if(playerChoice == 1) {
                        System.out.println("You picked up: " + game.getTopTile());
                        firstTurn = false;
                    }
                    else if(playerChoice == 2) {
                        System.out.println("You picked up: " + game.getLastDiscardedTile());
                    }

                    // display the hand after picking up new tile
                    game.displayCurrentPlayersTiles();
                }
                else{
                    // after first turn it is no longer the first turn
                    firstTurn = false;
                }

                gameContinues = !game.didGameFinish() && game.hasMoreTileInStack();

                if(gameContinues) {
                    // if game continues we need to discard a tile using the given index by the player
                    System.out.println("Which tile you will discard?");
                    System.out.print("Discard the tile in index: ");
                    playerChoice = sc.nextInt();

                    // TODO: make sure the given index is correct, should be 0 <= index <= 14

                    game.discardTile(playerChoice);
                    game.passTurnToNextPlayer();
                }
                else{
                    if(!game.didGameFinish()) {
                        // if we finish the hand we win
                        System.out.println("Congratulations, you win!");    
                    }
                    else{
                        // TODO: the game ended with no more tiles in the stack
                        // determine the winner based on longest chain lengths of the players
                        // use getPlayerWithHighestLongestChain method of game for this task
                    }
                }
            }
            else{
                // this is the computer player's turn
                if(devModeOn) {
                    game.displayCurrentPlayersTiles();
                }

                // computer picks a tile from tile stack or other player's discard
                game.pickTileForComputer();

                gameContinues = !game.didGameFinish() && game.hasMoreTileInStack();

                if(gameContinues) {
                    // if game did not end computer should discard
                    game.discardTileForComputer();
                    game.passTurnToNextPlayer();
                }
                else{
                    if(!game.didGameFinish()) {
                        // current computer character wins
                        System.out.println(game.getCurrentPlayerName() + " wins.");
                    }
                    else{
                        // TODO: the game ended with no more tiles in the stack
                        // determine the winner based on longest chain lengths of the players
                        // use getPlayerWithHighestLongestChain method of game for this task
                    }
                }
            }
        }

        sc.close();
    }
}