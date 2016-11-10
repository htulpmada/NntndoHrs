/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nntndohrs;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PushbackReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import static nntndohrs.lexer.lex;

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
        advance();
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

    public lexeme definition(){}



    //pending functions
    public boolean programPending(){return definitionPending();}
    public boolean definitionPending(){return variableDefinitionPending() | functionDefinitionPending() | idDefPending();}
    public boolean variableDefinitionPending(){return check("TYPE");}
    public boolean functionDefinitionPending(){return check("FUNC");}
    public boolean idDefPending(){return check("ID");}
    public boolean pListPending(){return check("ID");}
    public boolean eprListPending(){return exprPending();}
    public boolean exprPending(){return unaryPending();}
    public boolean unaryPending(){return idDefPending() | check("STRING") | check("INTEGER") | check("NOT") | check("NOT") | check("OPAREN") | check("OPAREN") | k_lambdaPending() | functionDefinitionPending() | check("OBRACKET") | check("NIL") | check("BOOLEAN") | check("PRINT") | check("APPEND") | check("INSERT") | check("REMOVE") | check("SET") | check("LENGTH");}
    public boolean opPending(){return check("EQUAL") | check("NOTEQUAL") | check("GREATER") | check("LESS") | check("GREATEREQUAL") | check("LESSEQUAL") | check("PLUS") | check("MINUS") | check("MULTIPLY") | check("DIVIDE") | check("INTEGERDIVIDE") | check("POWER") | check("AND") | check("OR") | check("ASSIGN") | check("DOUBLEEQUAL");}
    public boolean blockPending(){return check("OBRACE");}
    public boolean statementListPending(){return statementPending();}
    public boolean statementPending(){return variableDefinitionPending() | functionDefinitionPending() | exprPending() | whileLoopPending() | ifStatementPending() | check("RETURN");}
    public boolean whileLoopPending(){return check("while");}
    public boolean ifStatementPending(){return check("IF");}
    public boolean elseStatementPending(){return check("ELSE");}
    public boolean k_lambdaPending(){return check("LAMBDA");}

}
