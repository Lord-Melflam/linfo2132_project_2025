package compiler.Parser.AST;

import compiler.Parser.Utils.Interface.ASTNode;
import compiler.Parser.Utils.Interface.ASTVisitor;

public class Punctuation extends ASTNode {

  String name;

  public Punctuation(String h) {
    this.name = "=";
  }

  @Override
  public String toString() {
    return "Punctuation";
  }

  @Override
  public void accept(ASTVisitor visitor) {

  }
}