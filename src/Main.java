import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


public class Main {

    private static boolean generateCode = false;
    private static boolean displaySymbolTable = false;
    private static boolean displayAST = false;
    private static boolean optimize = false;

    public static void main(String[] args) throws ParseException {

        if (args.length <= 0 || args.length > 4) {
            printUsage();
            return;
        }

        for (int i = 1; i < args.length; i++) {
            if (!parseArgs(args[i])) {
                printUsage();
                return;
            }
        }

        InputStream in = null;
        try {
            in = new FileInputStream(args[0]);
        } catch (FileNotFoundException e) {
            System.out.println(args[0] + " file not found");
            return;
        }
        Parser parser = new Parser(in);

        SimpleNode root = parser.Program();

        if (parser.errors > 0) {
            throw new ParseException("Parsing errors encountered!");
        }

        if (displayAST) {
            root.dump("");
        }

        System.out.println("\nCREATING SYMBOL TABLE\n");        
        SymbolTable symbolTable = new SymbolTable();
        TraverseAst traverseAst = new TraverseAst(root, symbolTable);
        try{
        traverseAst.execute(root);
        }
        catch (Exception e) {
            System.err.println("[SEMANTIC ERROR]: " + e.getMessage());
            throw new ParseException("[SEMANTIC ERROR]: ");
        }

        if (displaySymbolTable) {
            symbolTable.print_all();
        }
        
        System.out.println("\nSYMBOL TABLE CREATED\n\n");        
        
        System.out.println("SEMANTIC ANALYSIS\n");
        SemanticAnalysis semanticAnalysis = new SemanticAnalysis(symbolTable);
        try{
            semanticAnalysis.execute(root);
        }
        catch (Exception e) {

            System.err.println("[SEMANTIC ERROR]: " + e.getMessage());
            throw new ParseException("[SEMANTIC ERROR]: ");
   
        }
        
        System.out.println("\nFINISHED SEMANTIC ANALYSIS\n");

        symbolTable.reset();

        if (optimize) {
            ConstantOptimization optimizer = new ConstantOptimization();
            optimizer.init(root);
            System.out.println(" -----=====  Optimized AST  =====----- ");
            root.dump("");
        }

        if (generateCode) {
            Generator codeGenerator = new Generator(symbolTable);
            String filename = args[0].substring(args[0].lastIndexOf("/") + 1, args[0].lastIndexOf("."));
            codeGenerator.generate(root, filename);
            System.out.println("CODE GENERATION COMPLETE");
        }

    }

    private static boolean parseArgs(String arg) {
        
        if (arg.equals("-c")) {
            if (generateCode)
            return false;
            generateCode = true;
        }
        else if (arg.equals("-t")) {
            if (displayAST)
            return false;
            displayAST = true;
        }
        else if (arg.equals("-s")) {
            if (displaySymbolTable)
            return false;
            displaySymbolTable = true;
        }
        else if (arg.equals("-o")) {
            if (optimize)
                return false;
            optimize = true;
        }
        
        return true;
    }

    private static void printUsage() {
        System.out.println("Usage: java -jar comp2020-3h.jar <file>");
        System.out.println("    -t - display the AST");
        System.out.println("    -s - display the symbol table");
        System.out.println("    -c - generate code");
        System.out.println("    -o - optimizations");
    }
}
