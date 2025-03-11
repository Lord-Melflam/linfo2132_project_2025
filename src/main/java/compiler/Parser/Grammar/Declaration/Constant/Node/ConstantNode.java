package compiler.Parser.Grammar.Declaration.Constant.Node;

import compiler.Parser.Grammar.Expression.Node.ExpressionNode;
import compiler.Parser.Utils.Interface.ASTNode;
import compiler.Parser.Utils.Interface.ASTVisitor;

public class ConstantNode extends ASTNode {

  private String keyword;
  private String identifier;
  private String typeSpecifier;
  private String assignment;
  private ExpressionNode expression;
  private String specialSymbol;

  public ConstantNode(ExpressionNode expression) {
    this.keyword = " SymbolsName.Keyword.name()";
    this.identifier = "SymbolsName.Identifier.name()";
    this.typeSpecifier = " SymbolsName.TypeSpecifier.name()";
    this.assignment = "SymbolsName.Assignment.name();";
    this.expression = expression;
    this.specialSymbol = "SymbolsName.SpecialSymbol.name()";
  }

  @Override
  public String toString() {
    return "ConstantNode{" +
        "keyword='" + keyword + '\'' +
        ", identifier='" + identifier + '\'' +
        ", typeSpecifier='" + typeSpecifier + '\'' +
        ", assignment='" + assignment + '\'' +
        ", expression=" + expression +
        ", specialSymbol='" + specialSymbol + '\'' +
        '}';
  }

  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}

