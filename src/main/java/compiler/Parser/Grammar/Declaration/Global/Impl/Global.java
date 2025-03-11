package compiler.Parser.Grammar.Declaration.Global.Impl;

public class Global {
/*
  private final Utils utils;
  private Expression expression;
  private List<HashSet<String>> expectedSymbols = List.of(
      new HashSet<>(Set.of(SymbolsName.Identifier.name())),
      new HashSet<>(Set.of(
          SymbolsName.TypeSpecifier.name(),
          SymbolsName.Record.name()
      )),
      new HashSet<>(Set.of(SymbolsName.Assignment.name()))
  );

  private List<HashSet<String>> expectedSymbolsBuiltInFunction = List.of(
      new HashSet<>(Set.of(SymbolsName.BuiltInFunction.name())),
      new HashSet<>(Set.of(SymbolsName.SpecialSymbol.name())),
      new HashSet<>(Set.of(SymbolsName.SpecialSymbol.name())),
      new HashSet<>(Set.of(SymbolsName.Punctuation.name()))

  );

  public Global(Utils utils) throws UnrecognisedTokenException, ParserException {
    this.utils = utils;
*//*
    expression = new Expression(utils);
*//*

  }

  *//*public GlobalNode isGlobal() throws UnrecognisedTokenException, ParserException {
    if (utils.lookahead_is(SymbolsName.Identifier.name())) {
      utils.match(SymbolsName.Identifier.name());
*//**//*
      IdentifierNode identifierNode = new IdentifierNode(utils.getPreviousSymbol().getToken());
*//**//*
      if (utils.lookahead_is(SymbolsName.TypeSpecifier.name())) {
        utils.match(SymbolsName.TypeSpecifier.name());
       *//**//* TypeSpecifier typeSpecifier = new TypeSpecifier(utils.getPreviousSymbol().getToken(),
            utils.getPreviousSymbol().getLine_number());
        TypeSpecifierNode typeSpecifierNode = new TypeSpecifierNode(
            typeSpecifier.typeOfTypeSpecifier());*//**//*
      } else {
        utils.match(SymbolsName.Record.name());

      }
      utils.match(SymbolsName.Assignment.name());
*//**//*
      ExpressionNode expressionNode = expression.expression();
*//**//*
      if (utils.lookahead_is(SymbolsName.BuiltInFunction.name())) {
        utils.match(SymbolsName.BuiltInFunction.name());
        utils.match(SymbolsName.SpecialSymbol.name());
*//**//*
        ParameterListNode parameterList = new ParameterList(utils).isParameterList();
*//**//*
        utils.match(SymbolsName.SpecialSymbol.name());
        utils.match(SymbolsName.Punctuation.name());

      } else if (utils.lookahead_is(SymbolsName.Punctuation.name())) {
        utils.match(SymbolsName.Punctuation.name());

      } else {
        new FunctionCall(utils).isFunctionCall();
      }
      *//**//*SpecialSymbolNode specialSymbolNode = new SpecialSymbolNode(
          utils.getPreviousSymbol().getToken());*//**//*
   *//**//*
      return new GlobalNode(identifierNode, expressionNode, specialSymbolNode);
*//**//*
      return new GlobalNode(null, null, null);
    }
    return null;
  }*//*
  public GlobalNode isGlobal() throws UnrecognisedTokenException, ParserException {
    *//*if (utils.lookahead_matches(expectedSymbols)) {
      if (!utils.lookahead_matches(expectedSymbolsBuiltInFunction)) {
        if (utils.lookahead_is(SymbolsName.Punctuation.name())) {
          utils.match(SymbolsName.Punctuation.name());
        } else {
          new FunctionCall(utils).isFunctionCall();
        }
      }
      return new GlobalNode(null, null, null);
    }*//*
    return null;
  }*/
}

