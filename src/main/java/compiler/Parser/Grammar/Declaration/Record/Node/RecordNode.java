package compiler.Parser.Grammar.Declaration.Record.Node;

import compiler.Parser.AST.KeywordNode;
import compiler.Parser.AST.RecordNameNode;
import compiler.Parser.AST.SpecialSymbolNode;
import compiler.Parser.Utils.Interface.ASTNode;
import compiler.Parser.Utils.Interface.ASTVisitor;
import java.util.ArrayList;
import java.util.List;

public class RecordNode extends ASTNode {

  private RecordNameNode recordNameNode;
  private SpecialSymbolNode specialSymbolNodeOpen;
  private SpecialSymbolNode specialSymbolNodeClose;

  private List<RecordFieldNode> children;
  private KeywordNode keywordNode;

  public RecordNode(RecordNameNode recordNameNode, KeywordNode keywordNode,
      SpecialSymbolNode specialSymbolNodeOpen, SpecialSymbolNode specialSymbolNodeClose,
      ArrayList<RecordFieldNode> fieldNodes) {
    this.recordNameNode = recordNameNode;
    this.specialSymbolNodeOpen = specialSymbolNodeOpen;
    this.specialSymbolNodeClose = specialSymbolNodeClose;
    this.keywordNode = keywordNode;
    this.children = fieldNodes;
  }

  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
