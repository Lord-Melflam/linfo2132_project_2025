package compiler.Parser.ASTNode;

import java.util.ArrayList;
import java.util.List;

public class RootNode {

  private final List<MainNode> nodes;

  public RootNode() {
    nodes = new ArrayList<>();
  }

  public void addNode(MainNode node) {
    nodes.add(node);
  }


  public List<MainNode> getNodes() {
    return nodes;
  }

  public void printTree(String prefix) {
    for (MainNode child : nodes) {
      System.out.println(prefix + child.toString());
    }
  }
}

