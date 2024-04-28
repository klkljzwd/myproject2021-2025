import java.util.*;
public class BestWork {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int n = input.nextInt();
        int[] a = new int[n+1];
        int[] b = new int[n+1];
        for(int i=1;i<=n;i++){
            a[i]=input.nextInt();
        }
        for(int i=1;i<=n;i++){
            b[i]=input.nextInt();
        }
        System.out.println(findMin(a, b, n));
    }
    private static int findMin(int[] a,int[] b,int n){
        int[][] dp = new int[1000][1000];
        int aTime = 0;
        for(int i=1;i<=n;i++){
            aTime+=a[i];
            for(int j=0;j<=aTime;j++){
                if(j<a[i]){
                    dp[j][i] = dp[j][i-1]+b[i];
                }
                else{
                    dp[j][i] = Math.min(dp[j-a[i]][i-1],dp[j][i-1]+b[i]);
                }
            }
        }
        int minTime = Integer.MAX_VALUE;
        for(int i=0;i<aTime;i++){
            int maxt = Math.max(i, dp[i][n]);
            minTime=Math.min(minTime, maxt);
        }
        return minTime;
    }
}
