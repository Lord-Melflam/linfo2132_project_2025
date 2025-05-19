package compiler.Parser.ASTNode;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import compiler.Exceptions.Semantic.OperatorError;
import compiler.Parser.Utils.Interfaces.ASTNode;
import compiler.Parser.Utils.Interfaces.ASTVisitor;
import java.util.LinkedList;
import java.util.List;

@JsonTypeName("MainNode")
public class MainNode extends ASTNode {

  private String name;
  @JsonIgnore

  private List<ASTNode> children;
  @JsonIgnore
  private int line;
  @JsonIgnore
  private List<MainNode> nodes;

  public MainNode() {
    this.name = "";
    this.children = new LinkedList<>();
    this.line = -1;
  }

  public MainNode(String name, List<ASTNode> children, int line) {
    this.name = name;
    this.children = new LinkedList<>();
    this.line = line;
    for (ASTNode node : children) {
      if (node != null) {
        this.children.addLast(node);
      }
    }
  }

  public int getLine() {
    return line;
  }

  public void add(List<ASTNode> list) {
    for (ASTNode node : list) {
      if (node != null) {
        children.addLast(node);
      }
    }
  }

  public void add(ASTNode node) {
    if (node != null) {
      children.addLast(node);
    }
  }

  public MainNode(List<MainNode> children) {
    this.name = "Root";
    this.nodes = new LinkedList<>();

    for (MainNode node : children) {
      if (node != null) {
        this.nodes.addLast(node);
      }
    }
  }

  public String getName() {
    return name;
  }

  public List<ASTNode> getChildren() {
    return children;
  }

  public LinkedList<ASTNode> getChildrenList() {
    return (LinkedList<ASTNode>) children;
  }

  public List<MainNode> getNodes() {
    return nodes;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(getName()).append("\n");
    for (ASTNode child : children) {
      sb.append("  ").append(child.toString()).append("\n");
    }
    return sb.substring(0, sb.length() - 1);
  }

  @Override
  public void acceptAST(ASTVisitor visitor) {
    visitor.visit(this);
  }

  // MainNode.java
  @Override
  public <T> T accept(ASTVisitor<T> visitor) throws OperatorError {
    return visitor.visitMainNode(this);
  }

  public void printTree() {
    for (MainNode child : getNodes()) {
      printNode(child, "", true);
    }
  }

  private void printNode(ASTNode node, String prefix, boolean isLast) {
    if (node == null) {
      return;
    }

    String branch = isLast ? "└── " : "├── ";
    if (node instanceof GenericNode<?>) {
      System.out.println(
          prefix + branch + node.toString() + "line: " + ((GenericNode<?>) node).getLine());
    } else if (node instanceof MainNode mainNode) {
      System.out.println(prefix + branch + mainNode.getName() + "line: " + (mainNode).getLine());
      String newPrefix = prefix + (isLast ? "    " : "│   ");
      List<? extends ASTNode> children = mainNode.getChildren();
      for (int i = 0; i < children.size(); i++) {
        printNode(children.get(i), newPrefix, i == children.size() - 1);
      }
    }
  }


  public void copyImpl(List<ASTNode> nodesAst) {

  }

  public ASTNode deepCopy() {
    return null;
  }



 /* @Override
  public MainNode deepCopy(List<ASTNode> ignored) {
    for (ASTNode child : ignored) {
      if (child != null) {
        if (child instanceof GenericNode<?>) {

        } else {
          copiedChildren.add(child.deepCopy(copy(child)));
        }
      }
    }
    return new MainNode(this.name, copiedChildren, this.line);
  }

  private List<ASTNode> copy(ASTNode child) {
    MainNode mainNode = (MainNode) child;
    List<ASTNode> astNodes = new ArrayList<>();
    if (mainNode.getNodes() == null || mainNode.getNodes().isEmpty()) {
      return astNodes;
    }
    astNodes.addAll(mainNode.getNodes());
    return astNodes;
  }*/

}

