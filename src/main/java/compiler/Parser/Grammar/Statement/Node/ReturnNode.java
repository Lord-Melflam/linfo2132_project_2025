package compiler.Parser.Grammar.Statement.Node;

import compiler.Parser.AST.KeywordNode;
import compiler.Parser.AST.SpecialSymbolNode;
import compiler.Parser.Grammar.Expression.Node.ExpressionNode;
import compiler.Parser.Utils.Interface.ASTNode;
import compiler.Parser.Utils.Interface.ASTVisitor;

public class ReturnNode extends ASTNode {

  private KeywordNode keywordNode;
  private ExpressionNode expressionNode;
  private SpecialSymbolNode specialSymbolNode;

  public ReturnNode(KeywordNode keywordNode, ExpressionNode expressionNode,
      SpecialSymbolNode specialSymbolNode) {
    this.keywordNode = keywordNode;
    this.expressionNode = expressionNode;
    this.specialSymbolNode = specialSymbolNode;
  }

  @Override
  public void accept(ASTVisitor visitor) {
    
  }
}
