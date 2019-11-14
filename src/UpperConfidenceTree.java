import java.util.Collections;
import java.util.Comparator;

public class UpperConfidenceTree {
    private static final double UCT_CONSTANT = 1;

    private static double uctValue(int parentSimulationCount, int winCount, int drawCount, int nodeSimulationCount) {
        if (nodeSimulationCount == 0) {
            return Integer.MAX_VALUE; // Prioritize unexpanded / unsampled nodes
        }
        return (winCount + (double)(drawCount/2) / nodeSimulationCount + UCT_CONSTANT * Math.sqrt(Math.log(parentSimulationCount) / (double) nodeSimulationCount));
    }

    public static Node findBestNodeWithUCT(Node node) {
        int parentSimulationCount = node.getSimulationCount();
        if (node.getBoard().getPlayerTurn() == Board.RED_TURN) {
            return Collections.max(node.getExpandedNodes(),
                    Comparator.comparing(
                            c -> uctValue(parentSimulationCount,
                                    c.getRedWinCount(),
                                    c.getDrawCount(),
                                    c.getSimulationCount() )));
        } else {
            return Collections.max(node.getExpandedNodes(),
                    Comparator.comparing(
                            c -> uctValue(parentSimulationCount,
                                    c.getYellowWinCount(),
                                    c.getDrawCount(),
                                    c.getSimulationCount() )));
        }
    }
}

