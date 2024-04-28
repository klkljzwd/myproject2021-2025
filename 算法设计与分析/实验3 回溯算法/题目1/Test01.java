import java.util.Scanner;

public class Test01 {
    private static int[] log = new int[99999];
    private static boolean isPrint = false;
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int n = input.nextInt();
        int s = input.nextInt();
        int[] arr = new int[n];
        for(int i=0;i<n;i++){
            arr[i]=input.nextInt();
        }
        dfs(0,n,s,0,arr);
        if(!isPrint){
            System.out.println("No Solution!");
        }
        
    }
    private static void dfs(int t,int n,int s,int sum,int[] arr){
        if(isPrint){
            return;
        }
        if(sum>s){
            return;
        }
        if(t==n){
            if(sum==s){
                for(int i=0;i<arr.length;i++){
                    if(log[i]==1){
                        System.out.print(arr[i]+" ");
                    }
                }
                isPrint=true;
            }
            return;
        }
        else{
            sum+=arr[t];
            log[t]=1;
            
            dfs(t+1, n, s, sum, arr);
            
            sum-=arr[t];
            log[t]=0;
            dfs(t+1, n, s, sum, arr);
        }
    }
}
