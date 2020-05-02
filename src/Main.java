import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


public class Main {

    public static void main(String[] args) throws ParseException {

        // TODO: create a flag system:
            // -t -> display the AST
            // -c -> generate code

        if (args.length <= 0 || args.length > 2) {
            System.out.println("Usage: java -jar comp2020-3h.jar <file> -c");
            return;
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

        root.dump("");

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
        symbolTable.print_all();
        System.out.println("\nSYMBOL TABLE CREATED\n\n");        
        
        System.out.println("SEMANTIC ANALYSIS\n");
        SemanticAnalysis semanticAnalysis = new SemanticAnalysis(symbolTable);
        try{
            semanticAnalysis.execute(root);
        }
        catch (Exception e) {
            System.err.println("[SEMANTIC ERROR]: " + e.getMessage());
            throw new ParseException("[SEMANTIC ERROR]: ");
            //e.printStackTrace();
            /*
            exceptionCounter++;
            if (exceptionCounter >= MAX_EXCEPTIONS) {
                System.err.println("[PROGRAM TERMINATING] THERE ARE MORE THAN " + MAX_EXCEPTIONS + " SEMANTIC ERRORS.");
                System.exit(0);
            }
            */
            // throw new ParseException("Parse exception");
        }
        
        System.out.println("\nFINISHED SEMANTIC ANALYSIS\n");

        symbolTable.reset();

        if (args.length == 2 && args[1].equals("-c")) {
            Generator codeGenerator = new Generator(symbolTable);
            String filename = args[0].substring(args[0].lastIndexOf("/") + 1, args[0].lastIndexOf("."));
            codeGenerator.generate(root, filename);
            System.out.println("CODE GENERATION COMPLETE");
        }

    }
}
