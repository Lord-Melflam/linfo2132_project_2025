package compiler.Parser.AST;

import compiler.Parser.Utils.Interface.ASTNode;
import java.util.ArrayList;
import java.util.List;

public class RootNode {

  private List<ASTNode> nodes;

  public RootNode() {
    nodes = new ArrayList<>();
  }

  public void addNode(ASTNode node) {
    nodes.add(node);
  }

  public List<ASTNode> getNodes() {
    return nodes;
  }

  public void printNodes() {
    for (ASTNode node : nodes) {
      System.out.println(node);
    }
  }
}

