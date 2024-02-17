public class Player {
    String playerName;
    Tile[] playerTiles;
    int numberOfTiles;

    public Player(String name) {
        setName(name);
        playerTiles = new Tile[15]; // there are at most 15 tiles a player owns at any time
        numberOfTiles = 0; // currently this player owns 0 tiles, will pick tiles at the beggining of the game
    }

    /*
     * TODO: checks this player's hand to determine if this player is winning
     * the player with a complete chain of 14 consecutive numbers wins the game
     * note that the player whose turn is now draws one extra tile to have 15 tiles in hand,
     * and the extra tile does not disturb the longest chain and therefore the winning condition
     * check the assigment text for more details on winning condition
     */
    public boolean checkWinning() {
        return false;
    }

    /**
     * This method finds the longest chain in this player hand
     * this method iterates over playerTiles to find the longest chain
     * of consecutive numbers, used for checking the winning condition
     * and also for determining the winner if tile stack has no tiles.
     */
    public int findLongestChain() {
        int longestChain = 0;
        int currentChain = 0;
        int currentValue;
        int previousValue = -1;

        for (Tile t : playerTiles) {
            currentValue = t.getValue();

            if (currentValue == previousValue + 1) { // If current value is consecutive
                currentChain++;
            }
            else if (currentValue != previousValue) { // Ignoring the case that player has same number multiple times
                if (currentChain > longestChain) { // If current chain is larger than the longest chain so far
                    longestChain = currentChain;
                }

                // Resetting the chain
                currentChain = 1;
            }

            // Updating previous value
            previousValue = currentValue;
        }

        return longestChain;
    }

    /*
     * TODO: removes and returns the tile in given index position
     */
    public Tile getAndRemoveTile(int index) {
        return null;
    }

    /**
     * This method adds the given tile to this player's hand keeping the ascending order
     * by looping over the existing tiles to find the correct position,
     * then shifts the remaining tiles to the right by one.
     */
    public void addTile(Tile t) {
        int insertionIndex;

        // While the tile t's value is smaller than the tile's value in insertion index, we increment the insertion index.
        for (insertionIndex = 0; playerTiles[insertionIndex] != null && t.getValue() > playerTiles[insertionIndex].getValue(); insertionIndex++);

        // Updating the player tiles array
        for (int updateIndex = playerTiles.length - 1; updateIndex > insertionIndex; updateIndex--) {
            playerTiles[updateIndex] = playerTiles[updateIndex - 1];
        }

        // Inserting the new tile
        playerTiles[insertionIndex] = t;
        this.numberOfTiles++;
    }

    /*
     * finds the index for a given tile in this player's hand
     */
    public int findPositionOfTile(Tile t) {
        int tilePosition = -1;
        for (int i = 0; i < numberOfTiles; i++) {
            if(playerTiles[i].matchingTiles(t)) {
                tilePosition = i;
            }
        }
        return tilePosition;
    }

    /*
     * displays the tiles of this player
     */
    public void displayTiles() {
        System.out.println(playerName + "'s Tiles:");
        for (int i = 0; i < numberOfTiles; i++) {
            System.out.print(playerTiles[i].toString() + " ");
        }
        System.out.println();
    }

    public Tile[] getTiles() {
        return playerTiles;
    }

    public void setName(String name) {
        playerName = name;
    }

    public String getName() {
        return playerName;
    }
}
