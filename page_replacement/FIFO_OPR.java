import java.util.*;

public class FIFO_OPR {

    // ---------- FIFO Algorithm ----------
    public static int fifoPageReplacement(int[] pages, int numFrames) {
        Set<Integer> frames = new HashSet<>();
        Queue<Integer> fifoQueue = new LinkedList<>();
        int pageFaults = 0;

        System.out.print("Gantt Chart:\n0|"); // Start of Gantt Chart

        for (int i = 0; i < pages.length; i++) {
            int page = pages[i];
            if (!frames.contains(page)) {
                pageFaults++;
                if (frames.size() == numFrames) {
                    int oldestPage = fifoQueue.poll();
                    frames.remove(oldestPage);
                }
                frames.add(page);
                fifoQueue.add(page);
            }
            // Print Gantt step
            System.out.print("P" + page + " " + (i + 1) + "|");
        }

        System.out.println(); // new line
        return pageFaults;
    }

    // ---------- OPTIMAL Algorithm ----------
    public static int optimalPageReplacement(int[] pages, int capacity) {
        List<Integer> memory = new ArrayList<>();
        int pageFaults = 0;

        System.out.print("Gantt Chart:\n0|"); // Start of Gantt Chart

        for (int i = 0; i < pages.length; i++) {
            int page = pages[i];
            if (!memory.contains(page)) {
                pageFaults++;
                if (memory.size() == capacity) {
                    int farthest = -1;
                    int indexToReplace = -1;

                    for (int j = 0; j < memory.size(); j++) {
                        int nextUse = Integer.MAX_VALUE;
                        for (int k = i + 1; k < pages.length; k++) {
                            if (pages[k] == memory.get(j)) {
                                nextUse = k;
                                break;
                            }
                        }
                        if (nextUse > farthest) {
                            farthest = nextUse;
                            indexToReplace = j;
                        }
                    }

                    memory.set(indexToReplace, page);
                } else {
                    memory.add(page);
                }
            }
            // Print Gantt step
            System.out.print("P" + page + " " + (i + 1) + "|");
        }

        System.out.println(); // new line
        return pageFaults;
    }

    // ---------- Main ----------
    public static void main(String[] args) {
        int[] pages = {7, 0, 1, 2, 0, 3, 0, 4, 2, 3, 0, 3, 2};
        int numFrames = 3;

        System.out.println("Choose Algorithm:");
        System.out.println("1. FIFO");
        System.out.println("2. Optimal");
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter your choice: ");
        int choice = sc.nextInt();

        int faults;
        if (choice == 1) {
            faults = fifoPageReplacement(pages, numFrames);
            System.out.println("Number of Page Faults using FIFO: " + faults);
        } else if (choice == 2) {
            faults = optimalPageReplacement(pages, numFrames);
            System.out.println("Number of Page Faults using Optimal: " + faults);
        } else {
            System.out.println("Invalid choice!");
        }

        sc.close();
    }
}
