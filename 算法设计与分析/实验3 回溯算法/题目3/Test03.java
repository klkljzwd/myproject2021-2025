import java.util.Scanner;

public class Test03 {
    private static int[][] p;
    private static int[][] q;
    private static int maxSum=0;
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int n = input.nextInt();
        p=new int[n][n];
        q=new int[n][n];
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                p[i][j]=input.nextInt();
            }
        }
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                q[i][j]=input.nextInt();
            }
        }
        dfs(0, n, 0);
        System.out.println(maxSum);
    }
    private static void dfs(int t,int n,int sum){
        if(t==n){
            if(sum>maxSum){
                maxSum=sum;
            }
        }
        else{
            int temp=sum;
            int flag=0;
            for(int j=0;j<n;j++){
                for(int i=t+1;i<n;i++){
                    temp=temp+p[i][j]*q[j][i];
                }
                if(temp>maxSum){
                    flag=1;
                    break;
                }
            }
            if(flag==0){
                return;
            }
            for(int i=t;i<n;i++){
                for(int j=0;j<n;j++){
                    sum+=p[i][j]*q[j][i];
                    dfs(t+1, n, sum);
                    sum-=p[i][j]*q[j][i];
                }
            }
        }
    }
}
