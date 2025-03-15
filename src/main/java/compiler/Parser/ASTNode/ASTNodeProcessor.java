package compiler.Parser.ASTNode;/*
package compiler.Parser.Utils;

import compiler.Parser.AST.MainNode;
import compiler.Parser.AST.RootNode;
import compiler.Parser.Utils.Interface.ASTNode;
import compiler.Parser.Utils.Interface.ASTVisitor;

public class ASTNodeProcessor implements ASTVisitor {

  private RootNode root;

  public ASTNodeProcessor() {
    this.root = new RootNode();
  }

  public RootNode getRoot() {
    return root;
  }


  public void printTree(String prefix) {
    for (MainNode child : root.getNodes()) {
      if (child != null) {
        System.out.println(child.getName());
        for (ASTNode child2 : child.getChildren()) {
          if (child2 != null) {
            System.out.println(" " + child2.toString());
          }
        }
      }
    }
  }

  @Override
  public void visit(MainNode constantNode) {
    root.addNode(constantNode);
  }

*/
/*

  @Override
  public void visit(MainNode recordNode) {
    root.addNode(recordNode);
  }

  @Override
  public void visit(MainNode functionNode) {
    root.addNode(functionNode);
  }

  @Override
  public void visit(MainNode statementNode) {
    root.addNode(statementNode);
  }
*//*


}

*/

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import compiler.Parser.Utils.Interfaces.ASTNode;
import compiler.Parser.Utils.Interfaces.ASTVisitor;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ASTNodeProcessor implements ASTVisitor {

  @JsonProperty("root")
  private RootNode root;

  public ASTNodeProcessor() {
    this.root = new RootNode();
  }

  public RootNode getRoot() {
    return root;
  }

  public void printTree(String prefix) {
    for (MainNode child : root.getNodes()) {
      if (child != null) {
        System.out.println(child.getName());
        for (ASTNode child2 : child.getChildren()) {
          if (child2 != null) {
            System.out.println(" " + child2.toString());
          }
        }
      }
    }
  }

  @Override
  public void visit(MainNode constantNode) {
    root.addNode(constantNode);
  }
}
