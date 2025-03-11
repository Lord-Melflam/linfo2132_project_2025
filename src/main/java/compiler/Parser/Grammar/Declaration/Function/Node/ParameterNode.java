package compiler.Parser.Grammar.Declaration.Function.Node;

import compiler.Parser.AST.IdentifierNode;
import compiler.Parser.AST.TypeSpecifierNode;
import compiler.Parser.Utils.Interface.ASTNode;
import compiler.Parser.Utils.Interface.ASTVisitor;

public class ParameterNode extends ASTNode {

  private IdentifierNode identifierNode;
  private TypeSpecifierNode typeSpecifierNode;

  public ParameterNode(IdentifierNode identifierNode, TypeSpecifierNode typeSpecifierNode) {
    this.identifierNode = identifierNode;
    this.typeSpecifierNode = typeSpecifierNode;
  }

  public ParameterNode(IdentifierNode identifierNode) {
    this.identifierNode = identifierNode;
  }

  @Override
  public void accept(ASTVisitor visitor) {

  }
}
