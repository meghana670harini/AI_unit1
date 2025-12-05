import java.util.*;

/**
 * A* solution to Tower of Hanoi.
 * Disks are indexed 0..n-1 where 0 is the smallest and n-1 is the largest.
 * Pegs are 0, 1, 2. Goal: all disks on peg 2.
 */
public class HanoiAStar {

    static class State {
        final int[] pos; // pos[i] = peg of disk i
        final int n;

        State(int[] pos) {
            this.pos = pos;
            this.n = pos.length;
        }

       
        String key() {
            StringBuilder sb = new StringBuilder(n * 2);
            for (int i = 0; i < n; i++) {
                sb.append((char) ('0' + pos[i]));
            }
            return sb.toString();
        }

       
        boolean isGoal() {
            for (int p : pos) {
                if (p != 2)
                    return false;
            }
            return true;
        }

        
        boolean isTop(int d) {
            int peg = pos[d];
            for (int i = 0; i < d; i++) { 
                if (pos[i] == peg)
                    return false;
            }
            return true;
        }


        int topOnPeg(int peg) {
            for (int i = 0; i < n; i++) {
                if (pos[i] == peg)
                    return i; 
            }
            return -1;
        }

       
        List<Neighbor> neighbors() {
            List<Neighbor> out = new ArrayList<>();
            for (int d = 0; d < n; d++) {
                if (!isTop(d))
                    continue;
                int from = pos[d];
                for (int to = 0; to < 3; to++) {
                    if (to == from)
                        continue;
                    int topTarget = topOnPeg(to);
                   
                    if (topTarget == -1 || topTarget > d) {
                        int[] next = Arrays.copyOf(pos, n);
                        next[d] = to;
                        String move = "Move disk " + (d + 1) + " from " + from + " to " + to;
                        out.add(new Neighbor(new State(next), move));
                    }
                }
            }
            return out;
        }
    }

    static class Neighbor {
        final State state;
        final String move;

        Neighbor(State s, String m) {
            this.state = s;
            this.move = m;
        }
    }

    
    static int heuristic(State s) {
        int count = 0;
        for (int p : s.pos) {
            if (p != 2)
                count++;
        }
        return count;
    }

    static class Node {
        final State state;
        final int g; 
        final int h; 
        final String move; 
        final String parent; 
        Node(State state, int g, int h, String move, String parent) {
            this.state = state;
            this.g = g;
            this.h = h;
            this.move = move;
            this.parent = parent;
        }

        int f() {
            return g + h;
        }
    }

    public static List<String> aStarSolve(int n) {
        int[] startPos = new int[n];
        Arrays.fill(startPos, 0);
        State start = new State(startPos);

        PriorityQueue<Node> open = new PriorityQueue<>(
                Comparator.<Node>comparingInt(Node::f)
                        .thenComparing(n1 -> n1.state.key()));
        Map<String, Integer> gScore = new HashMap<>();
        Map<String, Node> cameFrom = new HashMap<>();

        Node startNode = new Node(start, 0, heuristic(start), null, null);
        open.add(startNode);
        gScore.put(start.key(), 0);
        cameFrom.put(start.key(), startNode);

        Set<String> closed = new HashSet<>();

        while (!open.isEmpty()) {
            Node current = open.poll();
            String ck = current.state.key();

            if (current.state.isGoal()) {
                return reconstructPath(cameFrom, ck);
            }

            if (closed.contains(ck))
                continue;
            closed.add(ck);

            for (Neighbor nb : current.state.neighbors()) {
                String nk = nb.state.key();
                int tentativeG = current.g + 1;

                if (closed.contains(nk))
                    continue;

                Integer knownG = gScore.get(nk);
                if (knownG == null || tentativeG < knownG) {
                    int h = heuristic(nb.state);
                    Node nextNode = new Node(nb.state, tentativeG, h, nb.move, ck);
                    gScore.put(nk, tentativeG);
                    cameFrom.put(nk, nextNode);
                    open.add(nextNode);
                }
            }
        }

      
        return Collections.emptyList();
    }

    private static List<String> reconstructPath(Map<String, Node> cameFrom, String goalKey) {
        List<String> moves = new ArrayList<>();
        String cur = goalKey;
        while (true) {
            Node node = cameFrom.get(cur);
            if (node == null || node.parent == null)
                break;
            moves.add(node.move);
            cur = node.parent;
        }
        Collections.reverse(moves);
        return moves;
    }

    public static void main(String[] args) {
        int n = 3; 
        List<String> moves = aStarSolve(n);
        System.out.println("Solution found in " + moves.size() + " moves:");
        for (String m : moves) {
            System.out.println(m);
        }
    }
}
