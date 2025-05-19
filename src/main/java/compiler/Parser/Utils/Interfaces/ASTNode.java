package compiler.Parser.Utils.Interfaces;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import compiler.Exceptions.Semantic.OperatorError;
import compiler.Parser.ASTNode.GenericNode;
import compiler.Parser.ASTNode.MainNode;
import compiler.Parser.ASTNode.TypeSpecifierNode;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = MainNode.class, name = "MainNode"),
    @JsonSubTypes.Type(value = TypeSpecifierNode.class, name = "TypeSpecifierNode"),
    @JsonSubTypes.Type(value = GenericNode.class, name = "GenericNode")

})
public abstract class ASTNode {

  public abstract String getName();

  public abstract int getLine();

  public abstract void acceptAST(ASTVisitor visitor);

  public abstract <T> T accept(ASTVisitor<T> visitor) throws OperatorError;

  public abstract ASTNode deepCopy();
}
