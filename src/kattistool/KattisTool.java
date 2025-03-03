package kattistool;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.TextArea;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import processing.app.Base;
import processing.app.tools.Tool;

/**
 *
 * @author dahjon
 */
public class KattisTool extends JFrame implements Tool {

    JTextArea outTextArea = new JTextArea(60, 60);
    JLabel status = new JLabel();
    Base base;

    public KattisTool() {
        KTUTils.debug("->KattisTool (constructor)");
        add(new JScrollPane(outTextArea));
        outTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        setTitle("KattisTool " + BuildTime.date + " " + BuildTime.time);
        add(status, BorderLayout.SOUTH);
        pack();
    }

    @Override
    public void init(Base base) {
        KTUTils.debug("->init");
        this.base = base;
    }

    @Override
    public void run() {
        //KTUTils.debug("->run kollar om tom");
        String code = base.getActiveEditor().getText();
        String fileName = base.getActiveEditor().getSketch().getName(); //Filename without .pde
        //debug("fileName = " + fileName);
        if (code.trim().isEmpty()) {
            int ans = JOptionPane.showConfirmDialog(base.getActiveEditor(), "Jag ser att sketchen är tom.\nVill du att jag ska fylla på \nmed mallkod för Kattisuppgifter?", "Fylla på med mall?", JOptionPane.YES_NO_OPTION);
            if (ans == JOptionPane.YES_OPTION) {
                insertTemplate();
            } else {
                KTUTils.debug("Slipp då");
            }
        } else {
            setVisible(true);
            //String fileName = "test.pde";
            code = proc2Java(fileName, code);
            //KattisTool kt = new KattisTool();
            setVisible(true);
            outTextArea.setText(code);
            boolean saved = KTUTils.saveJavaFile(fileName, code);
            if (saved) {
                KTUTils.debug("Sparad i mappen " + KTUTils.getKattisProjectDir());
                status.setText("Sparad i mappen " + KTUTils.getKattisSaveDir());
            } else {
                KTUTils.debug("Skapa projektet " + KTUTils.getKattisProjectDir() + " så sparas javafilen där");
                status.setText("Skapa projektet " + KTUTils.getKattisProjectDir() + " så sparas javafilen där");
            }
        }
    }

    private void insertTemplate() {
        String template
                = "import longinput.*;\n"
                + "import java.util.*;\n"
                + "\n"
                + "Scanner scan = new Scanner(LongInput.input());\n"
                + ""
                + "int tal = scan.nextInt(); //hämta ett heltal (Sudda rad om du inte behöver den)\n"
                + "String input1 = scan.next(); //Hämta ett ord (Sudda rad om du inte behöver den)\n"
                + "\n"
                + "//Här skriver du resten av din kod\n"
                + "\n"
                + "\n"
                + "scan.close();\n";
        base.getActiveEditor().setText(template);
    }

    @Override
    public String getMenuTitle() {
        return "Kattis Tool for processing";
    }

    static String getExtraCode(String[][] extraFuncArr, String org) {
        KTUTils.debug("org = " + org);
        String extraCode = "";
        for (int i = 0; i < extraFuncArr.length; i++) {
            String[] row = extraFuncArr[i];
            if (row != null) {
                KTUTils.debug("i: " + i + ", row:" + row[0] + ", " + row[1]);
//            if (org.contains(row[0])) {
                if (KTUTils.findFunction(row[0], org)) {
                    KTUTils.debug("Lägg till!!!!!");
                    if (!extraCode.contains(row[1])) {
                        extraCode += row[1];
                    }
                } else {
                    KTUTils.debug("Lägg inte till");
                }
            }
        }
        return extraCode;
    }

//    static String printlnCode
//            = "\n    static void println(Object... inp) {\n"
//            + "        for (int i = 0; i < inp.length; i++) {\n"
//            + "            Object object = inp[i];\n"
//            + "            System.out.print(object);\n"
//            + "        }\n"
//            + "        System.out.println();\n"
//            + "    }\n\n";
    public static void main(String[] args) {
        String code
                = "import java.util.*;\n"
                + "import longinput.*;\n"
                + "\n"
                + "//Lite kommentarer\n"
                + "void setup(){\n"
                + "  Scanner scan = new Scanner(LongInput.input());\n"
                + "  String inputStr = scan.next();\n"
                + "  int input =int(inputStr);\n"
                + "  float decinput =float(inputStr);\n"
                + "  double ddecinput =double(inputStr);\n"
                + "  if(isEven(input)){\n"
                + "    println(\"Bob\",8);\n"
                + "  }\n"
                + "  else {\n"
                + "    println(\"Alice\");\n"
                + "  }    \n"
                + "}\n"
                + "\n"
                + "\n"
                + "boolean isEven(int num){\n"
                + "  boolean retVal;\n"
                + "  retVal = num % 2 == 0;\n"
                + "  return retVal;\n"
                + "}\n"
                + ""; // TODO code application logic here
        String fileName = "TakeTwoStones.pde";
        StringBuilder sb = new StringBuilder();
        final String javaCode = proc2Java(fileName, code);

        //println(javaCode);
        KattisTool kt = new KattisTool();
        kt.setVisible(true);
        kt.outTextArea.setText(javaCode);

    }

    private static String replaceMisc(String code) {
        return KTUTils.replace(code, "[^\\w\\.]LongInput\\.input\\s*\\(\\s*\\)", "System.in");
    }

//    public static String replaceIntFunction(String code) {
//        final String funktionsNamn = "int";
//        return replaceFunction(funktionsNamn, "intConv", code);
//    }
//    private static String[] replaceFunctions(String[] rows) {
//        for (int i = 0; i < rows.length; i++) {
//            rows[i] = replaceFunctions(rows[i]);
//
//        }
//        return rows;
//    }
    private static String replaceFunctions(String[][] replaceFunc, String code) {
        for (int i = 0; i < replaceFunc.length; i++) {
            String[] row = replaceFunc[i];
            if (row != null) {
                code = KTUTils.replaceFunction(row[0], row[1], code);
            }

        }
        return code;
    }

    public static String proc2Java(String fileName, String procCode) {
        String[][] extraFuncArr = {
            {"println",
                "    static void println(Object... inp) {\n"
                + "        for (int i = 0; i < inp.length; i++) {\n"
                + "            Object object = inp[i];\n"
                + "            System.out.print(object);\n"
                + "        }\n"
                + "        System.out.println();\n"
                + "    }\n"
                + ""},
            {"print",
                "    static void print(Object... inp) {\n"
                + "        for (int i = 0; i < inp.length; i++) {\n"
                + "            Object object = inp[i];\n"
                + "            System.out.print(object);\n"
                + "        }\n"
                + "    }\n"
                + ""},
            {"int", "    public static int intConv(String tal){return Integer.parseInt(tal);}\n"
                + "    public static int intConv(double tal){ return (int) tal;}\n"
                + " "}

        };

        String[][] replaceFunc = {
            {"println", "System.out.println"},
            {"int", "intConv"},
            {"double", "Double.parseDouble"},
            {"float", "Float.parseFloat"}
        };
        String className = KTUTils.removeExtension(fileName, ".pde");
        String importString = "";
        String javaCode = "";
        boolean noSetup = true;
        boolean jOptionPaneExists = false;
        boolean scannerExists = false;
        Pattern p = Pattern.compile("\\s*void\\s+setup\\s*\\(\\s*\\)\\s*\\{.*");
        Matcher m = p.matcher(javaCode);

//        int nextLeftBracketPos = 0;
//        int nextRightBracketPos = 0;
//        while(nextLeftBracketPos != -1){
//            nextLeftBracketPos = procCode.indexOf('{');
//            String curSegment = procCode.substring(nextRightBracketPos, nextLeftBracketPos);
//        }
        procCode = replaceMisc(procCode);
        String rows[] = procCode.split("\n");
        int bracetDept = 0;
        boolean printlnWithComma = false;
        for (int i = 0; i < rows.length; i++) {
            String row = rows[i];
            if (row.matches("\\s*void\\s+setup\\s*\\(\\s*\\)\\s*\\{.*")) {
                row = "public static " + row;
                row = row.replace("setup()", "main(String[] argv)");
                noSetup = false;

            }
            if (!printlnWithComma) {
                if (row.contains("println(")) {
                    printlnWithComma = KTUTils.isCommaOutsideQuote(row);
                    if (printlnWithComma) {
                        KTUTils.debug("Komma hittad!!!");
                    }
                }
            }
            if (row.contains("Scanner")) {
                scannerExists = true;
            }
            rows[i] = row;
        }
        KTUTils.debug("printlnWithComma = " + printlnWithComma);
        if (!noSetup) {
            KTUTils.debug("setup hittad");

        } else {
            KTUTils.debug("ingen setup");
        }
        if (printlnWithComma) {
            replaceFunc[0] = null;
        } else {
            extraFuncArr[0] = null;
        }

        //rows = replaceFunctions(rows);
        String extraCode = "\n" + getExtraCode(extraFuncArr, procCode);

        if(!scannerExists){
            importString += "import java.util.*;\n";
        }
        for (int i = 0; i < rows.length; i++) {
            String row = rows[i];
            if (row.trim().startsWith("import ")) {
                if (!row.contains("longinput.") && !row.contains("import javax.swing.JOptionPane;")) {
                    importString += row + "\n";
                }
                row = null;
            }
            if (row != null) {
                if (noSetup) {
                    row = "        " + row;

                } else {
                    if (bracetDept == 0) {
                        if (!row.trim().isEmpty() && !row.contains("static ") && !row.trim().startsWith("/")) {
                            row = "static " + row;

                        }
                        if (row.contains("{")) {
                            bracetDept++;
                        }
                        if (row.contains("}")) {
                            bracetDept--;

                        }
                        row = "    " + row;
                    }
                }
            }
            if (row!=null && row.contains("JOptionPane.showInputDialog")) {
                jOptionPaneExists = true;
                row = KTUTils.replace(row,"[^\\w\\.]JOptionPane.showInputDialogs*\\(.*\\)" , "scan.nextLine()");
                if(!scannerExists){
                    row = "        Scanner scan = new Scanner(System.in);\n"+row;
                    scannerExists = true;
                }
            }

            rows[i] = row;
        }
        String mainStart = "";
        String mainEnd = "";
        if (noSetup) {
            mainStart = "\n    public static void main(String[] args){\n";
            mainEnd = "    }\n";
        }

        //rows = replaceFunctions(rows);
        KTUTils.debug("extraCode = '" + extraCode + "'");
        javaCode = KTUTils.joinAndRemoveNull(rows);
        KTUTils.debug("javaCode = " + javaCode);
        javaCode = replaceFunctions(replaceFunc, javaCode);

        javaCode = importString + "\n"
                + "public class " + className + "{\n" + mainStart + javaCode
                + "\n" + mainEnd + extraCode
                + "\n}\n";

        return javaCode;
    }

    static void println(Object... inp) {
        for (int i = 0; i < inp.length; i++) {
            Object object = inp[i];
//            KTUTils.debug(object.getClass());
            System.out.print(object);

        }
        System.out.println();
        //String[] stringArray = Arrays.copyOf(inp, inp.length, String[].class);
        //System.out.println(String.join("\n", stringArray));
        //JOptionPane.showInputDialog("");

    }

}
