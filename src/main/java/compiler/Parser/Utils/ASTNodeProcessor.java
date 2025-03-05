package compiler.Parser.Utils;

import compiler.Parser.AST.RootNode;
import compiler.Parser.Grammar.Declaration.Constant.Node.ConstantNode;
import compiler.Parser.Grammar.Declaration.Global.Node.GlobalNode;
import compiler.Parser.Grammar.Declaration.Record.Node.RecordNode;
import compiler.Parser.Utils.Interface.ASTVisitor;

public class ASTNodeProcessor implements ASTVisitor {

  private RootNode root;

  public ASTNodeProcessor() {
    this.root = new RootNode();
  }

  public RootNode getRoot() {
    return root;
  }

  @Override
  public void visit(ConstantNode constantNode) {
    root.addNode(constantNode);
  }

  @Override
  public void visit(RecordNode recordNode) {
    root.addNode(recordNode);
  }

  @Override
  public void visit(GlobalNode globalNode) {
    root.addNode(globalNode);
  }
}

