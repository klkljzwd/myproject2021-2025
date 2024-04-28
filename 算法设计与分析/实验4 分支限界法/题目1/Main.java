import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.Scanner;

public class Main{
    private static int n;
    private static int m;
    private static int[] value;
    private static int[][] e;
    private static int bestValue = Integer.MAX_VALUE;
    private static int[] bestResult;
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        n = input.nextInt();
        m = input.nextInt();
        value = new int[n+1];
        e = new int[n+1][n+1];
        bestResult = new int[n+1];
        for(int i=1;i<=n;i++){
            value[i] = input.nextInt();
        }
        for(int i=1;i<=m;i++){
            int x = input.nextInt();
            int y = input.nextInt();
            e[x][y]=1;
            e[y][x]=1;
        }
        BFS();
        System.out.println(bestValue);
        for(int i=1;i<=n;i++){
            System.out.print(bestResult[i]+" ");
        }
    }
    public static void BFS(){
        Deque<Node> deque = new ArrayDeque<>();
        Node first = new Node();
        first.level=0;
        deque.push(first);
        while (!deque.isEmpty()){
            Node node = deque.poll();
            if(node.level==n){
                int[] result = cover(node);
                int v=0;
                if(result!=null){
                    for(int i=1;i<= result.length-1;i++){
                        if(result[i]==1){
                            v+=value[i];
                        }
                    }
                    if(v<=bestValue){
                        bestValue = v;
                        bestResult = result;
                    }
                }
                continue;
            }
            //左孩子
            if(node.value+value[node.level+1]<bestValue){
                Node left = new Node();
                left.level = node.level+1;
                left.value = node.value+value[node.level+1];
                left.father = node;
                left.isChoosed = true;
                deque.push(left);
            }
            //右孩子
            Node right = new Node();
            right.isChoosed = false;
            right.father = node;
            right.level = node.level+1;
            right.value = node.value;
            deque.push(right);
        }
    }
    public static int[] cover(Node node){
        int[] cover = new int[n+1];
        Node temp = node;
        while(true){
            if(temp.level==0){
                break;
            }
            if(temp.isChoosed){
                cover[temp.level]=1;
            }
            temp = temp.father;
        }
        for(int i=1;i<=n;i++){
            if(cover[i]==0){
                int flag = 0;
                for(int j=1;j<=n;j++){
                    if(e[i][j]==1){
                        if(cover[j]==1){
                            flag=1;
                            break;
                        }
                    }
                }
                //不是一个覆盖
                if(flag==0){
                    return null;
                }
            }
        }
        //是一个覆盖
        return cover;
    }
}
class Node{
    public int value;
    public boolean isChoosed;
    public Node father;
    public int level;

    public Node() {
    }

    public Node(int value, boolean isChoosed, Node father, int level) {
        this.value = value;
        this.isChoosed = isChoosed;
        this.father = father;
        this.level = level;
    }

}