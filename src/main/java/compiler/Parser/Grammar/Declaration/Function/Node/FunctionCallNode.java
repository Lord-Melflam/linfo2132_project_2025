package compiler.Parser.Grammar.Declaration.Function.Node;

import compiler.Parser.AST.IdentifierNode;
import compiler.Parser.AST.SpecialSymbolNode;
import compiler.Parser.Utils.Interface.ASTNode;
import compiler.Parser.Utils.Interface.ASTVisitor;

public class FunctionCallNode extends ASTNode {

  IdentifierNode identifierNode;
  SpecialSymbolNode specialSymbolNode1;
  ParameterListNode parameterListNode;
  SpecialSymbolNode specialSymbolNode2;

  public FunctionCallNode(/*IdentifierNode identifierNode,*/ SpecialSymbolNode specialSymbolNode1,
      ParameterListNode parameterListNode, SpecialSymbolNode specialSymbolNode2) {
/*
    this.identifierNode = identifierNode;
*/
    this.specialSymbolNode1 = specialSymbolNode1;
    this.parameterListNode = parameterListNode;
    this.specialSymbolNode2 = specialSymbolNode2;
  }

  @Override
  public void accept(ASTVisitor visitor) {

  }
}
