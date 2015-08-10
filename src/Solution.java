import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class Solution {
	public final boolean DEBUG = false;

	private static BufferedReader br;
	private static int numCases;

	public static void main(String[] args) throws IOException {
		Solution s = new Solution();

		br = new BufferedReader(new InputStreamReader(System.in));

		numCases = Integer.parseInt(br.readLine()); // T
		for (int testCase = 0; testCase < numCases; testCase++) {
			s.readInput();
			s.generateGraph();
			s.findPath();
			s.output();
		}
	}

	private int numSteps;
	private int maxJumpLength;
	private int crossingPenalty;
	private int[] stairA;
	private int[] stairB;
	private Node[] stairANodes;
	private Node[] stairBNodes;
	private Node goalNode;

	/** Reading the input */
	public void readInput() throws IOException {
		String[] info = br.readLine().split(" ");
		numSteps = Integer.parseInt(info[0]); // N
		maxJumpLength = Integer.parseInt(info[1]); // K
		crossingPenalty = Integer.parseInt(info[2]); // P
		stairA = parseStaircase(br.readLine());
		stairB = parseStaircase(br.readLine());
	}

	private int[] parseStaircase(String stairString) {
		int[] stair = new int[numSteps];
		String[] stairSteps = stairString.split(" ");
		for (int step = 0; step < numSteps; step++) {
			stair[step] = Integer.parseInt(stairSteps[step]);
		}
		return stair;
	}

	/** Generating the graph */
	private class Node {
		// A set of where I can go from here, and the associated cost
		public Map<Node, Integer> next = new HashMap<>();
		private final String staircase;
		private final int stepNum;
		public int distanceFromStart;
		public int heuristicDistance;

		public Node(String istaircase, int istepNum) {
			staircase = istaircase;
			stepNum = istepNum;
			distanceFromStart = Integer.MAX_VALUE;
		}

		public int getStepNum() {
			return stepNum;
		}

		public String toString() {
			return staircase + stepNum;
		}
	}

	public void generateGraph() {
		// Init nodes
		stairANodes = new Node[numSteps];
		stairBNodes = new Node[numSteps];
		for (int step = 0; step < numSteps; step++) {
			stairANodes[step] = new Node("A", step);
			stairBNodes[step] = new Node("B", step);
		}
		goalNode = new Node("G0", numSteps);
		// Join straight up (A[i] -> A[i+1] ... A[i+k])
		for (int step = 0; step < numSteps; step++) {
			for (int r = 1; r <= maxJumpLength && step + r < numSteps; r++) {
				// The movement cost is associated with the step you are leaving
				stairANodes[step].next.put(stairANodes[step + r], stairA[step]);
				stairBNodes[step].next.put(stairBNodes[step + r], stairB[step]);
			}
			// Jump to top of stairs
			if (step + maxJumpLength >= numSteps) {
				stairANodes[step].next.put(goalNode, stairA[step]);
				stairBNodes[step].next.put(goalNode, stairB[step]);
			}
		}
		// Cross-link stairs (A[i] -> B[i] ... B[i+k])
		for (int step = 0; step < numSteps; step++) {
			for (int r = 0; r <= maxJumpLength && step + r < numSteps; r++) {
				// The movement cost is associated with the step you are leaving
				// with the penalty added
				stairANodes[step].next.put(stairBNodes[step + r], stairA[step]
						+ crossingPenalty);
				stairBNodes[step].next.put(stairANodes[step + r], stairB[step]
						+ crossingPenalty);
			}
		}

		// Print the graph out
		if (DEBUG) {
			for (int i = 0; i < numSteps; i++) {
				System.out.print("From A" + i + " you can go to: ");
				StringBuilder sb = new StringBuilder();
				for (Node n : stairANodes[i].next.keySet()) {
					sb.append(n);
					sb.append(",");
				}
				System.out.println(sb.toString());
			}
			for (int i = 0; i < numSteps; i++) {
				System.out.print("From B" + i + " you can go to: ");
				StringBuilder sb = new StringBuilder();
				for (Node n : stairBNodes[i].next.keySet()) {
					sb.append(n);
					sb.append(",");
				}
				System.out.println(sb.toString());
			}
		}
	}

	/** Find the cheapest path */
	public void findPath() {
		Queue<Node> openSet = new PriorityQueue<>(2, new Comparator<Node>() {
			@Override
			public int compare(Node o1, Node o2) {
				return o1.heuristicDistance - o2.heuristicDistance;
			}
		});
		openSet.add(stairANodes[0]);
		openSet.add(stairBNodes[0]);
		for (Node startNode : openSet) {
			startNode.distanceFromStart = 0;
		}
		// Nodes we have already visited
		Set<Node> closedSet = new HashSet<>();

		while (!openSet.isEmpty()) {
			// Grab another node
			Node currentNode = openSet.poll();
			if (DEBUG) {
				System.out.print("Checking " + currentNode + " ("
						+ currentNode.distanceFromStart + " from start) ->");
			}
			if (currentNode == goalNode) {
				if (DEBUG) {
					System.out.println(" Found goal!");
				}
				break;
			}

			for (Entry<Node, Integer> nextEntry : currentNode.next.entrySet()) {
				// Figure out where I can go and what it'll cost
				Node next = nextEntry.getKey();
				int cost = nextEntry.getValue();
				int distanceFromStart = currentNode.distanceFromStart + cost;
				int heuristicDistance = distanceFromStart + numSteps
						- next.getStepNum();
				if (DEBUG) {
					System.out.print(" " + next + " (" + distanceFromStart
							+ "),");
				}

				// Don't save any of the calculations if we already had a more
				// efficient way to get to next
				if (closedSet.contains(next)
						|| next.distanceFromStart < distanceFromStart) {
					continue;
				}

				// Save the data: this route is more efficient than any previous
				// one
				next.distanceFromStart = distanceFromStart;
				next.heuristicDistance = heuristicDistance;

				// Remember to recursively add this node
				if (!openSet.contains(next)) {
					openSet.add(next);
				}
			}
			if (DEBUG) {
				System.out.println();
			}
			// No backtracking required in this problem because all edge weights
			// are positive
			closedSet.add(currentNode);
		}
	}

	public void output() {
		System.out.println(goalNode.distanceFromStart);
	}
}
