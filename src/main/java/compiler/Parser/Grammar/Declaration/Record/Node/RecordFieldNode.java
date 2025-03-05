package compiler.Parser.Grammar.Declaration.Record.Node;

import compiler.Parser.AST.IdentifierNode;
import compiler.Parser.AST.SpecialSymbolNode;
import compiler.Parser.AST.TypeSpecifierNode;

public class RecordFieldNode {

  private IdentifierNode identifierNode;
  private TypeSpecifierNode typeSpecifierNode;
  private SpecialSymbolNode specialSymbolNode;

  public RecordFieldNode(TypeSpecifierNode typeSpecifierNode, IdentifierNode identifierNode,
      SpecialSymbolNode specialSymbolNode) {
    this.identifierNode = identifierNode;
    this.typeSpecifierNode = typeSpecifierNode;
    this.specialSymbolNode = specialSymbolNode;
  }
}

