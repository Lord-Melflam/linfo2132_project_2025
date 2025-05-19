package compiler.Parser;

import compiler.Exceptions.Lexer.NotASCIIException;
import compiler.Exceptions.Lexer.UnrecognisedTokenException;
import compiler.Exceptions.Parser.ParserException;
import compiler.Lexer.Lexer;
import compiler.Lexer.Symbol;
import compiler.Parser.ASTNode.ASTNodeProcessor;
import compiler.Parser.ASTNode.GenericNode;
import compiler.Parser.ASTNode.MainNode;
import compiler.Parser.ASTNode.RootNode;
import compiler.Parser.Grammar.Program.Program;
import compiler.Parser.Utils.ASTUtils.ASTUtils;
import compiler.Parser.Utils.Enum.TokenType;
import compiler.Parser.Utils.Interfaces.ASTNode;
import compiler.Parser.Utils.Utils;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Stack;

public class Parser {

  private final Lexer lexer;
  private LinkedList<Symbol> symbols;
  private Stack<Character> stack = new Stack<>();
  private RootNode node;
  private ASTNodeProcessor nodeProcessor = new ASTNodeProcessor();
  private MainNode mainNode;
  public MainNode mainNodeCopy;

  public Parser(Lexer lexer)
      throws UnrecognisedTokenException, ParserException, IOException, NotASCIIException {
    this.lexer = lexer;
    symbols = new LinkedList<>();
    nodeProcessor = getAST();
    mainNode = new MainNode(nodeProcessor.getRoot().getNodes());
    LinkedList<ASTNode> astNodes = new LinkedList<>();
    mainNodeCopy = copy(astNodes);
    mainNode.printTree();
  }

  public ASTNodeProcessor getAstNodeProcessor() {
    return nodeProcessor;
  }

  public boolean isClosingBraceValid(Character c) {
    if (c == '{') {
      stack.push(c);
    } else if (c == '}') {
      if (stack.isEmpty()) {
        return false;
      }
      stack.pop();
    }
    return stack.isEmpty();
  }

  public ASTNodeProcessor getAST()
      throws UnrecognisedTokenException, ParserException, IOException, NotASCIIException {
    Symbol symbol = lexer.getNextSymbol();
    while (symbol != null && !symbol.getName().equals("EndFile")) {
      if (symbol.getName().equals("StartFile")) {
        continue;
      }
      if (symbol.getToken().equals(TokenType.SEMICOLON.getValue()) || symbol.getToken()
          .equals(TokenType.RBRACE.getValue()) || symbol.getToken()
          .equals(TokenType.LBRACE.getValue())) {
        if (symbol.getToken().equals(TokenType.RBRACE.getValue()) || symbol.getToken()
            .equals(TokenType.LBRACE.getValue())) {
          symbols.add(symbol);
          if (isClosingBraceValid(symbol.getToken().charAt(0))) {
            Utils utils = new Utils(symbols);
            Program program = new Program(lexer);
            nodeProcessor = program.isProgram(utils, symbols, nodeProcessor);
            stack.clear();
            symbols.clear();
          }
        } else {
          symbols.add(symbol);
          if (stack.isEmpty()) {
            Utils utils = new Utils(symbols);
            Program program = new Program(lexer);
            nodeProcessor = program.isProgram(utils, symbols, nodeProcessor);
            symbols.clear();
          }
        }
      } else {
        symbols.add(symbol);
      }
      symbol = lexer.getNextSymbol();
    }
    if (nodeProcessor.getRoot().getNodes().isEmpty()) {
      throw new ParserException(Integer.toString(symbols.getLast().getLine_number()));
    }
    ASTUtils.saveAST(nodeProcessor, "test/TestFile/AnswersParser/code.json",
        false);
    return nodeProcessor;
  }

  public MainNode getMainNode() {
    return mainNode;
  }


  public MainNode copy(LinkedList<ASTNode> astNodes) {
    String name = mainNode.getName();
    for (ASTNode astNode : mainNode.getNodes()) {
      if (astNode instanceof GenericNode<?>) {
        astNodes.add(astNode.deepCopy());
      } else {
        LinkedList<ASTNode> astNodesMain = new LinkedList<>();
        copyImpl((MainNode) astNode, astNodesMain, astNodes);
        MainNode newMainNode = new MainNode(astNode.getName(), astNodesMain, astNode.getLine());
        astNodes.add(newMainNode);
      }
    }
    return new MainNode(name, astNodes, mainNode.getLine());
  }

  public MainNode copyImpl(MainNode astNode, LinkedList<ASTNode> astNodesMain,
      LinkedList<ASTNode> astNodes) {
    for (ASTNode node : astNode.getChildrenList()) {
      if (node instanceof GenericNode<?>) {
        astNodesMain.add(node.deepCopy());
      } else {
        LinkedList<ASTNode> list = new LinkedList<>();
        copyImpl((MainNode) node, list, astNodes);
        MainNode newMainNode = new MainNode(node.getName(), list, node.getLine());
        astNodesMain.add(newMainNode);
      }
    }
    return null;
  }

}