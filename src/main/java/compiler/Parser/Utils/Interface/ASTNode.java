package compiler.Parser.Utils.Interface;


public abstract class ASTNode {

  public abstract void accept(ASTVisitor visitor);
}
