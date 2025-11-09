import java.util.Scanner;

public class PageReplacement {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of pages: ");
        int n = sc.nextInt();
        int[] pages = new int[n];
        System.out.println("Enter page reference string:");
        for (int i = 0; i < n; i++) {
            pages[i] = sc.nextInt();
        }

        System.out.print("Enter frame capacity: ");
        int capacity = sc.nextInt();

        int choice;
        do {
            System.out.println("\n==== PAGE REPLACEMENT MENU ====");
            System.out.println("1. FCFS");
            System.out.println("2. Optimal");
            System.out.println("3. LRU");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    fcfs(pages, n, capacity);
                    break;
                case 2:
                    optimal(pages, n, capacity);
                    break;
                case 3:
                    lru(pages, n, capacity);
                    break;
                case 4:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice! Try again.");
            }
        } while (choice != 4);
        sc.close();
    }

    // ---------- FCFS ----------
    static void fcfs(int[] pages, int n, int capacity) {
        int[] frame = new int[capacity];
        int index = 0, fault = 0, hit = 0;
        for (int i = 0; i < capacity; i++) frame[i] = -1;

        System.out.println("\nFCFS Page Replacement:");
        for (int i = 0; i < n; i++) {
            boolean found = false;
            for (int j = 0; j < capacity; j++) {
                if (frame[j] == pages[i]) {
                    found = true;
                    hit++;
                    break;
                }
            }
            if (!found) {
                frame[index] = pages[i];
                index = (index + 1) % capacity;
                fault++;
            }
            printFrame(frame);
        }
        System.out.println("Total Hits: " + hit + " | Faults: " + fault);
    }

    // ---------- OPTIMAL ----------
    static void optimal(int[] pages, int n, int capacity) {
        int[] frame = new int[capacity];
        int fault = 0, hit = 0;
        for (int i = 0; i < capacity; i++) frame[i] = -1;

        System.out.println("\nOptimal Page Replacement:");
        for (int i = 0; i < n; i++) {
            boolean found = false;
            for (int j = 0; j < capacity; j++) {
                if (frame[j] == pages[i]) {
                    found = true;
                    hit++;
                    break;
                }
            }
            if (!found) {
                int replace = -1, farthest = i + 1;
                for (int j = 0; j < capacity; j++) {
                    int k;
                    for (k = i + 1; k < n; k++) {
                        if (frame[j] == pages[k])
                            break;
                    }
                    if (k == n) { // not used again
                        replace = j;
                        break;
                    }
                    if (k > farthest) {
                        farthest = k;
                        replace = j;
                    }
                }
                if (replace == -1)
                    replace = 0;
                frame[replace] = pages[i];
                fault++;
            }
            printFrame(frame);
        }
        System.out.println("Total Hits: " + hit + " | Faults: " + fault);
    }

    // ---------- LRU ----------
    static void lru(int[] pages, int n, int capacity) {
        int[] frame = new int[capacity];
        int[] lastUsed = new int[capacity];
        int fault = 0, hit = 0;
        for (int i = 0; i < capacity; i++) {
            frame[i] = -1;
            lastUsed[i] = -1;
        }

        System.out.println("\nLRU Page Replacement:");
        for (int i = 0; i < n; i++) {
            boolean found = false;
            for (int j = 0; j < capacity; j++) {
                if (frame[j] == pages[i]) {
                    found = true;
                    hit++;
                    lastUsed[j] = i; // update last used time
                    break;
                }
            }
            if (!found) {
                int replace = 0, min = Integer.MAX_VALUE;
                for (int j = 0; j < capacity; j++) {
                    if (frame[j] == -1) { // empty frame
                        replace = j;
                        break;
                    }
                    if (lastUsed[j] < min) {
                        min = lastUsed[j];
                        replace = j;
                    }
                }
                frame[replace] = pages[i];
                lastUsed[replace] = i;
                fault++;
            }
            printFrame(frame);
        }
        System.out.println("Total Hits: " + hit + " | Faults: " + fault);
    }

    // ---------- Utility Function ----------
    static void printFrame(int[] frame) {
        System.out.print("Frame: ");
        for (int f : frame) {
            if (f == -1)
                System.out.print("- ");
            else
                System.out.print(f + " ");
        }
        System.out.println();
    }
}