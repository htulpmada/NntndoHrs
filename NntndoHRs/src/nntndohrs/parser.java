/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nntndohrs;

import java.io.FileNotFoundException;
import java.io.IOException;
import static nntndohrs.NntndoHRs.gameOver;

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
    
    //---------------------------------utility functions-------------------------------------//
    public lexeme parse() {
        lexeme root = pro();
        lexeme eof = match("ENDOFINPUT");
        return cons("PARSE",root,eof);
    }
    
    public static void fatal(String problem){
        System.out.println("\nERROR; "+problem);
        gameOver();
        System.exit(1);
    }
    
    public Boolean check(String type){
        return t.type.equals(type);
    }
    
    public lexeme advance(){
        lexeme old = t;
        t=t.left;
        old.left=null;//not sure if this will cause problems
        //System.out.println(old.type+" "+old.string);
        return old;
    }
    
    public lexeme match(String type){
        if(check(type)){return advance();}
        fatal("Syntax Expected "+type+", Received "+t.type+ " line: "+(t.line-1));//t.line
        return null;
    }
    
    public lexeme cons(String value,lexeme l,lexeme r){
        return new lexeme(value, value, l, r);
    }

    //----------------------------------grammar functions-------------------------------------//
    public lexeme pro(){
        lexeme d= def();
            if(proPending()){
                lexeme p=pro();
                return cons("PRO",d,cons("JOIN",p,null));
            }
            return cons("PRO",d,null);
    }

    public lexeme def(){
        if(vDefPending()){
            lexeme v = varDef();
            return cons("DEF", v, null);
        }
        else if(fDefPending()){
            lexeme f = fDef();
            return cons("DEF", f, null);
        }
        else if(fDefPending()){
            lexeme f = fDef();
            return cons("DEF", f, null);
        }
        else if(exprPending()){
            lexeme f = expr();
            return cons("DEF", f, null);//was f;
        }
        return null;
    }
    
    public lexeme varDef() {
        lexeme v = match("TYPE");
        lexeme i = match("ID");
        lexeme eq = match("EQUAL");
        lexeme e = expr();
        lexeme s = match("SEMI");
        return cons("VDEF", v, cons("JOIN", i, cons("JOIN", eq, cons("JOIN", e, cons("JOIN", s, null)))));
    }

    public lexeme fDef(){
        lexeme f = match("FUNC");
        lexeme e = match("ID");
        if(check("EQUAL")){//lambdas here<-------
            lexeme eq = match("EQUAL");
            lexeme l = lambda();
            lexeme s = match("SEMI");
            lexeme o = null;
            lexeme op = l.right.right.left;
            lexeme c = null;
            lexeme b = l.right.right.right.right.left;
            return cons("FDEF", f, cons("JOIN", e, cons("JOIN", o, cons("JOIN", op, cons("JOIN", c, cons("JOIN", b, null))))));
        }
        else{
            lexeme o = match("OPAREN");
            lexeme op = optPList();
            lexeme c = match("CPAREN");
            lexeme b = block();
            return cons("FDEF", f, cons("JOIN", e, cons("JOIN", o, cons("JOIN", op, cons("JOIN", c, cons("JOIN", b, null))))));
        }
    }
    
    public lexeme idDef(){
        lexeme i = match("ID");
        if (check("OPAREN")){
            lexeme o = match("OPAREN");
            lexeme e = optExprList();
            lexeme c = match("CPAREN");
            return cons("FCALL", i, cons("JOIN", o, cons("JOIN", e, cons("JOIN", c, null))));
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
    
    public lexeme optPList(){
        if(pListPending()){
            lexeme p = pList();
            return cons("OPTPLIST", p, null);
        }
        return cons("OPTPLIST", null, null);
    }
    
    public lexeme pList(){
        lexeme i = match("ID");
        if (check("COMMA")){
            lexeme c = match("COMMA");
            lexeme p = pList();
            return cons("PLIST", i,p);
        }
        return cons("PLIST", i, null);
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
            return cons("EXPRLIST", e, ex);
        }
        return cons("EXPRLIST", e, null);
    }

     public lexeme expr(){
        lexeme u = unary();
        if(opPending()){
            lexeme o = op();
            lexeme e = expr();
            return cons("EXPR", new lexeme("OP", o.type, u, e),null);
        }     
    return cons("EXPR", u, null);
    }    
    
    public lexeme unary(){
        if (idDefPending()){
            lexeme p = idDef();
            return cons("UNARY", p, null);
        }
        else if(check("STRING")){
            lexeme p = match("STRING");
            return cons("UNARY", p, null);
        }
        else if(check("INTEGER")){
            lexeme p = match("INTEGER");
            return cons("UNARY", p, null);
        }
        else if(check("REAL")){
            lexeme p = match("REAL");
            return cons("UNARY", p, null);
        }
        else if (check("NOT")){
            lexeme n = match("NOT");
            lexeme p = unary();
            return cons("UNARY", n, cons("JOIN", p, null));
        }
        else if (check("OPAREN")){
            lexeme o = match("OPAREN");
            lexeme e = expr();
            lexeme c = match("CPAREN");
            return cons("UNARY", o, cons("JOIN", e, cons("JOIN", c, null)));
        }
        else if (lambdaPending()){
            lexeme p = lambda();
            return cons("UNARY", p, null);
        }
        else if (fDefPending()){
            lexeme p = fDef();
            return cons("UNARY", p, null);
        }
        else if (check("OBRACKET")){//array initializer
            lexeme o = match("OBRACKET");
            lexeme e = optExprList();
            lexeme c = match("CBRACKET");
            return cons("UNARY", o, cons("JOIN", e, cons("JOIN", c, null)));
        }
        else if (check("NIL")){
            lexeme n = match("NIL");
            return cons("UNARY", n, null);
        }
        else if (check("BOOLEAN")){
            lexeme b = match("BOOLEAN");
            return cons("UNARY", b, null);
        }
        else if (check("PRINT")){
            lexeme f = match("PRINT");
            lexeme o = match("OPAREN");
            lexeme e = exprList();
            lexeme c = match("CPAREN");
            return cons("PRINT", f, cons("JOIN", e, null));
        }
        else if (check("BREAK")){
            lexeme b = match("BREAK");
            //lexeme s = match("SEMI");
            return b;//cons("BREAK", b, s);
        }
        else if (check("APPEND")){
            lexeme f = match("APPEND");
            lexeme o = match("OPAREN");
            lexeme e = exprList();
            lexeme c = match("CPAREN");
            return cons("APPEND", f,cons("JOIN", e, null));
        }
        else if (check("INSERT")){
            lexeme f = match("INSERT");
            lexeme o = match("OPAREN");
            lexeme e = exprList();
            lexeme c = match("CPAREN");
            return cons("INSERT", f, cons("JOIN", e, null));
        }
        else if (check("REMOVE")){
            lexeme f = match("REMOVE");
            lexeme o = match("OPAREN");
            lexeme e = exprList();
            lexeme c = match("CPAREN");
            return cons("REMOVE", f, cons("JOIN", e, null));
        }
        else if (check("SET")){
            lexeme f = match("SET");
            lexeme o = match("OPAREN");
            lexeme e = exprList();
            lexeme c = match("CPAREN");
            return cons("SET", f, cons("JOIN", e, null));
        }
        else if (check("LENGTH")){
            lexeme f = match("LENGTH");
            lexeme o = match("OPAREN");
            lexeme e = exprList();
            lexeme c = match("CPAREN");
            return cons("LENGTH", f, cons("JOIN", e,null));
        }
        else return null;
    }
    
    public lexeme op(){
        if(check("EQUAL")){
            lexeme op = match("EQUAL");
            return cons("EQUAL", op, null);
        }
        else if(check("DOUBLEEQUAL")){
            lexeme op = match("DOUBLEEQUAL");
            return cons("DOUBLEEQUAL", op, null);
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
        else if(check("TIMES")){
            lexeme op = match("TIMES");
            return cons("TIMES", op, null);
        }
        else if(check("DIVIDE")){
            lexeme op = match("DIVIDE");
            return cons("DIVIDE", op, null);
        }
        else if(check("INTDIVIDE")){
            lexeme op = match("INTDIVIDE");
            return cons("INTDIVIDE", op, null);
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
        else return null;
    }
    
     public lexeme block(){
        lexeme o = match("OCURLY");
        lexeme s = optStateList();
        lexeme c = match("CCURLY");
        return cons("BLOCK", o, cons("JOIN", s, cons("JOIN", c, null)));
     }
    
    public lexeme optStateList(){
        if (stateListPending()){
            lexeme s = stateList();
            return cons("OPTSTATELIST", s, null);
        }
    return cons("OPTSTATELIST", null, null);
    }
     
    public lexeme stateList(){
        lexeme s = state();
        if(stateListPending()){
            lexeme sl = stateList();
            return cons("STATELIST", s, cons("JOIN", sl, null));
        }
    return cons("STATELIST", s, null);
    }    
    
    public lexeme state(){
        if(vDefPending()){
            lexeme v = varDef();
            return cons("STATE", v, null);
        }
        else if(fDefPending()){
            lexeme f = fDef();
            return cons("STATE", f, null);
        }
        else if(exprPending()){
            lexeme e = expr();
            lexeme s = match("SEMI");
            return cons("STATE", e, cons("JOIN", s, null));
        }
        else if(whileLoopPending()){
            lexeme w = whileLoop();
            return cons("STATE", w, null);
        }
        else if(ifStatePending()){
            lexeme i = ifState();
            return cons("STATE", i, null);
        }
        else if(check("RETURN")){
            lexeme r = match("RETURN");
            lexeme e = expr();
            lexeme s = match("SEMI");
        return cons("STATE", r ,cons("JOIN", e, cons("JOIN", s, null)));
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
    
    public lexeme ifState(){
        lexeme i = match("IF");
        lexeme o = match("OPAREN");
        lexeme e = expr();
        lexeme c = match("CPAREN");
        lexeme b = block();
        lexeme oe = optElseState();
        return cons("IFSTATE", i ,cons("JOIN", o, cons("JOIN", e, cons("JOIN", c, cons("JOIN", b, cons("JOIN", oe, null))))));
    }
    
    public lexeme optElseState(){
        if (elseStatePending()){
            lexeme e = elseState();
            return cons("OPTELSESTATE", e, null);
        }
        return cons("OPTELSESTATE", null, null);
    }
    
    public lexeme elseState(){
        lexeme e = match("ELSE");
        if(blockPending()){
            lexeme b = block();
            return cons("ELSESTATE", e, cons("JOIN", b, null));
        }
        else if(ifStatePending()){
            lexeme i = ifState();
            return cons("ELSESTATE", e, cons("JOIN", i, null));
        }
        else return null;
    }
    
    public lexeme lambda(){
        lexeme l = match("LAMBDA");
        lexeme o = match("OPAREN");
        lexeme op = optPList();
        lexeme c = match("CPAREN");
        lexeme b = block();
        return cons("LAMBDA", l ,cons("JOIN", o, cons("JOIN", op, cons("JOIN", c, cons("JOIN", b, null)))));
    }
//--------------------------------------------------------pending functions------------------------------------------------------//
    public boolean proPending(){return defPending();}
    public boolean defPending(){return vDefPending() | fDefPending() | idDefPending();}
    public boolean vDefPending(){return check("TYPE");}
    public boolean fDefPending(){return check("FUNC");}
    public boolean idDefPending(){return check("ID");}
    public boolean pListPending(){return check("ID");}
    public boolean exprListPending(){return exprPending();}
    public boolean exprPending(){return unaryPending();}
    public boolean blockPending(){return check("OCURLY");}
    public boolean stateListPending(){return statePending();}
    public boolean statePending(){return vDefPending() | fDefPending() | exprPending() | whileLoopPending() | ifStatePending() | check("RETURN");}//| check("BREAK");}
    public boolean whileLoopPending(){return check("WHILE");}
    public boolean ifStatePending(){return check("IF");}
    public boolean elseStatePending(){return check("ELSE");}
    public boolean lambdaPending(){return check("LAMBDA");}
    public boolean unaryPending(){
        return idDefPending() 
                | check("STRING") 
                | check("INTEGER") 
                | check("NOT") 
                | check("OPAREN") 
                | lambdaPending() 
                | fDefPending() 
                | check("OBRACKET") 
                | check("NIL") 
                | check("BOOLEAN") 
                | check("PRINT") 
                | check("APPEND") 
                | check("INSERT") 
                | check("REMOVE") 
                | check("SET") 
                | check("BREAK") 
                | check("LENGTH");
        //^^^^^^^^^^^BUILT-INS go here^^^^^^//
    }
    public boolean opPending(){
        return check("EQUAL") 
                | check("NOTEQUAL") 
                | check("GREATER") 
                | check("LESS") 
                | check("GREATEREQUAL") 
                | check("LESSEQUAL") 
                | check("PLUS") 
                | check("MINUS") 
                | check("TIMES") 
                | check("DIVIDE") 
                | check("INTDIVIDE") 
                | check("POWER") 
                | check("AND") 
                | check("OR") 
                | check("ASSIGN") 
                | check("DOUBLEEQUAL");
    }
   
    public boolean builtInPending() {
        return check("print");
        //| check();
    }// | check() all builut in functions


}
