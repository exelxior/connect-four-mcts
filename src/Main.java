public class Main {

    public static void main(String[] args) {
        System.out.println("\n[CONNECT FOUR GAME - MONTE CARLO TREE SEARCH METHOD]");
        System.out.println("     Computer RED [R] VS Computer YELLOW [R]\n");

        Board ConnectFourBoard = new Board(7, 6, Board.RED_TURN); // Red always play first

        int moveColumn;
        while (ConnectFourBoard.checkBoardState() == Board.IN_PROGRESS) {

            ConnectFourBoard.printPlayerTurn();
            MonteCarloTreeSearch mcts = new MonteCarloTreeSearch(ConnectFourBoard);

            if (ConnectFourBoard.getPlayerTurn() == Board.RED_TURN)
                moveColumn = mcts.findBestMove(1);
            else
                moveColumn = mcts.findBestMove(1.5); // I chose to sample more for YELLOW player

            ConnectFourBoard.dropMarker(moveColumn);
            ConnectFourBoard.alternatePlayerTurn();
            System.out.println(ConnectFourBoard.toString());
        }
        // game is done at this point, print out results
        System.out.println("\n GAME FINISHED!!");
        ConnectFourBoard.printBoardState();
    }
}