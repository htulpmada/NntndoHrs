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
    
    //--------------utility functions---------------//
    public lexeme parse() {
        //advance();
        lexeme root = pro();
        lexeme eof = match("ENDOFINPUT");
        return cons("PARSE",root,eof);
    }
    
    public static void fatal(String problem){
        System.out.println("\nERROR; "+problem);
        System.exit(1);
    }
    
    public Boolean check(String type){
        return t.type.equals(type);
    }
    
    public lexeme advance(){
        lexeme old = t;
        t=t.left;
        old.left=null;//not sure if this will cause problems
        System.out.println(old.type+" "+old.string);
        return old;
    }
    
    public lexeme match(String type){
        if(check(type)){return advance();}
        fatal("Syntax Expected "+type+" , Received "+t.type);//t.line
        return null;
    }
    
    public lexeme cons(String value,lexeme l,lexeme r){
        return new lexeme(value, value, l, r);
    }

    //------------------------grammar functions------------------------------//
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
            //v.left=null;
            return cons("DEF", v, null);
        }
        else if(fDefPending()){
            lexeme f = fDef();
            //f.left=null;
            return cons("DEF", f, null);
        }
        else if(idDefPending()){
            lexeme f = idDef();
            return f;
        }
        return null;//idDef should be here
    }
    
    public lexeme varDef() {
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
        return cons("VDEF", v, cons("JOIN", i, cons("JOIN", eq, cons("JOIN", e, cons("JOIN", s, null)))));
    }

    public lexeme fDef(){
        lexeme f = match("FUNC");
            //f.left=null;
        lexeme e = match("ID");
            //e.left=null;
        lexeme o = match("OPAREN");
            //o.left=null;
        lexeme op = optPList();
            //op.left=null;
        lexeme c = match("CPAREN");
            //c.left=null;
        lexeme b = block();
            //b.left=null;
        return cons("FDEF", f, cons("JOIN", e, cons("JOIN", o, cons("JOIN", op, cons("JOIN", c, cons("JOIN", b, null))))));
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
            return cons("FCALL", i, cons("JOIN", o, cons("JOIN", e, cons("JOIN", c, null))));
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
    
    public lexeme optPList(){
        if(pListPending()){
            lexeme p = pList();
            //p.left=null;
            return cons("OPTPLIST", p, null);
        }
        return cons("OPTPLIST", null, null);
    }
    
    public lexeme pList(){
        lexeme i = match("ID");
            //i.left=null;
        if (check("COMMA")){
            lexeme c = match("COMMA");
            //c.left=null;
            lexeme p = pList();
            //p.left=null;
            return cons("PLIST", i, cons("JOIN", c, cons("JOIN", p, null)));
        }
        return cons("PLIST", i, null);
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
            lexeme o = op();
            //o.left=null;
            lexeme e = expr();
            //e.left=null;
            return new lexeme("EXPR", "EXPR", new lexeme("OP", o.type, p, e),null);
        }     
    return cons("EXPR", p, null);
    }    
    
    public lexeme unary(){
        if (idDefPending()){
            lexeme p = idDef();
            //p.left=null;
            return cons("UNARY", p, null);
        }
        else if(check("STRING")){
            lexeme p = match("STRING");
            //p.left=null;
            return cons("UNARY", p, null);
        }
        else if(check("INTEGER")){
            lexeme p = match("INTEGER");
            //p.left=null;
            return cons("UNARY", p, null);
        }
        else if (check("NOT")){
            lexeme n = match("NOT");
            //n.left=null;
            lexeme p = unary();
            //p.left=null;
            return cons("UNARY", n, cons("JOIN", p, null));
        }
        else if (check("OPAREN")){
            lexeme o = match("OPAREN");
            //o.left=null;
            lexeme e = expr();
            //e.left=null;
            lexeme c = match("CPAREN");
            //c.left=null;
            return cons("UNARY", o, cons("JOIN", e, cons("JOIN", c, null)));
        }
        else if (lambdaPending()){
            lexeme p = lambda();
            //p.left=null;
            return cons("UNARY", p, null);
        }
        else if (fDefPending()){
            lexeme p = fDef();
            //p.left=null;
            return cons("UNARY", p, null);
        }
        else if (check("OBRACKET")){
            lexeme o = match("OBRACKET");
            //o.left=null;
            lexeme e = optExprList();
            //e.left=null;
            lexeme c = match("CBRACKET");
            //c.left=null;
            return cons("UNARY", o, cons("JOIN", e, cons("JOIN", c, null)));
        }
        else if (check("NIL")){
            lexeme n = match("NIL");
            //n.left=null;
            return cons("UNARY", n, null);
        }
        else if (check("BOOLEAN")){
            lexeme b = match("BOOLEAN");
            //b.left=null;
            return cons("UNARY", b, null);
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
    
    public lexeme op(){
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
        else if(check("TIMES")){
            lexeme op = match("TIMES");
            //op.left=null;
            return cons("TIMES", op, null);
        }
        else if(check("DIVIDE")){
            lexeme op = match("DIVIDE");
            //op.left=null;
            return cons("DIVIDE", op, null);
        }
        else if(check("INTDIVIDE")){
            lexeme op = match("INTDIVIDE");
            //op.left=null;
            return cons("INTDIVIDE", op, null);
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
        else if(check("EQUAL")){
            lexeme op = match("EQUAL");
            //op.left=null;
            return cons("EQUAL", op, null);
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
        lexeme s = optStateList();
            //s.left=null;
        lexeme c = match("CCURLY");
            //c.left=null;
        return cons("BLOCK", o, cons("JOIN", s, cons("JOIN", c, null)));
     }
    
    public lexeme optStateList(){
        if (stateListPending()){
            lexeme s = stateList();
            //s.left=null;
            return cons("OPTSTATELIST", s, null);
        }
    return cons("OPTSTATELIST", null, null);
    }
     
    public lexeme stateList(){
        lexeme s= state();
            //s.left=null;
        if(stateListPending()){
            lexeme sl = stateList();
            //sl.left=null;
            return cons("STATELIST", s, cons("JOIN", sl, null));
        }
    return cons("STATELIST", s, null);
    }    
    
    public lexeme state(){
        if(vDefPending()){
            lexeme v = varDef();
            //v.left=null;
            return cons("STATE", v, null);
        }
        else if(fDefPending()){
            lexeme f = fDef();
            //f.left=null;
            return cons("STATE", f, null);
        }
        else if(exprPending()){
            lexeme e = expr();
            //e.left=null;
            lexeme s = match("SEMI");
            //s.left=null;
            return cons("STATE", e, cons("JOIN", s, null));
        }
        else if(whileLoopPending()){
            lexeme w = whileLoop();
            //w.left=null;
            return cons("STATE", w, null);
        }
        else if(ifStatePending()){
            lexeme i = ifState();
            //i.left=null;
            return cons("STATE", i, null);
        }
        else if(check("RETURN")){
            lexeme r = match("RETURN");
            //r.left=null;
            lexeme e = expr();
            //e.left=null;
            lexeme s = match("SEMI");
            //s.left=null;
        return cons("STATE", r ,cons("JOIN", e, cons("JOIN", s, null)));
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
    
    public lexeme ifState(){
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
        lexeme oe = optElseState();
            //oe.left=null;
        return cons("IFSTATE", i ,cons("JOIN", o, cons("JOIN", e, cons("JOIN", c, cons("JOIN", b, cons("JOIN", oe, null))))));
    }
    
    public lexeme optElseState(){
        if (elseStatePending()){
            lexeme e = elseState();
            //e.left=null;
            return cons("OPTELSESTATE", e, null);
        }
        return cons("OPTELSESTATE", null, null);
    }
    
    public lexeme elseState(){
        lexeme e = match("ELSE");
            //e.left=null;
        if(blockPending()){
            lexeme b = block();
            //b.left=null;
            return cons("ELSESTATE", e, cons("JOIN", b, null));
        }
        else if(ifStatePending()){
            lexeme i = ifState();
            //i.left=null;
            return cons("ELSESTATE", e, cons("JOIN", i, null));
        }
        else return null;
    }
    
    public lexeme lambda(){
        lexeme l = match("LAMBDA");
            //l.left=null;
        lexeme o = match("OPAREN");
            //o.left=null;
        lexeme op = optPList();
            //op.left=null;
        lexeme c = match("CPAREN");
            //c.left=null;
        lexeme b = block();
            //b.left=null;
        return cons("LAMBDA", l ,cons("JOIN", o, cons("JOIN", op, cons("JOIN", c, cons("JOIN", b, null)))));
    }
    //pending functions
    public boolean proPending(){return defPending();}
    public boolean defPending(){return vDefPending() | fDefPending() | idDefPending();}
    public boolean vDefPending(){return check("TYPE");}
    public boolean fDefPending(){return check("FUNC");}
    public boolean idDefPending(){return check("ID");}
    public boolean pListPending(){return check("ID");}
    public boolean exprListPending(){return exprPending();}
    public boolean exprPending(){return unaryPending();}
    public boolean unaryPending(){return idDefPending() | check("STRING") | check("INTEGER") | check("NOT") | check("OPAREN") | lambdaPending() | fDefPending() | check("OBRACKET") | check("NIL") | check("BOOLEAN") | check("PRINT") | check("APPEND") | check("INSERT") | check("REMOVE") | check("SET") | check("LENGTH");}
    public boolean opPending(){return check("EQUAL") | check("NOTEQUAL") | check("GREATER") | check("LESS") | check("GREATEREQUAL") | check("LESSEQUAL") | check("PLUS") | check("MINUS") | check("TIMES") | check("DIVIDE") | check("INTDIVIDE") | check("POWER") | check("AND") | check("OR") | check("ASSIGN") | check("DOUBLEEQUAL");}
    public boolean blockPending(){return check("OCURLY");}
    public boolean stateListPending(){return statePending();}
    public boolean statePending(){return vDefPending() | fDefPending() | exprPending() | whileLoopPending() | ifStatePending() | check("RETURN");}
    public boolean whileLoopPending(){return check("WHILE");}
    public boolean ifStatePending(){return check("IF");}
    public boolean elseStatePending(){return check("ELSE");}
    public boolean lambdaPending(){return check("LAMBDA");}
}
