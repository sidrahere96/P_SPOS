import java.io.*;

public class MacroProcessor {
    static String[] MNT = new String[10];
    static String[] MDT = new String[50];
    static int mntc = 0, mdtc = 0;

    public static void main(String[] args) throws Exception {
        pass1();
        pass2();
        System.out.println("\nMacro Processing Done. Check output.asm");
    }

    static void pass1() throws Exception {
        BufferedReader br = new BufferedReader(new FileReader("input.asm"));
        BufferedWriter inter = new BufferedWriter(new FileWriter("intermediate.asm"));
        String line;
        boolean inMacro = false;

        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.equals("")) continue;

            if (line.equalsIgnoreCase("MACRO")) {
                inMacro = true;
                MNT[mntc++] = br.readLine().trim();
                MDT[mdtc++] = MNT[mntc - 1];
                continue;
            }

            if (inMacro) {
                if (line.equalsIgnoreCase("MEND")) {
                    MDT[mdtc++] = "MEND";
                    inMacro = false;
                } else MDT[mdtc++] = line;
            } else {
                inter.write(line);
                inter.newLine();
            }
        }

        br.close(); inter.close();

        System.out.println("\nPASS 1 OUTPUT:");
        System.out.println("MNT:");
        for (int i = 0; i < mntc; i++) System.out.println(i + " -> " + MNT[i]);
        System.out.println("\nMDT:");
        for (int i = 0; i < mdtc; i++) System.out.println(i + " -> " + MDT[i]);
    }

    static void pass2() throws Exception {
        BufferedReader br = new BufferedReader(new FileReader("intermediate.asm"));
        BufferedWriter out = new BufferedWriter(new FileWriter("output.asm"));
        String line;

        System.out.println("\nPASS 2 OUTPUT (Expanded Code):");

        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.equals("")) continue;

            boolean found = false;
            for (int i = 0; i < mntc; i++) {
                if (line.startsWith(MNT[i].split(" ")[0])) {
                    found = true;
                    for (int j = 0; j < mdtc; j++) {
                        if (MDT[j].equals(MNT[i])) {
                            j++;
                            while (!MDT[j].equals("MEND")) {
                                out.write(MDT[j]);
                                out.newLine();
                                System.out.println(MDT[j]);
                                j++;
                            }
                            break;
                        }
                    }
                    break;
                }
            }
            if (!found) {
                out.write(line);
                out.newLine();
                System.out.println(line);
            }
        }
        br.close(); out.close();
    }
}
