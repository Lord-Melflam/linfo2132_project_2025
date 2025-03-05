package compiler.Parser.AST;

import compiler.Parser.Utils.Interface.ASTNode;
import compiler.Parser.Utils.Interface.ASTVisitor;

public class RecordNameNode extends ASTNode {

  String name;

  public RecordNameNode(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "RecordNameNode(" + name + ")";
  }

  @Override
  public void accept(ASTVisitor visitor) {

  }
}
