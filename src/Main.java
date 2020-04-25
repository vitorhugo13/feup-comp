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

        
        SymbolTable symbolTable = new SymbolTable();
        TraverseAst traverseAst= new TraverseAst(root, symbolTable);
        traverseAst.execute(root);
        symbolTable.print_all();
        
        SemanticAnalysis semanticAnalysis = new SemanticAnalysis(symbolTable);
        System.out.println("\nSEMANTIC ANALYSIS\n");
        semanticAnalysis.execute(root);
    }

}
