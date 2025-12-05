public class TowerOfHanoiDFS {

   
    public static void solveHanoi(int n, char source, char auxiliary, char destination) {
        if (n == 1) {
            System.out.println("Move disk 1 from " + source + " to " + destination);
            return;
        }

       
        solveHanoi(n - 1, source, destination, auxiliary);

        System.out.println("Move disk " + n + " from " + source + " to " + destination);

        solveHanoi(n - 1, auxiliary, source, destination);
    }

    public static void main(String[] args) {
        int n = 3; 
        System.out.println("Tower of Hanoi solution using DFS for " + n + " disks:");
        solveHanoi(n, 'A', 'B', 'C'); // A=source, B=auxiliary, C=destination
    }
}
