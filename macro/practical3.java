import java.io.*;
import java.util.*;

public class practical3 {
    static class Macro {
        String name;
        int mdtIndex;
        List<String> parameters = new ArrayList<>();
    }

    static List<String> MDT = new ArrayList<>();
    static Map<String, Macro> MNT = new HashMap<>();

    public static void main(String[] args) throws Exception {
        pass1();
        pass2();
        System.out.println("Macro processing completed. See 'output.asm' for expanded code.");
    }

    // ---------- PASS I ----------
    static void pass1() throws Exception {
        BufferedReader br = new BufferedReader(new FileReader("input.asm"));
        BufferedWriter interWriter = new BufferedWriter(new FileWriter("intermediate.asm"));

        String line;
        boolean inMacro = false;
        Macro currentMacro = null;

        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;

            if (line.equalsIgnoreCase("MACRO")) {
                inMacro = true;
                String header = br.readLine().trim();  // macro name and params
                currentMacro = new Macro();
                String[] parts = header.split("[ ,]+");
                currentMacro.name = parts[0];

                // collect parameters (&X, &Y)
                for (int i = 1; i < parts.length; i++) {
                    currentMacro.parameters.add(parts[i]);
                }
                currentMacro.mdtIndex = MDT.size();
                MNT.put(currentMacro.name, currentMacro);

                // store header in MDT
                MDT.add(header);
                continue;
            }

            if (inMacro) {
                if (line.equalsIgnoreCase("MEND")) {
                    MDT.add("MEND");
                    inMacro = false;
                    continue;
                }

                // replace &params with #index
                String processed = line;
                for (int i = 0; i < currentMacro.parameters.size(); i++) {
                    String formal = currentMacro.parameters.get(i);
                    processed = processed.replace(formal, "#" + (i + 1));
                }
                MDT.add(processed);
            } else {
                // normal code goes to intermediate
                interWriter.write(line);
                interWriter.newLine();
            }
        }
        // ---------- print tables after Pass-I ----------
        System.out.println("\n----- MNT (Macro Name Table) -----");
        System.out.printf("%-10s %-10s %-15s\n", "Index", "Name", "MDT Index");
        int idx = 0;
        for (Macro m : MNT.values()) {
            System.out.printf("%-10d %-10s %-15d\n", idx++, m.name, m.mdtIndex);
        }

        System.out.println("\n----- MDT (Macro Definition Table) -----");
        System.out.printf("%-10s %-30s\n", "Index", "Definition");
        for (int i = 0; i < MDT.size(); i++) {
            System.out.printf("%-10d %-30s\n", i, MDT.get(i));
        }


        br.close();
        interWriter.close();
    }
    

    // ---------- PASS II ----------
    static void pass2() throws Exception {
        BufferedReader br = new BufferedReader(new FileReader("intermediate.asm"));
        BufferedWriter out = new BufferedWriter(new FileWriter("output.asm"));

        String line;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split("[ ,]+");
            String name = parts[0];

            if (MNT.containsKey(name)) {
                Macro macro = MNT.get(name);

                // Build ALA: index-based mapping
                List<String> ALA = new ArrayList<>();
                for (int i = 1; i < parts.length; i++) {
                    ALA.add(parts[i]);  // #1 = ALA[0], #2 = ALA[1], ...
                }

                // Print ALA for this macro call
                System.out.println("\nALA for call: " + line);
                for (int i = 0; i < ALA.size(); i++) {
                    System.out.println("#" + (i+1) + " = " + ALA.get(i));
                }

                // Expand macro using MDT
                for (int i = macro.mdtIndex + 1; i < MDT.size(); i++) {
                    String mline = MDT.get(i);
                    if (mline.equalsIgnoreCase("MEND")) break;

                    String expanded = mline;
                    for (int j = 0; j < ALA.size(); j++) {
                        expanded = expanded.replace("#" + (j+1), ALA.get(j));
                    }
                    out.write(expanded);
                    out.newLine();
                }
            } else {
                // normal line
                out.write(line);
                out.newLine();
            }
        }

        br.close();
        out.close();
    }
}
