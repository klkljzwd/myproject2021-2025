import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Main {
    private static int n;
    private static int[][] conn;
    private static int bestCost = Integer.MAX_VALUE;
    private static int[] result;
    private static int[] vis;
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        n = input.nextInt();;
        conn = new int[n+1][n+1];
        result = new int[n+1];
        vis = new int[n+1];
        for(int i=1;i<=n-1;i++){
            for(int j=i+1;j<=n;j++){
                conn[i][j] = input.nextInt();
                conn[j][i]=conn[i][j];
            }
        }

        bfs();
        System.out.println(bestCost);
        for(int i=1;i<=n;i++){
            System.out.print(result[i]+" ");
        }
    }
    public static void bfs(){
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                return o1.cost-o2.cost;
            }
        });
        Node first = new Node();
        first.level = 0;
        priorityQueue.add(first);
        while (!priorityQueue.isEmpty()){
            Node node = priorityQueue.poll();
            if(node.level==n){
                int cost = calCost(node);
                if(cost<bestCost){
                    bestCost = cost;
                    Node temp = node;
                    int s = n;
                    while (temp.level!=0){
                        result[s] = temp.choice;
                        temp = temp.father;
                        s--;
                    }
                }
            }
            else{
                if(node.cost>bestCost){
                    continue;
                }
                for(int i=1;i<=n;i++){
                    if(check(i,node)){
                        Node newNode = new Node();
                        newNode.level = node.level+1;
                        newNode.choice = i;
                        newNode.father = node;
                        priorityQueue.add(newNode);
                    }
                }
            }
        }
    }
    public static boolean check(int i,Node node){
        Node temp = node;
        while (temp.level!=0){
            if(temp.choice==i){
                return false;
            }
            temp = temp.father;
        }
        return true;
    }
    public static int calCost(Node node){
        Node temp = node;
        int[] arr = new int[n+1];
        int s = n;
        while (temp.level!=0){
            arr[s] = temp.choice;
            temp=temp.father;
            s--;
        }
        int cost = 0;
        for(int r = 2;r<=n;r++){
            for(int i=1;i<=n-r+1;i++){
                int j = i+r-1;
                cost+=conn[arr[i]][arr[j]]*(r-1);
            }
        }
        return cost;
    }
}
class Node{
    public int choice;
    public int cost;
    public int level;
    public Node father;
}