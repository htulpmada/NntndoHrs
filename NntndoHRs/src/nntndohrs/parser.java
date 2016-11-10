/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nntndohrs;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author Adam Pluth
 */
class parser {
static lexeme tree;
static lexeme t;

    public parser() throws FileNotFoundException, IOException {
        t=lexer.t;
    }
    
    //utility functions
    public lexeme parse() {
        //advance();
        lexeme root = program();
        lexeme eof = match("ENDOFINPUT");
        return cons("PARSE",root,eof);
    }
    
    public void fatal(String problem){
        System.out.println("\nERROR; "+problem);
        System.exit(1);
    }
    
    public Boolean check(String type){return t.type.equals(type);}
    
    public lexeme advance(){
        lexeme old = t;
        t=t.left;
        System.out.println(old.type+" "+old.integer+" "+old.string);
        return old;
    }
    
    public lexeme match(String type){
        if(check(type)){return advance();}
        fatal("Syntax Error. Expected "+type+" , Received "+t.type);
        return null;
    }
    
    public lexeme cons(String value,lexeme l,lexeme r){return new lexeme(value, value, l, r);}

    //grammar functions
    public lexeme program(){
        lexeme d= definition();
            if(programPending()){
                lexeme p=program();
                return cons("PROGRAM",d,cons("JOIN",p,null));
            }
            return cons("PROGRAM",d,null);
    }

    public lexeme definition(){
        if(variableDefinitionPending()){
            lexeme v = variableDefinition();
            return cons("DEFINITION", v, null);
        }
        else if(functionDefinitionPending()){
            lexeme f = functionDefinition();
            return cons("DEFINITION", f, null);
        }
        return null;//idDef should be here
    }
    
    public lexeme variableDefinition() {
        lexeme v = match("TYPE");
        lexeme i = match("ID");
        lexeme eq = match("EQUAL");
        lexeme e = expr();
        lexeme s = match("SEMI");
        return cons("VARDEF", v, cons("JOIN", i, cons("JOIN", eq, cons("JOIN", e, cons("JOIN", s, null)))));
    }

    public lexeme functionDefinition(){
        lexeme f = match("FUNC");
        lexeme e = match("ID");
        lexeme o = match("OPAREN");
        lexeme op = optParamList();
        lexeme c = match("CPAREN");
        lexeme b = block();
        return cons("FUNCDEF", f, cons("JOIN", e, cons("JOIN", o, cons("JOIN", op, cons("JOIN", c, cons("JOIN", b, null))))));
    }
    
    public lexeme idDef(){
        lexeme i = match("ID");
        if (check("OPAREN")){
            lexeme o = match("OPAREN");
            lexeme e = optExprList();
            lexeme c = match("CPAREN");
            return cons("FUNCCALL", i, cons("JOIN", o, cons("JOIN", e, cons("JOIN", c, null))));
        }
        else if (check("OBRACKET")){
            lexeme o = match("OBRACKET");
            lexeme e = expr();
            lexeme c = match("CBRACKET");
            return cons("ARRAYACCESS", i, cons("JOIN", o, cons("JOIN", e, cons("JOIN", c, null))));
        }
        else{
            return cons("IDDEF", i, null);
        }
    }
    
    public lexeme optParamList(){
        if(pListPending()){
            lexeme p = paramList();
            return cons("OPTPARAMLIST", p, null);
        }
        return cons("OPTPARAMLIST", null, null);
    }
    
    public lexeme paramList(){
        lexeme i = match("ID");
        if (check("COMMA")){
            lexeme c = match("COMMA");
            lexeme p = paramList();
            return cons("PARAMLIST", i, cons("JOIN", c, cons("JOIN", p, null)));
        }
        return cons("PARAMLIST", i, null);
    }
    
    public lexeme optExprList(){
        if(exprListPending()){
            lexeme e = exprList();
            return cons("OPTEXPRLIST", e, null);
        }            
        return cons("OPTEXPRLIST", null, null);
    }
    
    public lexeme exprList(){
        lexeme e = expr();
        if (check("COMMA")){
            lexeme c = match("COMMA");
            lexeme ex = exprList();
            return cons("EXPRLIST", e, cons("JOIN", c, cons("JOIN", ex, null)));
        }
        return cons("EXPRLIST", e, null);
    }

     public lexeme expr(){
        lexeme p = unary();
        if(opPending()){
            lexeme o = operator();
            lexeme e = expr();
            return new lexeme("EXPR", "EXPR", new lexeme("OPERATOR", o.type, p, e),null);
        }     
    return cons("EXPR", p, null);
    }    
    
    public lexeme unary(){
        if (idDefPending()){
            lexeme p = idDef();
            return cons("PRIMARY", p, null);
        }
        else if(check("STRING")){
            lexeme p = match("STRING");
            return cons("PRIMARY", p, null);
        }
        else if(check("INTEGER")){
            lexeme p = match("INTEGER");
            return cons("PRIMARY", p, null);
        }
        else if (check("NOT")){
            lexeme n = match("NOT");
            lexeme p = unary();
            return cons("PRIMARY", n, cons("JOIN", p, null));
        }
        else if (check("OPAREN")){
            lexeme o = match("OPAREN");
            lexeme e = expr();
            lexeme c = match("CPAREN");
            return cons("PRIMARY", o, cons("JOIN", e, cons("JOIN", c, null)));
        }
        else if (k_lambdaPending()){
            lexeme p = k_lambda();
            return cons("PRIMARY", p, null);
        }
        else if (functionDefinitionPending()){
            lexeme p = functionDefinition();
            return cons("PRIMARY", p, null);
        }
        else if (check("OBRACKET")){
            lexeme o = match("OBRACKET");
            lexeme e = optExprList();
            lexeme c = match("CBRACKET");
            return cons("PRIMARY", o, cons("JOIN", e, cons("JOIN", c, null)));
        }
        else if (check("NIL")){
            lexeme n = match("NIL");
            return cons("PRIMARY", n, null);
        }
        else if (check("BOOLEAN")){
            lexeme b = match("BOOLEAN");
            return cons("PRIMARY", b, null);
        }
        else if (check("PRINT")){
            lexeme f = match("PRINT");
            lexeme o = match("OPAREN");
            lexeme e = exprList();
            lexeme c = match("CPAREN");
            return cons("PRINT", f, cons("JOIN", o, cons("JOIN", e, cons("JOIN", c, null))));
        }
        else if (check("APPEND")){
            lexeme f = match("APPEND");
            lexeme o = match("OPAREN");
            lexeme e = exprList();
            lexeme c = match("CPAREN");
            return cons("APPEND", f, cons("JOIN", o, cons("JOIN", e, cons("JOIN", c, null))));
        }
        else if (check("INSERT")){
            lexeme f = match("INSERT");
            lexeme o = match("OPAREN");
            lexeme e = exprList();
            lexeme c = match("CPAREN");
            return cons("INSERT", f, cons("JOIN", o, cons("JOIN", e, cons("JOIN", c, null))));
        }
        else if (check("REMOVE")){
            lexeme f = match("REMOVE");
            lexeme o = match("OPAREN");
            lexeme e = exprList();
            lexeme c = match("CPAREN");
            return cons("REMOVE", f, cons("JOIN", o, cons("JOIN", e, cons("JOIN", c, null))));
        }
        else if (check("SET")){
            lexeme f = match("SET");
            lexeme o = match("OPAREN");
            lexeme e = exprList();
            lexeme c = match("CPAREN");
            return cons("SET", f, cons("JOIN", o, cons("JOIN", e, cons("JOIN", c, null))));
        }
        else if (check("LENGTH")){
            lexeme f = match("LENGTH");
            lexeme o = match("OPAREN");
            lexeme e = exprList();
            lexeme c = match("CPAREN");
            return cons("LENGTH", f, cons("JOIN", o, cons("JOIN", e, cons("JOIN", c, null))));
        }
        else return null;
    }
    
    public lexeme operator(){
        if(check("EQUAL")){
            lexeme op = match("EQUAL");
            return cons("EQUAL", op, null);
        }
        else if(check("NOTEQUAL")){
            lexeme op = match("NOTEQUAL");
            return cons("NOTEQUAL", op, null);
        }
        else if(check("GREATER")){
            lexeme op = match("GREATER");
            return cons("GREATER", op, null);
        }
        else if(check("LESS")){
            lexeme op = match("LESS");
            return cons("LESS", op, null);
        }
        else if(check("GREATEREQUAL")){
            lexeme op = match("GREATEREQUAL");
            return cons("GREATEREQUAL", op, null);
        }
        else if(check("LESSEQUAL")){
            lexeme op = match("LESSEQUAL");
            return cons("LESSEQUAL", op, null);
        }
        else if(check("PLUS")){
            lexeme op = match("PLUS");
            return cons("PLUS", op, null);
        }
        else if(check("MINUS")){
            lexeme op = match("MINUS");
            return cons("MINUS", op, null);
        }
        else if(check("MULTIPLY")){
            lexeme op = match("MULTIPLY");
            return cons("MULTIPLY", op, null);
        }
        else if(check("DIVIDE")){
            lexeme op = match("DIVIDE");
            return cons("DIVIDE", op, null);
        }
        else if(check("INTEGERDIVIDE")){
            lexeme op = match("INTEGERDIVIDE");
            return cons("INTEGERDIVIDE", op, null);
        }
        else if(check("POWER")){
            lexeme op = match("POWER");
            return cons("POWER", op, null);
        }
        else if(check("AND")){
            lexeme op = match("AND");
            return cons("AND", op, null);
        }
        else if(check("OR")){
            lexeme op = match("OR");
            return cons("OR", op, null);
        }
        else if(check("ASSIGN")){
            lexeme op = match("ASSIGN");
            return cons("ASSIGN", op, null);
        }
        else if(check("DOUBLEEQUAL")){
            lexeme op = match("DOUBLEEQUAL");
            return cons("DOUBLEEQUAL", op, null);
        }
        else return null;
    }
    
     public lexeme block(){
        lexeme o = match("OCURLY");
        lexeme s = optStatementList();
        lexeme c = match("CCURLY");
        return cons("BLOCK", o, cons("JOIN", s, cons("JOIN", c, null)));
     }
    
    public lexeme optStatementList(){
        if (statementListPending()){
            lexeme s = statementList();
            return cons("OPTSTATEMENTLIST", s, null);
        }
    return cons("OPTSTATEMENTLIST", null, null);
    }
     
    public lexeme statementList(){
        lexeme s= statement();
        if(statementListPending()){
            lexeme sl = statementList();
            return cons("STATEMENTLIST", s, cons("JOIN", sl, null));
        }
    return cons("STATEMENTLIST", s, null);
    }    
    
    public lexeme statement(){
        if(variableDefinitionPending()){
            lexeme v = variableDefinition();
            return cons("STATEMENT", v, null);
        }
        else if(functionDefinitionPending()){
            lexeme f = functionDefinition();
            return cons("STATEMENT", f, null);
        }
        else if(exprPending()){
            lexeme e = expr();
            lexeme s = match("SEMI");
            return cons("STATEMENT", e, cons("JOIN", s, null));
        }
        else if(whileLoopPending()){
            lexeme w = whileLoop();
            return cons("STATEMENT", w, null);
        }
        else if(ifStatementPending()){
            lexeme i = ifStatement();
            return cons("STATEMENT", i, null);
        }
        else if(check("RETURN")){
            lexeme r = match("RETURN");
            lexeme e = expr();
            lexeme s = match("SEMI");
        return cons("STATEMENT", r ,cons("JOIN", e, cons("JOIN", s, null)));
        }
        else return null;
    }
    
    public lexeme whileLoop(){
        lexeme w = match("WHILE");
        lexeme o = match("OPAREN");
        lexeme e = expr();
        lexeme c = match("CPAREN");
        lexeme b = block();
        return cons("WHILELOOP", w ,cons("JOIN", o, cons("JOIN", e, cons("JOIN", c, cons("JOIN", b, null)))));
    }
    
    public lexeme ifStatement(){
        lexeme i = match("IF");
        lexeme o = match("OPAREN");
        lexeme e = expr();
        lexeme c = match("CPAREN");
        lexeme b = block();
        lexeme oe = optElseStatement();
        return cons("IFSTATEMENT", i ,cons("JOIN", o, cons("JOIN", e, cons("JOIN", c, cons("JOIN", b, cons("JOIN", oe, null))))));
    }
    
    public lexeme optElseStatement(){
        if (elseStatementPending()){
            lexeme e = elseStatement();
            return cons("OPTELSESTATEMENT", e, null);
        }
        return cons("OPTELSESTATEMENT", null, null);
    }
    
    public lexeme elseStatement(){
        lexeme e = match("ELSE");
        if(blockPending()){
            lexeme b = block();
            return cons("ELSESTATEMENT", e, cons("JOIN", b, null));
        }
        else if(ifStatementPending()){
            lexeme i = ifStatement();
            return cons("ELSESTATEMENT", e, cons("JOIN", i, null));
        }
        else return null;
    }
    
    public lexeme k_lambda(){
        lexeme l = match("LAMBDA");
        lexeme o = match("OPAREN");
        lexeme op = optParamList();
        lexeme c = match("CPAREN");
        lexeme b = block();
        return cons("LAMBDA", l ,cons("JOIN", o, cons("JOIN", op, cons("JOIN", c, cons("JOIN", b, null)))));
    }
    
    
    //pending functions
    public boolean programPending(){return definitionPending();}
    public boolean definitionPending(){return variableDefinitionPending() | functionDefinitionPending() | idDefPending();}
    public boolean variableDefinitionPending(){return check("TYPE");}
    public boolean functionDefinitionPending(){return check("FUNC");}
    public boolean idDefPending(){return check("ID");}
    public boolean pListPending(){return check("ID");}
    public boolean exprListPending(){return exprPending();}
    public boolean exprPending(){return unaryPending();}
    public boolean unaryPending(){return idDefPending() | check("STRING") | check("INTEGER") | check("NOT") | check("OPAREN") | k_lambdaPending() | functionDefinitionPending() | check("OBRACKET") | check("NIL") | check("BOOLEAN") | check("PRINT") | check("APPEND") | check("INSERT") | check("REMOVE") | check("SET") | check("LENGTH");}
    public boolean opPending(){return check("EQUAL") | check("NOTEQUAL") | check("GREATER") | check("LESS") | check("GREATEREQUAL") | check("LESSEQUAL") | check("PLUS") | check("MINUS") | check("MULTIPLY") | check("DIVIDE") | check("INTEGERDIVIDE") | check("POWER") | check("AND") | check("OR") | check("ASSIGN") | check("DOUBLEEQUAL");}
    public boolean blockPending(){return check("OCURLY");}
    public boolean statementListPending(){return statementPending();}
    public boolean statementPending(){return variableDefinitionPending() | functionDefinitionPending() | exprPending() | whileLoopPending() | ifStatementPending() | check("RETURN");}
    public boolean whileLoopPending(){return check("WHILE");}
    public boolean ifStatementPending(){return check("IF");}
    public boolean elseStatementPending(){return check("ELSE");}
    public boolean k_lambdaPending(){return check("LAMBDA");}

    

}
