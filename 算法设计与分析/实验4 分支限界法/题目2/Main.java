import java.util.*;

public class Main {
    private static int n;
    private static int m;
    private static int[][] graph;
    private static int[] vis;
    private static List<Integer[]> result;
    private static int best=0;
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        n = input.nextInt();
        m = input.nextInt();
        graph = new int[n+1][n+1];
        vis = new int[n+1];
        result = new ArrayList<>();
        for(int i=1;i<=m;i++){
            int x = input.nextInt();
            int y = input.nextInt();
            graph[x][y] = 1;
            graph[y][x] = 1;
        }
        bfs();
        System.out.println(best);

        for(Integer[] arr:result){
            if(arr[0]==best){
                for(int i=1;i<=n;i++){
                    System.out.print(arr[i]+" ");
                }
                System.out.println();
            }
        }
    }
    public static void bfs(){
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                return o2.num-o1.num;
            }
        });
        Node first = new Node();
        first.num = Integer.MAX_VALUE;
        priorityQueue.add(first);
        while (!priorityQueue.isEmpty()){
            Node node = priorityQueue.poll();
            if(node.level == n){
                int[] re = cal(node);
                int value = re[0];
                if(value>=best){
                    best = value;
                    Integer[] temp = new Integer[n+1];
                    for(int i=2;i<=n+1;i++){
                        temp[i-1] = re[i];
                    }
                    temp[0]=best;
                    result.add(temp);
                }
            }
            else {
                int[] arr = cal(node);
                if(arr[0]+m-arr[0]-arr[1]>best){
                    Node left = new Node();
                    left.isChoosed = true;
                    left.father = node;
                    left.level = node.level+1;
                    left.num = cal(left)[0];
                    priorityQueue.add(left);
                }
                Node right = new Node();
                right.isChoosed = false;
                right.father = node;
                right.level = node.level+1;
                right.num = node.num;
                priorityQueue.add(right);
            }
        }
    }
    public static int[] cal(Node node){
        int[] log = new int[n+1];
        Node temp = node;

        while (true){
            if(temp.level==0){
                break;
            }
            if(temp.isChoosed){
                log[temp.level] = 1;
            }
            temp = temp.father;
        }

        int ge = 0;
        int inner = 0;
        for(int i=1;i<=log.length-1;i++){
            if(log[i]==1){
                for(int j = 1;j<=n;j++){
                    if(log[j]!=1 && graph[i][j]==1){
                        ge+=1;
                    }
                    else if(graph[i][j]==1 && log[j]==1){
                        inner+=1;
                    }
                }
            }
        }

        int[] arr = new int[2+n];
        arr[0] = ge;
        arr[1] = inner;
        for(int i=2;i<=n+1;i++){
            arr[i] = log[i-1];
        }
        return arr;
    }
}
class Node{
    public int level;
    public int num;
    public boolean isChoosed;
    public Node father;
}
