import java.util.List;
import java.util.Random;

public class MonteCarloTreeSearch {
    private Node root;
    private static final int numOfSimulations = 1000;


    public MonteCarloTreeSearch(Board board) {
        root = new Node(null, board.copy());
}

    public int findBestMove(double bonus) {
        for (int i = 0; i < numOfSimulations * bonus; i++) {
            //System.out.println("Simulation # " + i);
            // Step 1: Selection
            Node leaf = selectNode(root);
            //System.out.println("Selected Node: " + leaf.getBoard().toString());

            //System.out.println("Expansion phase");           // Step 2: Expansion
            // jump to simulation if leaf node
            Node nodeToExplore = expandNode(leaf);

            //System.out.println("Expanded Node: " + nodeToExplore.getBoard().toString());

            //System.out.println("Simulation phase");
            // Step 3: Simulation/Roll out
            int result = randomSimulation(nodeToExplore);

            //System.out.println("Back Propagation phase");
            // Step 4: Back Propagation
            backPropagate(nodeToExplore, result);
        }

        int maxIndex = root.getChildIndexWithMaxSimulation();
        Node winnerNode = root.getExpandedNodes().get(maxIndex);
        System.out.print("==> Move column selected: [" + maxIndex + "] - ");
        switch(root.getBoard().getPlayerTurn()) {
            case Board.RED_WON:
                System.out.print("Red win counts: " + winnerNode.getRedWinCount() + " - Number of simulations: " + winnerNode.getSimulationCount());
                System.out.println("\n==> Estimated probability best move: " + Math.round(((double) winnerNode.getRedWinCount() / winnerNode.getSimulationCount())*100) + "%");
                break;
            case Board.YELLOW_WON:
                System.out.print("YELLOW win counts: " + winnerNode.getYellowWinCount() + " - Number of simulations: " + winnerNode.getSimulationCount());
                System.out.println("\n==> Estimated probability best move: " + Math.round(((double)winnerNode.getYellowWinCount() / winnerNode.getSimulationCount())*100) + "%");
                break;
        }
        return root.getChildIndexWithMaxSimulation();
    }

    /* Traverse down the search tree & select nodes untill NOT FULLY EXPANDED NODE / LEAF NODE:
        1. Repeatedly select most promising legal move
        2. Move to that most promising node.
        3. Stop if the current node is a leaf node or not fully expanded. */
    private Node selectNode(Node rootNode) {
        Node currentNode = rootNode;
        while (!currentNode.isLeaf() & currentNode.isFullyExpanded())
                // Pick move that maximizes UCT value.
                currentNode = UpperConfidenceTree.findBestNodeWithUCT(currentNode);
        return currentNode;
    }

    /* 1. Randomly choose one of possible moves
       2. Create a child node according to that move.
       3. Add this node to the selected node after SELECTION PHASE to expand our search tree
       4. Playing stats will be initialized to 0  */
    private Node expandNode(Node node) {
        if (node.getUnexpandedNodes().size() == 0) {
            return node;
        } else {
            List<Node> unexpandedNodes = node.getUnexpandedNodes();
            int selectedIndex = (int) (Math.random() * unexpandedNodes.size());
            Node nodeToExpand = unexpandedNodes.get(selectedIndex);
            unexpandedNodes.remove(selectedIndex);
            node.getExpandedNodes().add(nodeToExpand); // add the newly created node to the current node}
            return nodeToExpand;
        }
    }

    /* Always simulate on a leaf node.
    1. Simulating game until it is finish (win/lost/draw)
    2. Moves are chosen randomly
    3. Return simulation result */
    private int randomSimulation(Node expandedNode) {
        Board simulatingBoard = expandedNode.getBoard().copy();

        // simulate the game by making random moves. If the game is finished, return board state
        while (simulatingBoard.checkBoardState() == Board.IN_PROGRESS) {
            // get possible columns that can be played
            List<Integer> legalMoveColumns = simulatingBoard.getLegalMoveColumns();

            // pick a random column
            Random rand = new Random();
            int selectedIndex = legalMoveColumns.get(rand.nextInt(legalMoveColumns.size()));

            //System.out.println("Playable Columns: " + legalMoveColumns);
            //simulatingBoard.printPlayerTurn();
            //System.out.println("Picking Column Index #" + selectedIndex);

            simulatingBoard.dropMarker(selectedIndex);
            simulatingBoard.alternatePlayerTurn();

            //System.out.println(simulatingBoard.toString());
        }
        //simulatingBoard.printBoardState();
        return simulatingBoard.checkBoardState();
    }

    /*
    1. Update parent statistics after the simulation playout. For each visited node:
    1. Increase simulation count
    2. Increase player win counts, draw count
    3. Increase win counts for each   */
    private void backPropagate(Node expandedNode, int simulationResult) {
        Node currentNode = expandedNode;
        while (currentNode != null) {
            currentNode.incrementSimulationCount();
            switch (simulationResult) {
                case Board.RED_WON:
                    currentNode.incrementRedWinCount();
                    break;
                case Board.YELLOW_WON:
                    currentNode.incrementYellowWinCount();
                    break;
                case Board.DRAW:
                    currentNode.incrementDrawCount();
                    break;
            }
            currentNode = currentNode.getParent();
        }
    }


}
