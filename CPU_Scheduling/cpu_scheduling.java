import java.util.Scanner;

public class cpu_scheduling {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n==== CPU SCHEDULING ALGORITHMS ====");
            System.out.println("1. First Come First Serve (FCFS)");
            System.out.println("2. Shortest Job First (SJF)");
            System.out.println("3. Round Robin (RR)");
            System.out.println("4. Priority Scheduling");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    fcfs(sc);
                    break;
                case 2:
                    sjf(sc);
                    break;
                case 3:
                    rr(sc);
                    break;
                case 4:
                    priority(sc);
                    break;
                case 5:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid Choice!");
            }
        } while (choice != 5);
        sc.close();
    }

    // -------- FCFS --------
    static void fcfs(Scanner sc) {
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();
        int[] bt = new int[n];
        int[] wt = new int[n];
        int[] tat = new int[n];

        System.out.println("Enter burst times:");
        for (int i = 0; i < n; i++) bt[i] = sc.nextInt();

        wt[0] = 0;
        for (int i = 1; i < n; i++) wt[i] = wt[i - 1] + bt[i - 1];
        for (int i = 0; i < n; i++) tat[i] = wt[i] + bt[i];

        System.out.println("\nP\tBT\tWT\tTAT");
        int totalWT = 0, totalTAT = 0;
        for (int i = 0; i < n; i++) {
            System.out.println("P" + (i + 1) + "\t" + bt[i] + "\t" + wt[i] + "\t" + tat[i]);
            totalWT += wt[i];
            totalTAT += tat[i];
        }
        System.out.println("Avg WT = " + (float) totalWT / n);
        System.out.println("Avg TAT = " + (float) totalTAT / n);
    }

    // -------- SJF --------
    static void sjf(Scanner sc) {
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();
        int[] bt = new int[n];
        int[] wt = new int[n];
        int[] tat = new int[n];
        int[] pid = new int[n];

        System.out.println("Enter burst times:");
        for (int i = 0; i < n; i++) {
            pid[i] = i + 1;
            bt[i] = sc.nextInt();
        }

        // Sort by burst time
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (bt[j] > bt[j + 1]) {
                    int temp = bt[j]; bt[j] = bt[j + 1]; bt[j + 1] = temp;
                    temp = pid[j]; pid[j] = pid[j + 1]; pid[j + 1] = temp;
                }
            }
        }

        wt[0] = 0;
        for (int i = 1; i < n; i++) wt[i] = wt[i - 1] + bt[i - 1];
        for (int i = 0; i < n; i++) tat[i] = wt[i] + bt[i];

        System.out.println("\nP\tBT\tWT\tTAT");
        int totalWT = 0, totalTAT = 0;
        for (int i = 0; i < n; i++) {
            System.out.println("P" + pid[i] + "\t" + bt[i] + "\t" + wt[i] + "\t" + tat[i]);
            totalWT += wt[i];
            totalTAT += tat[i];
        }
        System.out.println("Avg WT = " + (float) totalWT / n);
        System.out.println("Avg TAT = " + (float) totalTAT / n);
    }

    // -------- Round Robin --------
    static void rr(Scanner sc) {
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();
        int[] bt = new int[n];
        int[] rem = new int[n];
        int[] wt = new int[n];
        int[] tat = new int[n];

        System.out.println("Enter burst times:");
        for (int i = 0; i < n; i++) {
            bt[i] = sc.nextInt();
            rem[i] = bt[i];
        }

        System.out.print("Enter time quantum: ");
        int tq = sc.nextInt();

        int t = 0;
        boolean done;
        do {
            done = true;
            for (int i = 0; i < n; i++) {
                if (rem[i] > 0) {
                    done = false;
                    if (rem[i] > tq) {
                        t += tq;
                        rem[i] -= tq;
                    } else {
                        t += rem[i];
                        wt[i] = t - bt[i];
                        rem[i] = 0;
                    }
                }
            }
        } while (!done);

        for (int i = 0; i < n; i++) tat[i] = wt[i] + bt[i];

        System.out.println("\nP\tBT\tWT\tTAT");
        int totalWT = 0, totalTAT = 0;
        for (int i = 0; i < n; i++) {
            System.out.println("P" + (i + 1) + "\t" + bt[i] + "\t" + wt[i] + "\t" + tat[i]);
            totalWT += wt[i];
            totalTAT += tat[i];
        }
        System.out.println("Avg WT = " + (float) totalWT / n);
        System.out.println("Avg TAT = " + (float) totalTAT / n);
    }

    // -------- Priority Scheduling --------
    static void priority(Scanner sc) {
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();
        int[] bt = new int[n];
        int[] pr = new int[n];
        int[] wt = new int[n];
        int[] tat = new int[n];
        int[] pid = new int[n];

        System.out.println("Enter burst time and priority for each process:");
        for (int i = 0; i < n; i++) {
            pid[i] = i + 1;
            System.out.print("P" + (i + 1) + " BT: ");
            bt[i] = sc.nextInt();
            System.out.print("P" + (i + 1) + " Priority: ");
            pr[i] = sc.nextInt();
        }

        // Sort by priority (smaller number = higher priority)
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (pr[j] > pr[j + 1]) {
                    int temp = pr[j]; pr[j] = pr[j + 1]; pr[j + 1] = temp;
                    temp = bt[j]; bt[j] = bt[j + 1]; bt[j + 1] = temp;
                    temp = pid[j]; pid[j] = pid[j + 1]; pid[j + 1] = temp;
                }
            }
        }

        wt[0] = 0;
        for (int i = 1; i < n; i++) wt[i] = wt[i - 1] + bt[i - 1];
        for (int i = 0; i < n; i++) tat[i] = wt[i] + bt[i];

        System.out.println("\nP\tBT\tPR\tWT\tTAT");
        int totalWT = 0, totalTAT = 0;
        for (int i = 0; i < n; i++) {
            System.out.println("P" + pid[i] + "\t" + bt[i] + "\t" + pr[i] + "\t" + wt[i] + "\t" + tat[i]);
            totalWT += wt[i];
            totalTAT += tat[i];
        }
        System.out.println("Avg WT = " + (float) totalWT / n);
        System.out.println("Avg TAT = " + (float) totalTAT / n);
    }
}
