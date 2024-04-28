import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Main {
    private static int n;
    private static int m;
    private static int d;
    private static int[][] c;
    private static int[][] w;
    private static int[] result;
    private static int bestw = Integer.MAX_VALUE;
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        n = input.nextInt();
        m = input.nextInt();
        d = input.nextInt();
        c = new int[n+1][m+1];
        w = new  int[n+1][m+1];
        result = new int[n+1];
        for(int i=1;i<=n;i++){
            for(int j=1;j<=m;j++){
                c[i][j] = input.nextInt();
            }
        }
        for(int i=1;i<=n;i++){
            for(int j=1;j<=m;j++){
                w[i][j] = input.nextInt();
            }
        }
        bfs();
        System.out.println(bestw);
        for(int i= result.length-1;i>=1;i--){
            System.out.print(result[i]+" ");
        }
    }
    public static void bfs(){
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                return o1.weight - o2.weight;
            }
        });
        Node first = new Node();
        first.level = 0;
        priorityQueue.add(first);
        while (!priorityQueue.isEmpty()){
            Node node = priorityQueue.poll();
            if(node.level == n){
                if(node.weight<bestw){
                    Node temp = node;
                    bestw = temp.weight;
                    int i = 1;
                    while (temp.level!=0){
                        result[i] = temp.choice;
                        temp = temp.father;
                        i++;
                    }

                }
            }
            else {
                //剪枝
                if(node.weight+jianzhi(node)>bestw){
                    continue;
                }
                for(int j=1;j<=m;j++){
                    if(node.cost+c[node.level+1][j]<=d){
                        Node newNode = new Node();
                        newNode.weight = node.weight+w[node.level+1][j];
                        newNode.cost = node.cost + c[node.level+1][j];
                        newNode.level = node.level+1;
                        newNode.choice = j;
                        newNode.father = node;
                        priorityQueue.add(newNode);
                    }
                }
            }
        }
    }
    public static int jianzhi(Node node){
        int bestTotal = 0;
        int besti = Integer.MAX_VALUE;
        for(int i = node.level+1;i<=n;i++){
            for(int j = 1;j<=m;j++){
                besti = Math.min(w[i][j],besti);
            }
            bestTotal+=besti;
        }
        return bestTotal;
    }
}
class Node{
    public int choice;
    public int weight;
    public int cost;
    public Node father;
    public int level;
}