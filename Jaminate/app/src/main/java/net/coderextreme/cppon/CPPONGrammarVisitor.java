// Generated from src/main/antlr4/CPPONGrammar.g4 by ANTLR 4.13.1
package net.coderextreme.cppon;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link CPPONGrammarParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface CPPONGrammarVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link CPPONGrammarParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType(CPPONGrammarParser.TypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPPONGrammarParser#field}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitField(CPPONGrammarParser.FieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPPONGrammarParser#variable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariable(CPPONGrammarParser.VariableContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPPONGrammarParser#string}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitString(CPPONGrammarParser.StringContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPPONGrammarParser#cstring}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCstring(CPPONGrammarParser.CstringContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPPONGrammarParser#boolean}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolean(CPPONGrammarParser.BooleanContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPPONGrammarParser#boolean_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolean_list(CPPONGrammarParser.Boolean_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPPONGrammarParser#integer_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInteger_list(CPPONGrammarParser.Integer_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPPONGrammarParser#float_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFloat_list(CPPONGrammarParser.Float_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPPONGrammarParser#string_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitString_list(CPPONGrammarParser.String_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPPONGrammarParser#list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitList(CPPONGrammarParser.ListContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPPONGrammarParser#construct_array}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstruct_array(CPPONGrammarParser.Construct_arrayContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPPONGrammarParser#parameters}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameters(CPPONGrammarParser.ParametersContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPPONGrammarParser#operator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperator(CPPONGrammarParser.OperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPPONGrammarParser#funccall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunccall(CPPONGrammarParser.FunccallContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPPONGrammarParser#construct}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstruct(CPPONGrammarParser.ConstructContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPPONGrammarParser#set_field}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSet_field(CPPONGrammarParser.Set_fieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPPONGrammarParser#add_field}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdd_field(CPPONGrammarParser.Add_fieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPPONGrammarParser#line}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLine(CPPONGrammarParser.LineContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPPONGrammarParser#lines}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLines(CPPONGrammarParser.LinesContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPPONGrammarParser#x3d}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitX3d(CPPONGrammarParser.X3dContext ctx);
}