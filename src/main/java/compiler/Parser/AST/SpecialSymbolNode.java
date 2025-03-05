package compiler.Parser.AST;

import compiler.Parser.Utils.Interface.ASTNode;
import compiler.Parser.Utils.Interface.ASTVisitor;

public class SpecialSymbolNode extends ASTNode {

  private String specialSymbol;

  public SpecialSymbolNode(String specialSymbol) {
    this.specialSymbol = specialSymbol;
  }

  @Override
  public String toString() {
    return "SpecialSymbolNode{" +
        "specialSymbol='" + specialSymbol + '\'' +
        '}';
  }

  @Override
  public void accept(ASTVisitor visitor) {

  }
}
