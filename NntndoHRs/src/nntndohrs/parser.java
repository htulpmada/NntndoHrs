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
    
    public static void fatal(String problem){
        System.out.println("\nERROR; "+problem);
        System.exit(1);
    }
    
    public Boolean check(String type){return t.type.equals(type);}
    
    public lexeme advance(){
        lexeme old = t;
        t=t.left;
        old.left=null;//not sure if this will cause problems
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
            //v.left=null;
            return cons("DEFINITION", v, null);
        }
        else if(functionDefinitionPending()){
            lexeme f = functionDefinition();
            //f.left=null;
            return cons("DEFINITION", f, null);
        }
        return null;//idDef should be here
    }
    
    public lexeme variableDefinition() {
        lexeme v = match("TYPE");
            //v.left=null;
        lexeme i = match("ID");
            //i.left=null;
        lexeme eq = match("EQUAL");
            //eq.left=null;
        lexeme e = expr();
            //e.left=null;
        lexeme s = match("SEMI");
            //s.left=null;
        return cons("VARDEF", v, cons("JOIN", i, cons("JOIN", eq, cons("JOIN", e, cons("JOIN", s, null)))));
    }

    public lexeme functionDefinition(){
        lexeme f = match("FUNC");
            //f.left=null;
        lexeme e = match("ID");
            //e.left=null;
        lexeme o = match("OPAREN");
            //o.left=null;
        lexeme op = optParamList();
            //op.left=null;
        lexeme c = match("CPAREN");
            //c.left=null;
        lexeme b = block();
            //b.left=null;
        return cons("FUNCDEF", f, cons("JOIN", e, cons("JOIN", o, cons("JOIN", op, cons("JOIN", c, cons("JOIN", b, null))))));
    }
    
    public lexeme idDef(){
        lexeme i = match("ID");
            //i.left=null;
        if (check("OPAREN")){
            lexeme o = match("OPAREN");
            //o.left=null;
            lexeme e = optExprList();
            //e.left=null;
            lexeme c = match("CPAREN");
            //c.left=null;
            return cons("FUNCCALL", i, cons("JOIN", o, cons("JOIN", e, cons("JOIN", c, null))));
        }
        else if (check("OBRACKET")){
            lexeme o = match("OBRACKET");
            //o.left=null;
            lexeme e = expr();
            //e.left=null;
            lexeme c = match("CBRACKET");
            //c.left=null;
            return cons("ARRAYACCESS", i, cons("JOIN", o, cons("JOIN", e, cons("JOIN", c, null))));
        }
        else{
            return cons("IDDEF", i, null);
        }
    }
    
    public lexeme optParamList(){
        if(pListPending()){
            lexeme p = paramList();
            //p.left=null;
            return cons("OPTPARAMLIST", p, null);
        }
        return cons("OPTPARAMLIST", null, null);
    }
    
    public lexeme paramList(){
        lexeme i = match("ID");
            //i.left=null;
        if (check("COMMA")){
            lexeme c = match("COMMA");
            //c.left=null;
            lexeme p = paramList();
            //p.left=null;
            return cons("PARAMLIST", i, cons("JOIN", c, cons("JOIN", p, null)));
        }
        return cons("PARAMLIST", i, null);
    }
    
    public lexeme optExprList(){
        if(exprListPending()){
            lexeme e = exprList();
            //e.left=null;
            return cons("OPTEXPRLIST", e, null);
        }            
        return cons("OPTEXPRLIST", null, null);
    }
    
    public lexeme exprList(){
        lexeme e = expr();
            //e.left=null;
        if (check("COMMA")){
            lexeme c = match("COMMA");
            //c.left=null;
            lexeme ex = exprList();
            //ex.left=null;
            return cons("EXPRLIST", e, cons("JOIN", c, cons("JOIN", ex, null)));
        }
        return cons("EXPRLIST", e, null);
    }

     public lexeme expr(){
        lexeme p = unary();
            //p.left=null;
        if(opPending()){
            lexeme o = operator();
            //o.left=null;
            lexeme e = expr();
            //e.left=null;
            return new lexeme("EXPR", "EXPR", new lexeme("OPERATOR", o.type, p, e),null);
        }     
    return cons("EXPR", p, null);
    }    
    
    public lexeme unary(){
        if (idDefPending()){
            lexeme p = idDef();
            //p.left=null;
            return cons("PRIMARY", p, null);
        }
        else if(check("STRING")){
            lexeme p = match("STRING");
            //p.left=null;
            return cons("PRIMARY", p, null);
        }
        else if(check("INTEGER")){
            lexeme p = match("INTEGER");
            //p.left=null;
            return cons("PRIMARY", p, null);
        }
        else if (check("NOT")){
            lexeme n = match("NOT");
            //n.left=null;
            lexeme p = unary();
            //p.left=null;
            return cons("PRIMARY", n, cons("JOIN", p, null));
        }
        else if (check("OPAREN")){
            lexeme o = match("OPAREN");
            //o.left=null;
            lexeme e = expr();
            //e.left=null;
            lexeme c = match("CPAREN");
            //c.left=null;
            return cons("PRIMARY", o, cons("JOIN", e, cons("JOIN", c, null)));
        }
        else if (k_lambdaPending()){
            lexeme p = k_lambda();
            //p.left=null;
            return cons("PRIMARY", p, null);
        }
        else if (functionDefinitionPending()){
            lexeme p = functionDefinition();
            //p.left=null;
            return cons("PRIMARY", p, null);
        }
        else if (check("OBRACKET")){
            lexeme o = match("OBRACKET");
            //o.left=null;
            lexeme e = optExprList();
            //e.left=null;
            lexeme c = match("CBRACKET");
            //c.left=null;
            return cons("PRIMARY", o, cons("JOIN", e, cons("JOIN", c, null)));
        }
        else if (check("NIL")){
            lexeme n = match("NIL");
            //n.left=null;
            return cons("PRIMARY", n, null);
        }
        else if (check("BOOLEAN")){
            lexeme b = match("BOOLEAN");
            //b.left=null;
            return cons("PRIMARY", b, null);
        }
        else if (check("PRINT")){
            lexeme f = match("PRINT");
            //f.left=null;
            lexeme o = match("OPAREN");
            //o.left=null;
            lexeme e = exprList();
            //e.left=null;
            lexeme c = match("CPAREN");
            //c.left=null;
            return cons("PRINT", f, cons("JOIN", o, cons("JOIN", e, cons("JOIN", c, null))));
        }
        else if (check("APPEND")){
            lexeme f = match("APPEND");
            //f.left=null;
            lexeme o = match("OPAREN");
            //o.left=null;
            lexeme e = exprList();
            //e.left=null;
            lexeme c = match("CPAREN");
            //c.left=null;
            return cons("APPEND", f, cons("JOIN", o, cons("JOIN", e, cons("JOIN", c, null))));
        }
        else if (check("INSERT")){
            lexeme f = match("INSERT");
            //f.left=null;
            lexeme o = match("OPAREN");
            //o.left=null;
            lexeme e = exprList();
            //e.left=null;
            lexeme c = match("CPAREN");
            //c.left=null;
            return cons("INSERT", f, cons("JOIN", o, cons("JOIN", e, cons("JOIN", c, null))));
        }
        else if (check("REMOVE")){
            lexeme f = match("REMOVE");
            //f.left=null;
            lexeme o = match("OPAREN");
            //o.left=null;
            lexeme e = exprList();
            //e.left=null;
            lexeme c = match("CPAREN");
            //c.left=null;
            return cons("REMOVE", f, cons("JOIN", o, cons("JOIN", e, cons("JOIN", c, null))));
        }
        else if (check("SET")){
            lexeme f = match("SET");
            //f.left=null;
            lexeme o = match("OPAREN");
            //o.left=null;
            lexeme e = exprList();
            //e.left=null;
            lexeme c = match("CPAREN");
            //c.left=null;
            return cons("SET", f, cons("JOIN", o, cons("JOIN", e, cons("JOIN", c, null))));
        }
        else if (check("LENGTH")){
            lexeme f = match("LENGTH");
            //f.left=null;
            lexeme o = match("OPAREN");
            //o.left=null;
            lexeme e = exprList();
            //e.left=null;
            lexeme c = match("CPAREN");
            //c.left=null;
            return cons("LENGTH", f, cons("JOIN", o, cons("JOIN", e, cons("JOIN", c, null))));
        }
        else return null;
    }
    
    public lexeme operator(){
        if(check("EQUAL")){
            lexeme op = match("EQUAL");
            //op.left=null;
            return cons("EQUAL", op, null);
        }
        else if(check("NOTEQUAL")){
            lexeme op = match("NOTEQUAL");
            //op.left=null;
            return cons("NOTEQUAL", op, null);
        }
        else if(check("GREATER")){
            lexeme op = match("GREATER");
            //op.left=null;
            return cons("GREATER", op, null);
        }
        else if(check("LESS")){
            lexeme op = match("LESS");
            //op.left=null;
            return cons("LESS", op, null);
        }
        else if(check("GREATEREQUAL")){
            lexeme op = match("GREATEREQUAL");
            //op.left=null;
            return cons("GREATEREQUAL", op, null);
        }
        else if(check("LESSEQUAL")){
            lexeme op = match("LESSEQUAL");
            //op.left=null;
            return cons("LESSEQUAL", op, null);
        }
        else if(check("PLUS")){
            lexeme op = match("PLUS");
            //op.left=null;
            return cons("PLUS", op, null);
        }
        else if(check("MINUS")){
            lexeme op = match("MINUS");
            //op.left=null;
            return cons("MINUS", op, null);
        }
        else if(check("MULTIPLY")){
            lexeme op = match("MULTIPLY");
            //op.left=null;
            return cons("MULTIPLY", op, null);
        }
        else if(check("DIVIDE")){
            lexeme op = match("DIVIDE");
            //op.left=null;
            return cons("DIVIDE", op, null);
        }
        else if(check("INTEGERDIVIDE")){
            lexeme op = match("INTEGERDIVIDE");
            //op.left=null;
            return cons("INTEGERDIVIDE", op, null);
        }
        else if(check("POWER")){
            lexeme op = match("POWER");
            //op.left=null;
            return cons("POWER", op, null);
        }
        else if(check("AND")){
            lexeme op = match("AND");
            //op.left=null;            
            return cons("AND", op, null);
        }
        else if(check("OR")){
            lexeme op = match("OR");
            //op.left=null;
            return cons("OR", op, null);
        }
        else if(check("ASSIGN")){
            lexeme op = match("ASSIGN");
            //op.left=null;
            return cons("ASSIGN", op, null);
        }
        else if(check("DOUBLEEQUAL")){
            lexeme op = match("DOUBLEEQUAL");
            //op.left=null;
            return cons("DOUBLEEQUAL", op, null);
        }
        else return null;
    }
    
     public lexeme block(){
        lexeme o = match("OCURLY");
            //o.left=null;
        lexeme s = optStatementList();
            //s.left=null;
        lexeme c = match("CCURLY");
            //c.left=null;
        return cons("BLOCK", o, cons("JOIN", s, cons("JOIN", c, null)));
     }
    
    public lexeme optStatementList(){
        if (statementListPending()){
            lexeme s = statementList();
            //s.left=null;
            return cons("OPTSTATEMENTLIST", s, null);
        }
    return cons("OPTSTATEMENTLIST", null, null);
    }
     
    public lexeme statementList(){
        lexeme s= statement();
            //s.left=null;
        if(statementListPending()){
            lexeme sl = statementList();
            //sl.left=null;
            return cons("STATEMENTLIST", s, cons("JOIN", sl, null));
        }
    return cons("STATEMENTLIST", s, null);
    }    
    
    public lexeme statement(){
        if(variableDefinitionPending()){
            lexeme v = variableDefinition();
            //v.left=null;
            return cons("STATEMENT", v, null);
        }
        else if(functionDefinitionPending()){
            lexeme f = functionDefinition();
            //f.left=null;
            return cons("STATEMENT", f, null);
        }
        else if(exprPending()){
            lexeme e = expr();
            //e.left=null;
            lexeme s = match("SEMI");
            //s.left=null;
            return cons("STATEMENT", e, cons("JOIN", s, null));
        }
        else if(whileLoopPending()){
            lexeme w = whileLoop();
            //w.left=null;
            return cons("STATEMENT", w, null);
        }
        else if(ifStatementPending()){
            lexeme i = ifStatement();
            //i.left=null;
            return cons("STATEMENT", i, null);
        }
        else if(check("RETURN")){
            lexeme r = match("RETURN");
            //r.left=null;
            lexeme e = expr();
            //e.left=null;
            lexeme s = match("SEMI");
            //s.left=null;
        return cons("STATEMENT", r ,cons("JOIN", e, cons("JOIN", s, null)));
        }
        else return null;
    }
    
    public lexeme whileLoop(){
        lexeme w = match("WHILE");
            //w.left=null;
        lexeme o = match("OPAREN");
            //o.left=null;
        lexeme e = expr();
            //e.left=null;
        lexeme c = match("CPAREN");
            //c.left=null;
        lexeme b = block();
            //b.left=null;
        return cons("WHILELOOP", w ,cons("JOIN", o, cons("JOIN", e, cons("JOIN", c, cons("JOIN", b, null)))));
    }
    
    public lexeme ifStatement(){
        lexeme i = match("IF");
            //i.left=null;
        lexeme o = match("OPAREN");
            //o.left=null;
        lexeme e = expr();
            //e.left=null;
        lexeme c = match("CPAREN");
            //c.left=null;
        lexeme b = block();
            //b.left=null;
        lexeme oe = optElseStatement();
            //oe.left=null;
        return cons("IFSTATEMENT", i ,cons("JOIN", o, cons("JOIN", e, cons("JOIN", c, cons("JOIN", b, cons("JOIN", oe, null))))));
    }
    
    public lexeme optElseStatement(){
        if (elseStatementPending()){
            lexeme e = elseStatement();
            //e.left=null;
            return cons("OPTELSESTATEMENT", e, null);
        }
        return cons("OPTELSESTATEMENT", null, null);
    }
    
    public lexeme elseStatement(){
        lexeme e = match("ELSE");
            //e.left=null;
        if(blockPending()){
            lexeme b = block();
            //b.left=null;
            return cons("ELSESTATEMENT", e, cons("JOIN", b, null));
        }
        else if(ifStatementPending()){
            lexeme i = ifStatement();
            //i.left=null;
            return cons("ELSESTATEMENT", e, cons("JOIN", i, null));
        }
        else return null;
    }
    
    public lexeme k_lambda(){
        lexeme l = match("LAMBDA");
            //l.left=null;
        lexeme o = match("OPAREN");
            //o.left=null;
        lexeme op = optParamList();
            //op.left=null;
        lexeme c = match("CPAREN");
            //c.left=null;
        lexeme b = block();
            //b.left=null;
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
