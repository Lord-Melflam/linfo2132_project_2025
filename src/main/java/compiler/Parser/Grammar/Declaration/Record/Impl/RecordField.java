package compiler.Parser.Grammar.Declaration.Record.Impl;

import compiler.Exceptions.Lexer.UnrecognisedTokenException;
import compiler.Exceptions.Parser.ParserException;
import compiler.Parser.Grammar.Declaration.Record.Node.RecordFieldNode;
import compiler.Parser.Utils.Enum.TokenType;
import compiler.Parser.Utils.Position;
import compiler.Parser.Utils.Utils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RecordField {

  private Utils utils;
  private ArrayList<RecordFieldNode> fields = new ArrayList<RecordFieldNode>();
  private Position savedPosition;
  private List<HashSet<TokenType>> expectedSymbolsRecordField = List.of(
      new HashSet<>(Set.of(TokenType.IDENTIFIER)),
      new HashSet<>(Set.of(TokenType.TYPESPECIFIER, TokenType.RECORD)),
      new HashSet<>(Set.of(TokenType.SEMICOLON))
  );

  public RecordField(Utils utils, Position savedPosition)
      throws UnrecognisedTokenException, ParserException {
    this.utils = utils;
    this.savedPosition = savedPosition;
  }

  public ArrayList<RecordFieldNode> isRecordField()
      throws UnrecognisedTokenException, ParserException {
    if (utils.lookahead_matches(savedPosition.getSavedPosition(), expectedSymbolsRecordField)) {
      /*utils.match(SymbolsName.TypeSpecifier.name());
      TypeSpecifier typeSpecifier = new TypeSpecifier(utils.getPreviousSymbol().getToken(),
          utils.getPreviousSymbol().getLine_number());
      TypeSpecifierNode typeSpecifierNode = new TypeSpecifierNode(
          typeSpecifier.typeOfTypeSpecifier());

      utils.match(SymbolsName.Identifier.name());
      IdentifierNode identifierNode = new IdentifierNode(utils.getPreviousSymbol().getToken());

      utils.match(SymbolsName.Punctuation.name());
      SpecialSymbolNode specialSymbolNode = new SpecialSymbolNode(
          utils.getPreviousSymbol().getToken());
*/
      savedPosition.add(expectedSymbolsRecordField);
      fields.add(new RecordFieldNode(null, null, null));
      isRecordField();
    }
    if (fields.isEmpty()) {
      return null;
    }
    return fields;

  }

}
