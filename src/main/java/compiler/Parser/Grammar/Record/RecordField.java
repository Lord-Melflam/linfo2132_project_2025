package compiler.Parser.Grammar.Record;

import compiler.Exceptions.Lexer.UnrecognisedTokenException;
import compiler.Exceptions.Parser.ParserException;
import compiler.Parser.ASTNode.GenericNode;
import compiler.Parser.ASTNode.MainNode;
import compiler.Parser.Utils.Enum.TokenType;
import compiler.Parser.Utils.Interfaces.ASTNode;
import compiler.Parser.Utils.Position;
import compiler.Parser.Utils.Utils;
import java.util.LinkedList;

public class RecordField {

  private final Utils utils;
  private final String nodeName = "RecordField";
  private int line;
  private Position position;

  public RecordField(Utils utils, Position savedPosition) {
    this.utils = utils;
    line = utils.getLine();
    position = savedPosition;
  }

  public LinkedList<ASTNode> isRecordField() throws UnrecognisedTokenException, ParserException {
    LinkedList<ASTNode> recordFieldNodes = new LinkedList<>();

    while (utils.matchIndex(TokenType.IDENTIFIER, true)) {
      LinkedList<ASTNode> field = new LinkedList<>();
      field.add(utils.getGenericNode());

      if (!utils.matchIndex(TokenType.RECORD, true) && !utils.matchIndex(TokenType.TYPESPECIFIER,
          true)) {
        continue;
      }

      field.add(utils.getGenericNode());
      if (utils.matchIndex(TokenType.SEMICOLON, true)) {
        recordFieldNodes.addLast(new MainNode("Field", field, line));
        continue;
      }
/*      if (((GenericNode<?>) field.getLast()).getValue().contains("[]")) {
        MainNode mainNode = new MainNode("ArrayRecord", field, utils.getLine());
        field.clear();
        field.add(mainNode);

      }*/
      if (utils.matchIndex(TokenType.LITERAL, true)) {
        if (utils.getGenericNode().getValue().equals("[]")) {
          field.add(new GenericNode<>("TypeSpecifier",
              ((GenericNode<?>) field.removeLast()).getValue() + utils.getGenericNode().getValue(),
              utils.getLine()));
          recordFieldNodes.addLast(new MainNode("Field", field, line));
         /* MainNode mainNode = new MainNode("ArrayRecord", field, utils.getLine());
          field.clear();
          field.add(mainNode);*/
        }
      }

      if (!utils.matchIndex(TokenType.SEMICOLON, true)) {
        break;
      }
      //field.add(utils.getGenericNode());
      //recordFieldNodes.addLast(new MainNode("Field", field, line));
    }

    return recordFieldNodes;
  }
}
