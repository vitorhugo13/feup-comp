import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


public class Main {

    /**
     * if true we have to generate code
     */
    private static boolean generateCode = true;

    /**
     * if true symbol table state must be printed
     */
    private static boolean displaySymbolTable = false;

    /**
     * if true ast must be printed
     */
    private static boolean displayAST = false;
    private static boolean optimize = false;

    /**
     * @brief Main function that controls the entire execution
     * 
     * @param args Arguments received that show what we should show the user
     * @throws ParseException If there are exceptions when running the program
     */
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

        System.out.println("\nCREATING SYMBOL TABLE");        
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
        
        System.out.println("SYMBOL TABLE CREATED\n");        
        
        System.out.println("SEMANTIC ANALYSIS");
        SemanticAnalysis semanticAnalysis = new SemanticAnalysis(symbolTable);
        try{
            semanticAnalysis.execute(root);
        }
        catch (Exception e) {

            System.err.println("[SEMANTIC ERROR]: " + e.getMessage());
            throw new ParseException("[SEMANTIC ERROR]: ");
   
        }
        
        System.out.println("FINISHED SEMANTIC ANALYSIS\n");

        symbolTable.reset();

        if (optimize) {
            System.out.println("OPTIMIZING AST");
            ConstantOptimization optimizer = new ConstantOptimization();
            optimizer.init(root);
            System.out.println(" -----=====  Optimized AST  =====----- ");
            root.dump("");
            System.out.println("FINISHED OPTIMIZING AST");
        }

        if (generateCode) {
            System.out.println("GENERATING CODE");
            Generator codeGenerator = new Generator(symbolTable);
            String filename = args[0].substring(args[0].lastIndexOf(java.io.File.separator) + 1, args[0].lastIndexOf("."));
            codeGenerator.generate(root, filename);
            System.out.println("FINISHED GENERATING CODE\n");
        }

    }
    
    /**
     * @brief Responsible for processing the options received as an argument
     * @param arg Option to see at the moment
     * @return Success or failure
     */
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

    /**
     * @brief If the user makes mistakes when passing the arguments, this menu is printed to facilitate
     */
    private static void printUsage() {
        System.out.println("Usage: java -jar comp2020-3h.jar <file>");
        System.out.println("    -t - display the AST");
        System.out.println("    -s - display the symbol table");
        System.out.println("    -c - generate code");
        System.out.println("    -o - optimizations");
    }
}
