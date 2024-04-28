import java.util.*;

public class Main {
    private static int n;
    private static int m;
    private static int[] arr;
    private static int bestTime=Integer.MAX_VALUE;
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        n = input.nextInt();
        m = input.nextInt();
        arr = new int[n+1];
        for(int i=1;i<=n;i++){
            arr[i] = input.nextInt();
        }

        bfs();
        System.out.println(bestTime);
    }
    public static void bfs(){
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                return o1.time-o2.time;
            }
        });
        Node first = new Node();
        first.level = 0;
        priorityQueue.add(first);
        while (!priorityQueue.isEmpty()){
            Node node = priorityQueue.poll();
            if(node.level==n){
                if(node.time<bestTime){
                    bestTime= node.time;
                }

            }
            else {
                if(node.time>bestTime){
                    continue;
                }
                for(int i=1;i<=m;i++){
                    Node newNode = new Node();
                    newNode.level = node.level+1;
                    newNode.choice = i;
                    newNode.father = node;
                    newNode.time = calTime(newNode);
                    priorityQueue.add(newNode);
                }
            }
        }
    }
    public static int calTime(Node node){
        Node temp = node;
        Deque<Integer>[] times = new Deque[m+1];
        for(int i=1;i<=m;i++){
            times[i] = new ArrayDeque<>();
        }
        while (temp.level!=0){
            times[temp.choice].add(temp.level);
            temp = temp.father;
        }
        int cost = 0;
        for(int i=1;i<=m;i++){
            int t = 0;
            while (!times[i].isEmpty()){
                Integer x = times[i].pollLast();
                t += arr[x];
            }
            cost = Math.max(cost,t);
        }
        return cost;
    }
}
class Node{
    public int choice;
    public int level;
    public Node father;
    public int time;
}