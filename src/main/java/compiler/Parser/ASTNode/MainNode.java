package compiler.Parser.ASTNode;

import com.fasterxml.jackson.annotation.JsonTypeName;
import compiler.Parser.Utils.Interfaces.ASTNode;
import compiler.Parser.Utils.Interfaces.ASTVisitor;
import java.util.LinkedList;
import java.util.List;

@JsonTypeName("MainNode")

public class MainNode extends ASTNode {

  private String name;
  private List<ASTNode> children;

  public MainNode() {
    this.name = "";
    this.children = new LinkedList<>();
  }

  public MainNode(String name, List<ASTNode> children) {
    this.name = name;
    this.children = new LinkedList<>();

    for (ASTNode node : children) {
      if (node != null) {
        this.children.addLast(node);
      }
    }
  }

  public String getName() {
    return name;
  }

  public List<ASTNode> getChildren() {
    return children;
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
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}

