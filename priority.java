import java.util.*;

public class priority
{
    public static void main(String args[])
    {
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter no of processes: ");
        int n=sc.nextInt();
        int pid[]=new int[n];       //id
        int at[]=new int[n];        //arrival time
        int bt[]=new int[n];        //burst time
        int priority[]=new int[n];  //priority
        int et[]=new int[n];        //end time
        int tat[]=new int[n];       //turn around time
        int wt[]=new int[n];        //waiting time
        int temp;
        float avg_wt=0,avg_tat=0;

        for(int i=0;i<n;i++)
        {
            System.out.println("Enter arrival time for process "+(i+1)+": ");
            at[i]=sc.nextInt();
            System.out.println("Enter burst time for process "+(i+1)+": ");
            bt[i]=sc.nextInt();
            System.out.println("Enter priority of process "+(i+1)+": ");
            priority[i]=sc.nextInt();

            pid[i]=i+1;
        }

        // bubble sorting according priority
        for(int i=0;i<n;i++)
        {
            for(int j=0;j<n-(i+1);j++)        // OR j = i + 1 with j < n
            //By excluding the last 'i + 1' elements in each iteration, the inner loop avoids unnecessary comparisons for elements that are already in their correct positions.
            {
                if(priority[j]>priority[j+1])
                {
                    temp=at[j];
                    at[j]=at[j+1];
                    at[j+1]=temp;

                    temp=bt[j];
                    bt[j]=bt[j+1];
                    bt[j+1]=temp;

                    temp=pid[j];
                    pid[j]=pid[j+1];
                    pid[j+1]=temp;

                    temp=priority[j];
                    priority[j]=priority[j+1];
                    priority[j+1]=temp;
                }
            }
        }

        for(int i=0;i<n;i++)
        {
            // first calculate end time
            if(i==0){
                et[i]=at[i]+bt[i];
            }
            else{
                if(at[i]>et[i-1]){
                    et[i]=at[i]+bt[i];
                }
                else{
                    et[i]=et[i-1]+bt[i];
                }
            }

            tat[i]=et[i]-at[i];     // calculate turn around time
            wt[i]=tat[i]-bt[i];     // calculate waiting time

            avg_tat+=tat[i];        // calculate avg turn around time
            avg_wt+=wt[i];          // calculate avg waiting time
        }

        System.out.println("PID Arrival   Burst  priority   End  TrnAround  Waiting");
        for(int i=0;i<n;i++)
        {
            System.out.println(pid[i]+"\t"+at[i]+"\t"+bt[i]+"\t"+priority[i]+"\t"+et[i]+"\t"+tat[i]+"\t"+wt[i]);
        }
        sc.close();

        System.out.println("Avg TurnAroundTime: "+(avg_tat/n));
        System.out.println("Avg WaitingTime: "+(avg_wt/n));

    }
}


