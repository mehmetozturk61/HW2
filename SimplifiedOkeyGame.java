import java.util.Arrays;
import java.util.Random;

public class SimplifiedOkeyGame {

    Player[] players;
    Tile[] tiles;
    int tileCount;

    Tile lastDiscardedTile;

    int currentPlayerIndex = 0;

    public SimplifiedOkeyGame() {
        players = new Player[4];
    }

    /**
     * This method creates all the tiles. From 1 to 26, each number used 4 times.
     */
    public void createTiles() {
        tiles = new Tile[104];
        int currentTile = 0;

        // four copies of each value, no jokers
        for (int i = 1; i <= 26; i++) {
            for (int j = 0; j < 4; j++) {
                tiles[currentTile++] = new Tile(i);
            }
        }

        tileCount = 104;
    }

    /**
     * This method distributes the starting tiles to the players player at index 0 gets 15 tiles and starts first
     * other players get 14 tiles, this method assumes the tiles are already shuffled.
     */
    public void distributeTilesToPlayers() {
        int tileIndex = 0;

        for (int playerIndex = 0; playerIndex < players.length; playerIndex++) {
            for (int j = 0; j < 14; j++) {
                players [playerIndex].addTile (tiles [tileIndex]);
                tileIndex++;
            }
        }

        // Adding one extra tile to the first player
        players[0].addTile (tiles [tileIndex]);
    }

    /*
     * Adds the last discarded tile to player's hand. Returns last discarded tile.
     */
    public String getLastDiscardedTile() {
        players[currentPlayerIndex].addTile(lastDiscardedTile);
        return lastDiscardedTile.toString();
    }

    /*
     * Gets the top tile from tiles array for the current player
     * that tile is no longer in the tiles array (this simulates picking up the top tile)
     * and it will be given to the current player
     * returns the toString method of the tile so that we can print what we picked
     */
    public String getTopTile() {

        Tile addedTile = tiles[tiles.length - 1];
        players[currentPlayerIndex].addTile(addedTile);

        tiles = Arrays.copyOf(tiles, tiles.length - 1);

        return addedTile.toString();
    }

    /**
     * This method randomly shuffles the tiles array using Fisher-Yates shuffle before game starts.
     * 
     * @see <a href=https://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle>Fisher-Yates Shuffle</a>
     */
    public void shuffleTiles() {
        Random random = new Random();

        for (int i = tiles.length - 1; i > 0; i--) {
            // Pick a random index from 0 to i
            int randomIndex = random.nextInt(i + 1);

            // Swap tiles[i] and tiles[randomIndex]
            Tile temp = tiles[i];
            tiles[i] = tiles[randomIndex];
            tiles[randomIndex] = temp;
        }
    }

    /*
     * Returns true if current player has met the winning condition or there isn't any tile left in the stack.
     */
    public boolean didGameFinish() {
        
        return players[this.getCurrentPlayerIndex()].checkWinning() || !this.hasMoreTileInStack(); 
    }

    /* finds the player who has the highest number for the longest chain
     * if multiple players have the same length may return multiple players
     */
    public Player[] getPlayerWithHighestLongestChain() {

        int maximum = 0;
        int maximumCount = 0;
        int indexCount = 0;
        for( int i = 0 ; i < players.length ; i++)
        {
            int longestChain = players[i].findLongestChain();
            if( longestChain > maximum)
                maximum = longestChain;
        }
        for( int i = 0 ; i < players.length ; i++)
        {
            int longestChain = players[i].findLongestChain();
            if( longestChain == maximum)
                maximumCount++; 
        }
        Player winners[] = new Player[maximumCount];
        for( int i = 0 ; i < players.length ; i++)
        {
            int longestChain = players[i].findLongestChain();
            if( longestChain == maximum)
                winners[indexCount++] = players[i];
        }
        return winners;
    }
    
    /*
     * checks if there are more tiles on the stack to continue the game
     */
    public boolean hasMoreTileInStack() {
        return tileCount != 0;
    }

    /*
     * Method that picks a tile for the computer.
     * Checks if getting the discarded tile is useful for the computer
     * by checking if it increases the longest chain length, if it does
     *  gets the discarded tile if not gets the top tile
     */
    public void pickTileForComputer() {

        Player p = players[getCurrentPlayerIndex()];

        // A reference to the original array.
        Tile[] originalPlayerTiles = p.playerTiles;
        int prevLongestChain = p.findLongestChain();

        // Changes the playerTiles reference to a temporary array and adds the last discarded tile to that array to use the findLongestChain() method.
        p.playerTiles = Arrays.copyOf(p.playerTiles, p.playerTiles.length);
        p.addTile(lastDiscardedTile);
        int newLongestChain = p.findLongestChain();

        // Changes the playerTiles reference to the original array.
        p.playerTiles = originalPlayerTiles;

        if(newLongestChain > prevLongestChain){
            getLastDiscardedTile();
        }
        else{
            getTopTile();
        }
    }

    /*
     * Current computer player discards the least useful tile.
     * The least useful tiles are duplicates, and then the tiles that contribute to the shortest chain.
     */
    public void discardTileForComputer() {

        boolean duplicateFound = false;
        Tile[] currPlayerTiles = players[getCurrentPlayerIndex()].playerTiles;

        int minChainLength = 16, minChainIndex = 0, runningChainIndex = 0, runningChainLength = 0, currTileValue, prevTileValue = -1;

        for (int i = 0; i < currPlayerTiles.length; i++) {

            currTileValue = currPlayerTiles[i].value;

            if (currTileValue + 1 == prevTileValue || prevTileValue == -1) {
                runningChainLength++;
            }
            else if (currTileValue == prevTileValue) {
                discardTile(i);
                duplicateFound = true;
            }
            else {

                if (minChainLength > runningChainLength) {
                    minChainLength = runningChainLength;
                    minChainIndex = runningChainIndex;
                }

                runningChainIndex = i;
                runningChainLength = 1;

                // Edge case for the end.
                if(i == currPlayerTiles.length - 1){
                    minChainIndex = i;
                }

                prevTileValue = currTileValue;
            }

            if (!duplicateFound) discardTile(minChainIndex);
            
        }
    }

    /*
     * Discards the current player's tile at given index
     * Sets the lastDiscardedTile variable and removes that tile from
     * that player's tiles
     */
    public void discardTile(int tileIndex) {
        lastDiscardedTile = players[getCurrentPlayerIndex()].getAndRemoveTile(tileIndex);
    }

    public void displayDiscardInformation() {
        if(lastDiscardedTile != null) {
            System.out.println("Last Discarded: " + lastDiscardedTile.toString());
        }
    }

    public void displayCurrentPlayersTiles() {
        players[currentPlayerIndex].displayTiles();
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

      public String getCurrentPlayerName() {
        return players[currentPlayerIndex].getName();
    }

    public void passTurnToNextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % 4;
    }

    public void setPlayerName(int index, String name) {
        if(index >= 0 && index <= 3) {
            players[index] = new Player(name);
        }
    }

}
