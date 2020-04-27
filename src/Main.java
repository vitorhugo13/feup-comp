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
        TraverseAst traverseAst = new TraverseAst(root, symbolTable);
        traverseAst.execute(root);
        // SemanticAnalysis semanticAnalysis = new SemanticAnalysis(symbolTable);
        // System.out.println("SEMANTIC ANALYSIS");
        // semanticAnalysis.execute(root);

        // TODO: get the name of the file from the args
        // Generator codeGenerator = new Generator(symbolTable);
        // codeGenerator.generate(root, "test");
        // System.out.println("CODE GENERATION COMPLETE");
    }

}
