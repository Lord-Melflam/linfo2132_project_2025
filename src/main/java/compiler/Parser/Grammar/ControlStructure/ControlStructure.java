package compiler.Parser.Grammar.ControlStructure;

import compiler.Exceptions.Lexer.UnrecognisedTokenException;
import compiler.Exceptions.Parser.ParserException;
import compiler.Parser.ASTNode.MainNode;
import compiler.Parser.Grammar.Expression.Expression;
import compiler.Parser.Grammar.Statement.StatementList;
import compiler.Parser.Utils.Enum.TokenType;
import compiler.Parser.Utils.Interfaces.ASTNode;
import compiler.Parser.Utils.Position;
import compiler.Parser.Utils.Utils;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ControlStructure {

  private final Utils utils;
  private final Position savedPosition;
  private final List<HashSet<TokenType>> expectedSymbolsControlStructure = List.of(
      new HashSet<>(Set.of(TokenType.TYPESPECIFIER, TokenType.RECORD)));
  private LinkedList<ASTNode> controlStructureNode;
  private final String nodeName = "ControlStructure";

  public ControlStructure(Utils utils, Position savedPosition) {
    this.utils = utils;
    this.savedPosition = savedPosition;
    controlStructureNode = new LinkedList<>();
  }


  public MainNode controlStructure() throws ParserException, UnrecognisedTokenException {
    if (utils.matchIndex(TokenType.FOR, true)) {
      controlStructureNode.addLast(utils.getGenericNode());
      if (utils.matchIndex(TokenType.LPAREN, true)) {
        controlStructureNode.addLast(utils.getGenericNode());
        if (utils.matchIndex(TokenType.IDENTIFIER, true)) {
          controlStructureNode.addLast(utils.getGenericNode());
          if (utils.lookahead_matches(expectedSymbolsControlStructure, true)) {
            controlStructureNode.addAll(utils.getAstNodes());
          }
          return forLoop();
        }
      }
    }

    MainNode whileNode = whileLoop();
    if (whileNode != null) {
      controlStructureNode.addLast(whileNode);
      return whileNode;
    }

    return ifStatement();
  }

  private MainNode forLoop() throws ParserException, UnrecognisedTokenException {
    if (utils.matchIndex(TokenType.COMMA, true)) {
      controlStructureNode.addLast(utils.getGenericNode());
      MainNode expressionNode = new Expression(utils, savedPosition).expression();
      controlStructureNode.addLast(expressionNode);
      if (utils.matchIndex(TokenType.COMMA, true)) {
        controlStructureNode.addLast(utils.getGenericNode());

        expressionNode = new Expression(utils, savedPosition).expression();
        controlStructureNode.addLast(expressionNode);
      }
      if (utils.matchIndex(TokenType.COMMA, true)) {
        controlStructureNode.addLast(utils.getGenericNode());
        expressionNode = new Expression(utils, savedPosition).expression();
        controlStructureNode.addLast(expressionNode);
      }
      if (rightPar()) {
        return new MainNode("For" + nodeName, controlStructureNode);
      }
    }
    utils.throwParserException();
    return null;
  }

  private boolean rightPar() throws ParserException, UnrecognisedTokenException {
    if (utils.matchIndex(TokenType.RPAREN, true)) {
      controlStructureNode.addLast(utils.getGenericNode());
      if (utils.matchIndex(TokenType.LBRACE, true)) {
        controlStructureNode.addLast(utils.getGenericNode());
        MainNode mainNode = new StatementList(utils, savedPosition).statementList();
        controlStructureNode.addLast(mainNode);
        if (utils.matchIndex(TokenType.RBRACE, true)) {
          controlStructureNode.addLast(utils.getGenericNode());
          return true;
        }
      }
    }
/*
    utils.throwParserException();
*/
    return false;
  }

  private MainNode whileLoop() throws ParserException, UnrecognisedTokenException {
    if (utils.matchIndex(TokenType.WHILE, true)) {
      controlStructureNode.addLast(utils.getGenericNode());
      if (utils.matchIndex(TokenType.LPAREN, true)) {
        controlStructureNode.addLast(utils.getGenericNode());
        MainNode expressionNode = new Expression(utils, savedPosition).expression();
        controlStructureNode.addLast(expressionNode);
        if (rightPar()) {
          return new MainNode("While" + nodeName, controlStructureNode);
        }
      }
    }
/*
    utils.throwParserException();
*/
    return null;
  }

  private MainNode ifStatement() throws ParserException, UnrecognisedTokenException {
    if (utils.matchIndex(TokenType.IF, true)) {
      controlStructureNode.addLast(utils.getGenericNode());
      if (utils.matchIndex(TokenType.LPAREN, true)) {
        controlStructureNode.addLast(utils.getGenericNode());
        if (utils.matchIndex(TokenType.IDENTIFIER, false) || utils.matchIndex(TokenType.RECORD,
            false)
            || utils.matchIndex(TokenType.BUILTINFUNCTION, false)) {
          if (!utils.getGenericNode().getName().equals("Identifier")) {
            controlStructureNode.addLast(utils.getGenericNode());
          }
        }
        MainNode expressionNode = new Expression(utils, savedPosition).expression();
        controlStructureNode.addLast(expressionNode);
        if (utils.matchIndex(TokenType.RPAREN, true)) {
          controlStructureNode.addLast(utils.getGenericNode());
          if (utils.matchIndex(TokenType.LBRACE, true)) {
            controlStructureNode.addLast(utils.getGenericNode());
            MainNode mainNode = new StatementList(utils, savedPosition).statementList();
            controlStructureNode.addLast(mainNode);
            if (utils.matchIndex(TokenType.RBRACE, true)) {
              controlStructureNode.addLast(utils.getGenericNode());
              if (utils.matchIndex(TokenType.ELSE, true)) {
                controlStructureNode.addLast(utils.getGenericNode());
                if (utils.matchIndex(TokenType.LBRACE, true)) {
                  controlStructureNode.addLast(utils.getGenericNode());
                  MainNode mainNode1 = new StatementList(utils, savedPosition).statementList();
                  controlStructureNode.addLast(mainNode1);
                  if (utils.matchIndex(TokenType.RBRACE, true)) {
                    controlStructureNode.addLast(utils.getGenericNode());
                    return new MainNode("If" + nodeName, controlStructureNode);
                  }
                }
              }
              return new MainNode("If" + nodeName, controlStructureNode);
            }
          }
        }
      }
    }
/*
    utils.throwParserException();
*/
    return null;
  }
}
