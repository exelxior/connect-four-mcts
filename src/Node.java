import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Node {

    private Node parent;
    private List<Node> expandedNodes;
    private List<Node> unexpandedNodes;
    private Board board;
    private int simulationCount; // trials
    private int redWinCount;
    private int yellowWinCount;
    private int drawCount;

    public Node(Node parent, Board board) {
        this.parent = parent;
        this.board = board;
        this.simulationCount = 0;
        this.redWinCount = 0;
        this.yellowWinCount = 0;
        this.drawCount = 0;
        this.unexpandedNodes = new ArrayList<>();
        this.generateUnexpandedNodes();
        // generate child Nodes last otherwise could cause conflict
        this.expandedNodes = new ArrayList<>();
        //this.generateChildNodes();
    }

    public Node(Board board) {
        this.parent = null;
        this.board = board;
        this.simulationCount = 0;
        this.redWinCount = 0;
        this.yellowWinCount = 0;
        this.drawCount = 0;
        this.unexpandedNodes = new ArrayList<>();
        this.expandedNodes = new ArrayList<>(); // new child nodes will be added later
    }

    public void setParent(Node root) {
        this.parent = root;
    }

    public List<Node> getUnexpandedNodes() {
        return unexpandedNodes;
    }

    public void generateUnexpandedNodes() {
        // get possible columns that can be played
        List<Integer> playableColumnList = board.getLegalMoveColumns();

        for (int i = 0; i < playableColumnList.size(); i++) {
            Node newNode = new Node(board.copy());
            newNode.setParent(this);
            newNode.getBoard().dropMarker(playableColumnList.get(i));
            newNode.getBoard().alternatePlayerTurn();
            newNode.getExpandedNodes();
            this.unexpandedNodes.add(newNode);
        }
    }

    public Board getBoard() {
        return board;
    }

    public Node getParent() {
        return parent;
    }

    public List<Node> getExpandedNodes() {
        return expandedNodes;
    }

    public int getSimulationCount() {
        return simulationCount;
    }

    public int getChildIndexWithMaxSimulation() {
        int maxIndex = 0;
        for (int i = 1; i < expandedNodes.size(); i++) {
            if (expandedNodes.get(i).getSimulationCount() > expandedNodes.get(maxIndex).getSimulationCount()) {
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    public void incrementSimulationCount() {
        simulationCount++;
    }

    public void incrementRedWinCount() {
        redWinCount++;
    }

    public void incrementYellowWinCount() {
        yellowWinCount++;
    }

    public void incrementDrawCount() {
        drawCount++;
    }

    public int getRedWinCount() {
        return redWinCount;
    }

    public int getYellowWinCount() {
        return yellowWinCount;
    }

    public int getDrawCount() {
        return drawCount;
    }

    // check if the node has been fully expanded
    public boolean isFullyExpanded() {
        return unexpandedNodes.isEmpty();
    }

    // Check if this node is terminal/leaf (no children)
    public boolean isLeaf() {
        return expandedNodes.isEmpty();
    }
}
