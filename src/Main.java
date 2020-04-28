import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


public class Main {

    public static void main(String[] args) throws ParseException {

        InputStream in = null;
        try {
            in = new FileInputStream(args[0]);
        } catch (FileNotFoundException e) {
            System.out.println(args[0] + " file not found");
            return;
        }
        Parser parser = new Parser(in);

        // parser.Program();
        SimpleNode root = parser.Program();

        if (parser.errors > 0) {
            throw new ParseException("Parsing errors encountered!");
        }

        root.dump("");

        System.out.println("\nCREATING SYMBOL TABLE\n");        
        SymbolTable symbolTable = new SymbolTable();
        TraverseAst traverseAst= new TraverseAst(root, symbolTable);
        traverseAst.execute(root);
        symbolTable.print_all();
        System.out.println("SYMBOL TABLE CREATED");        
        
        System.out.println("SEMANTIC ANALYSIS");
        SemanticAnalysis semanticAnalysis = new SemanticAnalysis(symbolTable);
        semanticAnalysis.execute(root);
        System.out.println("FINISHED SEMANTIC ANALYSIS");

    }

}
