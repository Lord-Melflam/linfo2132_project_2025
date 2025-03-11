package compiler.Parser.Grammar.Declaration.Function.Node;

import compiler.Parser.AST.SpecialSymbolNode;
import compiler.Parser.Utils.Interface.ASTNode;
import compiler.Parser.Utils.Interface.ASTVisitor;

public class CallFunctionNode extends ASTNode {

  private FunctionCallNode functionCallNode;
  private SpecialSymbolNode specialSymbolNode;

  public CallFunctionNode(FunctionCallNode functionCallNode, SpecialSymbolNode specialSymbolNode) {
    this.specialSymbolNode = specialSymbolNode;
    this.functionCallNode = functionCallNode;
  }

  @Override
  public void accept(ASTVisitor visitor) {

  }
}
