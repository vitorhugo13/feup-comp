/* Generated By:JJTree: Do not edit this line. SimpleNode.java Version 6.1 */
/* JavaCCOptions:MULTI=false,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
public
class SimpleNode implements Node {

  protected Node parent;
  protected Node[] children;
  protected int id;
  protected Object value;
  protected Parser parser;


  private SourceCoords coords;
  public void setCoords(SourceCoords coords) { this.coords = coords; }
  public SourceCoords getCoords() { return this.coords; }


  public SimpleNode(int i) {
    id = i;
  }

  public SimpleNode(Parser p, int i) {
    this(i);
    parser = p;
  }

  public SimpleNode(int id, Node parent) {
    this.id = id;
    this.parent = parent;
  }

  public SimpleNode(int id, Object value, Node parent) {
    this.id = id;
    this.value = value;
    this.parent = parent;
  }

  // FIXME: this might be troublesome
  public SimpleNode(SimpleNode other) {
    this.id = other.id;
    this.parent = null;
    try {
      if (other.value != null)
        this.value = other.value;
    } catch (Exception e) {
      e.printStackTrace();
    }

    if (other.jjtGetNumChildren() != 0) {
      this.children = new Node[other.children.length];
      for (int i = 0; i < other.children.length; i++)
        this.children[i] = new SimpleNode((SimpleNode) other.children[i]);
    }
  }

  public void jjtOpen() {
  }

  public void jjtClose() {
  }

  public void jjtSetParent(Node n) { parent = n; }
  public Node jjtGetParent() { return parent; }

  public void jjtAddChild(Node n, int i) {
    if (children == null) {
      children = new Node[i + 1];
    } else if (i >= children.length) {
      Node c[] = new Node[i + 1];
      System.arraycopy(children, 0, c, 0, children.length);
      children = c;
    }
    children[i] = n;
  }

  public void jjtAddChildAt(Node n, int i) {
    // TODO: add error conditions
    Node c[] = new Node[children.length + 1];
    System.arraycopy(children, 0, c, 0, i);
    System.arraycopy(children, i, c, i + 1, children.length - i);
    c[i] = n;
    children = c;
  }

  public void jjtAppendChild(Node n) {
    if (children == null) {
      children = new Node[1];
      children[0] = n;
    }
    else {
      Node c[] = new Node[children.length + 1];
      System.arraycopy(children, 0, c, 0, children.length);
      c[children.length] = n;
      children = c;
    }
  }

  public Node[] jjtGetChildren() {
    return this.children;
  }

  public void jjtSetChildren(Node[] children) {
    this.children = children;
  }

  public void jjtRemoveChild(int i) {
    // TODO: add error conditions
    Node c[] = new Node[children.length - 1];
    System.arraycopy(children, i + 1, children, i, children.length - 1 - i);
    System.arraycopy(children, 0, c, 0, children.length - 1);
    children = c;
  }

  public void jjtRemoveChildren() {
    children = null;
  }

  public Node jjtGetChild(int i) {
    return children[i];
  }

  public int jjtGetNumChildren() {
    return (children == null) ? 0 : children.length;
  }

  public void jjtSetValue(Object value) { this.value = value; }
  public Object jjtGetValue() { return value; }

  public String jjtGetName() {
    return ParserTreeConstants.jjtNodeName[id];
  }

  /* You can override these two methods in subclasses of SimpleNode to
     customize the way the node appears when the tree is dumped.  If
     your output uses more than one line you should override
     toString(String), otherwise overriding toString() is probably all
     you need to do. */

  public String toString() {
    switch (ParserTreeConstants.jjtNodeName[id]) {
      case "Type": case "Class": case "Method": case "Identifier": case "Integer": case "Boolean": case "Extends":
        return ParserTreeConstants.jjtNodeName[id] + "[" + this.value + "]";
      default:
        return ParserTreeConstants.jjtNodeName[id];
    }
  }
  public String toString(String prefix) { return prefix + toString(); }

  /* Override this method if you want to customize how the node dumps
     out its children. */

  public void dump(String prefix) {
    System.out.println(toString(prefix));
    if (children != null) {
      for (int i = 0; i < children.length; ++i) {
        SimpleNode n = (SimpleNode)children[i];
        if (n != null) {
          n.dump(prefix + "  ");
        }
      }
    }
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
}

/* JavaCC - OriginalChecksum=c6a53a7e2b2b8ce930bd6fa4f9a48784 (do not edit this line) */
