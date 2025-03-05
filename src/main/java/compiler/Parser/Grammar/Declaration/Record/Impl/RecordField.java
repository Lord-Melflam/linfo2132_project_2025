package compiler.Parser.Grammar.Declaration.Record.Impl;

import compiler.Exceptions.Lexer.UnrecognisedTokenException;
import compiler.Exceptions.Parser.ParserException;
import compiler.Lexer.Symbols.TypeSpecifier;
import compiler.Parser.AST.IdentifierNode;
import compiler.Parser.AST.SpecialSymbolNode;
import compiler.Parser.AST.TypeSpecifierNode;
import compiler.Parser.Grammar.Declaration.Record.Node.RecordFieldNode;
import compiler.Parser.Utils.Enum.SymbolsName;
import compiler.Parser.Utils.Utils;
import java.util.ArrayList;

public class RecordField {

  private Utils utils;
  private ArrayList<RecordFieldNode> fields = new ArrayList<RecordFieldNode>();

  public RecordField(Utils utils) throws UnrecognisedTokenException, ParserException {
    this.utils = utils;
  }

  public ArrayList<RecordFieldNode> isRecordField()
      throws UnrecognisedTokenException, ParserException {
    if (utils.lookahead_is(SymbolsName.TypeSpecifier.name())) {
      utils.match(SymbolsName.TypeSpecifier.name());
      TypeSpecifier typeSpecifier = new TypeSpecifier(utils.getPreviousSymbol().getToken(),
          utils.getPreviousSymbol().getLine_number());
      TypeSpecifierNode typeSpecifierNode = new TypeSpecifierNode(
          typeSpecifier.typeOfTypeSpecifier());

      utils.match(SymbolsName.Identifier.name());
      IdentifierNode identifierNode = new IdentifierNode(utils.getPreviousSymbol().getToken());

      utils.match(SymbolsName.SpecialSymbol.name());
      SpecialSymbolNode specialSymbolNode = new SpecialSymbolNode(
          utils.getPreviousSymbol().getToken());

      fields.add(new RecordFieldNode(typeSpecifierNode, identifierNode, specialSymbolNode));
      isRecordField();
    }
    if (fields.isEmpty()) {
      return null;
    }
    return fields;
  }

}
