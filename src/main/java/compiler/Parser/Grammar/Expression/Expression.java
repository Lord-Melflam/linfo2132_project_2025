package compiler.Parser.Grammar.Expression;

import compiler.Exceptions.Lexer.UnrecognisedTokenException;
import compiler.Exceptions.Parser.ParserException;
import compiler.Parser.ASTNode.MainNode;
import compiler.Parser.Utils.Enum.TokenType;
import compiler.Parser.Utils.Interfaces.ASTNode;
import compiler.Parser.Utils.Position;
import compiler.Parser.Utils.Utils;
import java.util.LinkedList;
import java.util.List;

public class Expression {

  private final Utils utils;
  private final Position savedPosition;
  private final LinkedList<ASTNode> expressionNode;
  private final String nodeName = "Expression";
  private ASTNode identifier;
  private int line;

  public Expression(Utils utils, Position savedPosition) {
    this.utils = utils;
    this.savedPosition = savedPosition;
    this.expressionNode = new LinkedList<>();
    line = utils.getLine();
  }

  public Expression(Utils utils, Position savedPosition, ASTNode identifier) {
    this.utils = utils;
    this.savedPosition = savedPosition;
    expressionNode = new LinkedList<>();
    expressionNode.addLast(identifier);
    this.identifier = identifier;
    line = utils.getLine();
  }

  public MainNode expression() throws UnrecognisedTokenException, ParserException {
    return logicalOr();
  }

  private MainNode logicalOr() throws UnrecognisedTokenException, ParserException {
    MainNode node = logicalAnd();
    while (utils.matchIndex(TokenType.OR, true)) {
      ASTNode operator = utils.getGenericNode();
      MainNode right = logicalAnd();
      LinkedList<ASTNode> localNodes = new LinkedList<>();
      localNodes.add(node);
      localNodes.add(operator);
      localNodes.add(right);
      node = new MainNode("LogicalExpression", localNodes, line);
    }
    return node;
  }

  private MainNode logicalAnd() throws UnrecognisedTokenException, ParserException {
    MainNode node = equality();
    while (utils.matchIndex(TokenType.AND, true)) {
      ASTNode operator = utils.getGenericNode();
      MainNode right = equality();
      LinkedList<ASTNode> localNodes = new LinkedList<>();
      localNodes.add(node);
      localNodes.add(operator);
      localNodes.add(right);
      node = new MainNode("LogicalExpression", localNodes, line);
    }
    return node;
  }


  private MainNode equality() throws UnrecognisedTokenException, ParserException {
    MainNode node = addition();
    while (utils.matchIndex(TokenType.DOUBLEEQUALS, true) || utils.matchIndex(TokenType.NOT_EQUAL,
        true) || utils.matchIndex(TokenType.LESS, true) || utils.matchIndex(TokenType.GREATER, true)
        || utils.matchIndex(TokenType.LESS_EQUAL, true) || utils.matchIndex(TokenType.GREATER_EQUAL,
        true)) {
      expressionNode.addLast(utils.getGenericNode());
      expressionNode.addLast(addition());
      node = new MainNode(nodeName, expressionNode, line);
    }
    return node;
  }

  private MainNode addition() throws UnrecognisedTokenException, ParserException {
    MainNode left = multiplication();

    while (utils.matchIndex(TokenType.PLUS, true) || utils.matchIndex(TokenType.MINUS, true)) {
      ASTNode operator = utils.getGenericNode();
      MainNode right = multiplication();
      LinkedList<ASTNode> localNodes = new LinkedList<>();
      localNodes.add(left);
      localNodes.add(operator);
      localNodes.add(right);

      left = new MainNode("BinaryExpression", localNodes, line);
    }

    return left;
  }


  private MainNode multiplication() throws UnrecognisedTokenException, ParserException {
    MainNode node = unary();
    while (utils.matchIndex(TokenType.STAR, true) || utils.matchIndex(TokenType.SLASH, true)
        || utils.matchIndex(TokenType.MOD, true)) {
      expressionNode.addLast(unary());
      node = new MainNode(nodeName, expressionNode, line);
    }
    return node;
  }

  private MainNode unary() throws UnrecognisedTokenException, ParserException {
    if (utils.matchIndex(TokenType.MINUS, true)) {
      ASTNode operator = utils.getGenericNode();
      expressionNode.addLast(operator);
      MainNode mainNode = primary();
      expressionNode.addLast(mainNode);
      return new MainNode("UnaryExpression", expressionNode, line);
    }
    return primary();
  }

  private MainNode primary() throws UnrecognisedTokenException, ParserException {
    MainNode node;

    if (utils.matchIndex(TokenType.LITERAL, true)) {
      return continuation(new MainNode(nodeName, List.of(utils.getGenericNode()), line));
    } else if (utils.matchIndex(TokenType.IDENTIFIER, true) || utils.matchIndex(TokenType.RECORD,
        true) || utils.matchIndex(TokenType.BUILTINFUNCTION, true)) {
      node = functionOrVariable();
    } else if (utils.matchIndex(TokenType.LPAREN, true)) {

      ASTNode lparen = utils.getGenericNode();
      node = expression();
      if (!utils.matchIndex(TokenType.RPAREN, true)) {
        return null;
      }
      ASTNode rparen = utils.getGenericNode();
      if (utils.matchIndex(TokenType.SEMICOLON, false)) {
        if (node != null) {
          //node.add(List.of(rparen));
          if (identifier == null) {
            return node;
          }
          return new MainNode("Expression", List.of(identifier,/* lparen,*/ node/*, rparen*/),
              line);
        }
        expressionNode.addLast(new MainNode("Parameters", List.of(/*lparen, rparen*/), line));
        return new MainNode("Function", expressionNode, line);
      }
    } else if (utils.matchIndex(TokenType.ARRAY, true)) {
      node = parseArrayInitialization();
    } else {
      return null;
    }

    if (utils.matchIndex(TokenType.LBRACKET, false)) {
      node = parseArrayAccess(node);
    }

    return continuation(node);
  }

  private MainNode parseArrayInitialization() throws UnrecognisedTokenException, ParserException {
    LinkedList<ASTNode> localNodes = new LinkedList<>();
    localNodes.add(utils.getGenericNode());

    if (!utils.matchIndex(TokenType.LBRACKET, true)) {
      return null;
    }
    MainNode sizeExpression = expression();
    while (utils.matchIndex(TokenType.COMMA, true)) {
      localNodes.add(expression());
    }
    if (utils.matchIndex(TokenType.RBRACKET, true)) {
      if (utils.matchIndex(TokenType.OF, true)) {
        ASTNode ofNode = utils.getGenericNode();
        if (utils.matchIndex(TokenType.TYPESPECIFIER, true)) {
          ASTNode typeNode = utils.getGenericNode();
          localNodes.add(sizeExpression);
          localNodes.add(ofNode);
          localNodes.add(typeNode);
          return new MainNode("ArrayInitialization", localNodes, line);
        }
      }
    }
    return null;
  }

  private MainNode parseArrayAccess(MainNode node)
      throws UnrecognisedTokenException, ParserException {
    while (utils.matchIndex(TokenType.LBRACKET, true)) {
      LinkedList<ASTNode> localNodes = new LinkedList<>();
      localNodes.add(node);
      if (!utils.matchIndex(TokenType.RBRACKET, true)) {
        MainNode indexExpression = expression();
        localNodes.add(indexExpression);
      }

      if (!utils.matchIndex(TokenType.RBRACKET, true)) {
        return null;
      }
      node = new MainNode("ArrayAccess", localNodes, line);
    }
    return node;
  }


  private MainNode functionOrVariable() throws UnrecognisedTokenException, ParserException {
    LinkedList<ASTNode> localNodes = new LinkedList<>();
    ASTNode node = utils.getGenericNode();
    localNodes.add(node);

    if (utils.matchIndex(TokenType.LPAREN, true)) {
      //localNodes.add(utils.getGenericNode());
      LinkedList<ASTNode> argNodes = new LinkedList<>();

      while (!utils.matchIndex(TokenType.RPAREN, true)) {
        argNodes.add(expression());
        if (utils.matchIndex(TokenType.COMMA, true)) {
        }
      }
      MainNode mainNodeArg = new MainNode("Parameters", argNodes, line);
      localNodes.add(mainNodeArg);
      localNodes.add(utils.getGenericNode());
      switch (node.getName()) {
        case "Record" -> {
          return new MainNode("RecordConstructorCall", localNodes, line);
        }
        case "BuiltInFunction" -> {
          return new MainNode("BuiltInFunctionCall", localNodes, line);
        }
        case "Identifier" -> {
          return new MainNode("FunctionCall", localNodes, line);
        }
      }
    }

    return new MainNode(nodeName, localNodes, line);
  }


  private MainNode continuation(MainNode left) throws UnrecognisedTokenException, ParserException {
    LinkedList<ASTNode> localNodes = new LinkedList<>();
    localNodes.add(left);

    while (true) {
      if (utils.matchIndex(TokenType.LBRACKET, false)) {
        left = parseArrayAccess(left);
        localNodes.add(left);
      } else if (utils.matchIndex(TokenType.DOT, true)) {
        localNodes.add(utils.getGenericNode());
        if (!utils.matchIndex(TokenType.IDENTIFIER, true)) {
          /*utils.throwParserException();*/
          return null;
        }
        localNodes.add(utils.getGenericNode());
        left = new MainNode("FieldAccess", localNodes, line);
      } else if (utils.matchIndex(TokenType.OPERATOR, true)) {
        /*todo code gen*/
        if (left.getName().equals("FieldAccess")) {
          localNodes.clear();
          localNodes.addLast(left);
        }
        localNodes.add(utils.getGenericNode());
        //localNodes.add(utils.getGenericNode());
        /*todo code gen*/
        MainNode right = addition();
        localNodes.add(right);
        left = new MainNode("BinaryExpression", localNodes, line);
      } /*else if (utils.matchIndex(TokenType.BUILTINFUNCTION, true)) {
        left = functionOrVariable();
        System.out.println();
      }*/ else {
        break;
      }
    }
    return left;
  }
}