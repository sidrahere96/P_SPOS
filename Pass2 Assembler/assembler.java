import java.util.*;

public class assembler {
    static String[] op = {"START", "MOVER", "MOVEM", "ADD", "SUB", "MULT", "DIV", "END"};
    static String[] reg = {"AREG", "BREG", "CREG", "DREG"};
    static String[] symbol = new String[10];
    static int[] addr = new int[10];
    static int symcount = 0;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String[][] code = {
            {"START", "100", "", ""},
            {"MOVER", "AREG", "A", ""},
            {"ADD", "BREG", "B", ""},
            {"MOVEM", "AREG", "RESULT", ""},
            {"END", "", "", ""}
        };

        System.out.println("=== INPUT CODE ===");
        for (String[] line : code)
            System.out.println(Arrays.toString(line));

        int lc = 0;
        System.out.println("\n=== PASS-I ===");
        for (int i = 0; i < code.length; i++) {
            if (code[i][0].equals("START"))
                lc = Integer.parseInt(code[i][1]);
            else if (code[i][0].equals("END"))
                break;
            else {
                // check for symbols
                String sym = code[i][2];
                if (!sym.equals("") && !isSymbolPresent(sym)) {
                    symbol[symcount] = sym;
                    addr[symcount] = lc + 1;
                    symcount++;
                }
                lc++;
            }
        }

        System.out.println("Symbol Table:");
        for (int i = 0; i < symcount; i++)
            System.out.println(symbol[i] + " -> " + addr[i]);

        System.out.println("\n=== PASS-II ===");
        for (int i = 0; i < code.length; i++) {
            String mnemonic = code[i][0];
            if (mnemonic.equals("START") || mnemonic.equals("END")) continue;

            int opcode = getOpcode(mnemonic);
            int regcode = getRegCode(code[i][1]);
            int symaddr = getSymbolAddr(code[i][2]);

            System.out.println("Machine Code: (" + opcode + ") (" + regcode + ") " + symaddr);
        }
        sc.close();
    }

    static boolean isSymbolPresent(String sym) {
        for (int i = 0; i < symcount; i++)
            if (symbol[i].equals(sym)) return true;
        return false;
    }

    static int getOpcode(String opx) {
        for (int i = 0; i < op.length; i++)
            if (opx.equals(op[i])) return i + 1;
        return 0;
    }

    static int getRegCode(String regx) {
        for (int i = 0; i < reg.length; i++)
            if (regx.equals(reg[i])) return i + 1;
        return 0;
    }

    static int getSymbolAddr(String sym) {
        for (int i = 0; i < symcount; i++)
            if (symbol[i].equals(sym)) return addr[i];
        return 0;
    }
}