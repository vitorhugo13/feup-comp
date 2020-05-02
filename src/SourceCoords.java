public class SourceCoords {

    private Token token;
    
    public SourceCoords(Token token) {
        this.token = token;
    }

    public int getLine() {
        return this.token.beginLine;
    }
}