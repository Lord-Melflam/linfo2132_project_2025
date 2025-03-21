package compiler.Parser.Grammar.Record;

import compiler.Exceptions.Lexer.UnrecognisedTokenException;
import compiler.Exceptions.Parser.ParserException;
import compiler.Parser.ASTNode.MainNode;
import compiler.Parser.Utils.Enum.TokenType;
import compiler.Parser.Utils.Interfaces.ASTNode;
import compiler.Parser.Utils.Position;
import compiler.Parser.Utils.Utils;
import java.util.LinkedList;

public class RecordField {

  private final Utils utils;
  private final String nodeName = "RecordField";

  public RecordField(Utils utils, Position savedPosition) {
    this.utils = utils;
  }

  public LinkedList<ASTNode> isRecordField() throws UnrecognisedTokenException, ParserException {
    LinkedList<ASTNode> recordFieldNodes = new LinkedList<>();

    while (utils.matchIndex(TokenType.IDENTIFIER, true)) {
      LinkedList<ASTNode> field = new LinkedList<>();
      field.add(utils.getGenericNode());

      if (!utils.matchIndex(TokenType.RECORD, true) && !utils.matchIndex(TokenType.TYPESPECIFIER,
          true)) {
        break;
      }
      field.add(utils.getGenericNode());

      if (!utils.matchIndex(TokenType.SEMICOLON, true)) {
        break;
      }
      field.add(utils.getGenericNode());
      recordFieldNodes.addLast(new MainNode("Field", field));
    }

    return recordFieldNodes;
  }
}
