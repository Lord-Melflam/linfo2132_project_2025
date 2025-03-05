package compiler.Parser.Utils.Interface;

import compiler.Parser.Grammar.Declaration.Constant.Node.ConstantNode;
import compiler.Parser.Grammar.Declaration.Global.Node.GlobalNode;
import compiler.Parser.Grammar.Declaration.Record.Node.RecordNode;

public interface ASTVisitor {

  void visit(ConstantNode constantNode);

  void visit(RecordNode recordNode);

  void visit(GlobalNode globalNode);
}
