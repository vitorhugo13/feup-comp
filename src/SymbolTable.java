import java.util.Stack;
import java.util.Hashtable;

public class SymbolTable{

    private Stack tbl;

    /** Creates an empty symbol table. */
    public SymbolTable() {
        tbl = new Stack();
    }

    /** Enters a new scope. A scope must be entered before anything
     * can be added to the table.
     * */
    public void enterScope() {
        tbl.push(new Hashtable());
    }

    /** Exits the most recently entered scope. */
    public void exitScope() {
        if (tbl.empty()) {
            System.err.println("existScope: can't remove scope from an empty symbol table.");
        }
        tbl.pop();
    }
    class Info extends Descriptor{};
    /** Adds a new entry to the symbol table.
     *
     * @param id the symbol
     * @param info the data asosciated with id
     * */
    public void addId(String id, Info info) {
        if (tbl.empty()) {
            System.err.println("addId: can't add a symbol without a scope.");
        }
        ((Hashtable)tbl.peek()).put(id, info);
    }

    /**
     * Looks up an item through all scopes of the symbol table.  If
     * found it returns the associated information field, if not it
     * returns <code>null</code>.
     *
     * @param sym the symbol
     * @return the info associated with sym, or null if not found
     * */
    public Info lookup(String sym) {
        if (tbl.empty()) {
            System.err.println("lookup: no scope in symbol table.");
        }
        // I break the abstraction here a bit by knowing that stack is
        // really a vector...
        for (int i = tbl.size() - 1; i >= 0; i--) {
            Info info = ((Hashtable)tbl.elementAt(i)).get(sym);
            if (info != null) return info;
        }
        return null;
    }

    /**
     * Probes the symbol table.  Check the top scope (only) for the
     * symbol <code>sym</code>.  If found, return the information field.
     * If not return <code>null</code>.
     *
     * @param sym the symbol
     * @return the info associated with sym, or null if not found
     * */
    public Info probe(String sym) {
        if (tbl.empty()) {
            System.err.println("lookup: no scope in symbol table.");
        }
        return ((Hashtable)tbl.peek()).get(sym);
    }

    /** Gets the string representation of the symbol table.
     *
     * @return the string rep
     * */
    public String toString() {
        String res = "";
        // I break the abstraction here a bit by knowing that stack is
        // really a vector...
        for (int i = tbl.size() - 1, j = 0; i >= 0; i--, j++) {
            res += "Scope " + j + ": " + tbl.elementAt(i) + "\n";
        }
        return res;
    }

}
/*
H2()

HashT1()
HT0()

enterScope()
    class Exemplo{ insert()
    #enterScope()
        int a;insert(a,detalhes)detalhes.typeVar=int;detalhes.type=var;
        int fc(int b){enterScope()
        float c=1; insert(c, )
        exitScope()
        }
        }

}

STACK
        H3{

        }

        H2{
            a: {type: var, typeValue: int}
            fc: {type: func, typeValue: int, param:}
        }
        H1{
            Exmplo: {type: class}
        }
        */
