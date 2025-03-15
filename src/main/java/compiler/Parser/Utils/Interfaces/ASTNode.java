package compiler.Parser.Utils.Interfaces;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import compiler.Parser.ASTNode.GenericNode;
import compiler.Parser.ASTNode.MainNode;
import compiler.Parser.ASTNode.TypeSpecifierNode;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = MainNode.class, name = "MainNode"),
    @JsonSubTypes.Type(value = TypeSpecifierNode.class, name = "TypeSpecifierNode"),
    @JsonSubTypes.Type(value = GenericNode.class, name = "GenericNode")

})

public abstract class ASTNode {

  public abstract void accept(ASTVisitor visitor);
}
