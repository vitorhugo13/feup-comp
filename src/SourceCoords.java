public class SourceCoords {

    /**
     * @brief lets you know where the errors are
     */
    private Token token;
    

    /**
     * 
     * @brief constructor 
     */
    public SourceCoords(Token token) {
        this.token = token;
    }

    /**
     * 
     * @return line where the error occurred
     */
    public int getLine() {
        return this.token.beginLine;
    }

    /**
     * 
     * @return column where the error occurred
     */
    public int getColumn() {
        return this.token.beginColumn;
    }
}