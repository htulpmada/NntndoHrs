//#!/bin/bash
//java NntndoHRS.class $*

        
package nntndohrs;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PushbackReader;
import java.nio.charset.Charset;

/**
 *
 * @author Adam Pluth
 */
public class lexer {
    static PushbackReader src;
    static int chr=0;
    static char c=0;
    static String token="";
    static lexeme l;
    static lexeme t;
    static lexeme n;
    static int lineNum=1;
    /**
     * @param file
     * @param go boolean to print lex list or not
     * @throws IOException 
     */
public lexer(String file,boolean go) throws IOException{
        src = new PushbackReader(
            new InputStreamReader(
            new FileInputStream(file),
            Charset.forName("UTF-8")));
        while(chr!=65535){
            l=lex();
            //System.out.println(l);
            if(t==null){t=l;n=t;}
            else{n.left=l;n=n.left;}
        }
    if(go){
        System.out.println(t);
    }
}
public static lexeme lex() throws IOException{    
        
       skipWhitSpace();
       chr=src.read();
       c=(char) chr;
       if(c=='\uffff'){return new lexeme("ENDOFINPUT","\uffff");}
       switch(c){
            case '(': return new lexeme("OPAREN","(");
            case ')': return new lexeme("CPAREN",")");
            case ',': return new lexeme("COMMA",",");
            case '.': return new lexeme("DOT",".");
            case '+': return new lexeme("PLUS","+");
            case '*': return new lexeme("TIMES","*");
            case '{': return new lexeme("OCURLY","{");
            case '}': return new lexeme("CCURLY","}");
            case '[': return new lexeme("OBRACKET","[");
            case ']': return new lexeme("CBRACKET","]");
            case ';': return new lexeme("SEMI",";");
            case '%': return new lexeme("MODULO","%");
            case '|': return new lexeme("OR","|");
            case '^': return new lexeme("POWER","^");
            case '&': return new lexeme("AND","&");

            default:
                //multi char tokens
                if(Character.isDigit(c)){
                    return lexNummber();
                }
                else if(Character.isLetter(c)){
                    return lexVariableorKeyword();
                }
                else if(c=='-'){
                    return lexMinus();
                }
                else if(c=='/'){
                    return lexDivide();
                }
                else if(c=='\"'){
                    return lexString();
                }
                else if(c=='<'|c=='>'|c=='='|c=='!'){
                    return lexOperator();
                }
                else if(c=='@'){skipComment(); return lex();}
                else{//needs to be last
                    return new lexeme("unknown");
                }
        }
    }
       
    private static lexeme lexOperator() throws IOException{
        char b=c;
        c=(char)src.read();
        switch(b){
            case '<':
                if(c=='='){return new lexeme("LESSEQUAL","<=");}
                else{src.unread(c);return new lexeme("LESS","<");}
            case '>':
                if(c=='='){return new lexeme("GREATEREQUAL",">=");}
                else{src.unread(c);return new lexeme("GREATER",">");}
            case '=':
                if(c=='='){return new lexeme("DOUBLEEQUAL","==");}
                else{src.unread(c);return new lexeme("EQUAL","=");}
            case '!':
                if(c=='='){return new lexeme("NOTEQUAL","!=");}
                else{src.unread(c);return new lexeme("NOT","!");}
        }
        return new lexeme("unknown");
    }

    /**
     *  skips all whitespace and pushes back extra char after end of whitespace
     */
    private static void skipWhitSpace() throws IOException {
        chr=src.read();
        c=(char) chr;
        if(c=='\n'){lineNum++;}
        while(Character.isWhitespace(c)){
            chr=src.read();
            c=(char) chr;
            if(c=='\n'){lineNum++;}
        }
       src.unread(c);
    }
    
    /**
     *  skips all chars after '@' on current line
     * '@'=comment
     * @throws IOException 
     */
    private static void skipComment() throws IOException{
        chr=src.read();
        c=(char) chr;
        if(c=='\n'){lineNum++;}
        while(c!='\n'){
            chr=src.read();
            c=(char) chr;
            if(c=='\n'){lineNum++;}
        }
    }
    
    private static lexeme lexVariableorKeyword() throws IOException{
        token="";
        while(Character.isLetter(c)||Character.isDigit(c)||c=='.'){
            token+=c;
            chr=src.read();
            c=(char)chr;
        }
        src.unread(chr);
        //read for keywords
        if(token.equals("if")||token.equals("doubleDragon")){return new lexeme("IF","if");}
        else if(token.equals("else")||token.equals("battleToads")){return new lexeme("ELSE","else");}
        else if(token.equals("boolean")||token.equals("bool")||token.equals("player")){return new lexeme("TYPE","boolean");}
        else if(token.equals("integer")||token.equals("int")||token.equals("mario")){return new lexeme("TYPE","int");}
        else if(token.equals("string")||token.equals("str")||token.equals("luigi")){return new lexeme("TYPE","string");}
        else if(token.equals("real")||token.equals("kirby")){return new lexeme("TYPE","real");}
        else if (token.equals("node")||token.equals("pika")){return new lexeme("TYPE", "node");}//need to document
        else if (token.equals("setNodeV")||token.equals("chu")){return new lexeme("sNodeV", "Vnode");}
        else if (token.equals("getNodeV")||token.equals("iChooseU")){return new lexeme("gNodeV", "Vnode");}
        else if (token.equals("setNodeL")||token.equals("pi")){return new lexeme("sNodeL", "Lnode");}
        else if (token.equals("setNodeR")||token.equals("ka")){return new lexeme("sNodeR", "Rnode");}
        else if (token.equals("getNodeL")||token.equals("iChooseL")){return new lexeme("gNodeL", "Lnode");}
        else if (token.equals("getNodeR")||token.equals("iChooseR")){return new lexeme("gNodeR", "Rnode");}//end of new stuff
        else if(token.equals("while")||token.equals("game")){return new lexeme("WHILE","while");}
        else if(token.equals("return")||token.equals("quit")){return new lexeme("RETURN","return");}
        else if (token.equals("func")||token.equals("yoshi")){return new lexeme("FUNC", "func");}
        else if (token.equals("nil")||token.equals("null")||token.equals("dk")){return new lexeme("NIL", "nil");}
        else if (token.equals("true")||token.equals("bubble")){return new lexeme("BOOLEAN", "TRUE");}
        else if (token.equals("false")||token.equals("bobble")){return new lexeme("BOOLEAN", "FALSE");}
        else if (token.equals("print")||token.equals("pit")){return new lexeme("PRINT", "print");}
        else if (token.equals("array")||token.equals("triforce")){return new lexeme("TYPE", "array");}
        else if (token.equals("append")||token.equals("link")){return new lexeme("APPEND", "append");}
        else if (token.equals("insert")||token.equals("zelda")){return new lexeme("INSERT", "insert");}
        else if (token.equals("remove")||token.equals("gannon")){return new lexeme("REMOVE", "remove");}
        else if (token.equals("set")||token.equals("sheik")){return new lexeme("SET", "set");}
        else if (token.equals("length")||token.equals("size")||token.equals("quest")){return new lexeme("LENGTH", "length");}
        else if (token.equals("break")||token.equals("gameover")){return new lexeme("BREAK", "break");}
        else if (token.equals("lambda")||token.equals("samus")){return new lexeme("LAMBDA", "lambda");}
        else {return new lexeme("ID",token);}
        
    }

    private static lexeme lexString() throws IOException {
        token="";
        chr=src.read();
        c=(char)chr;
//        token+=c;
        while(c!='\"'){
            token+=c;
            chr=src.read();
            c=(char)chr;
            //if (c == '\\'){c = (char) src.read();}
        }
//        token=token.substring(0,token.length());
        return new lexeme("STRING",token);
    }

    private static lexeme lexNummber() throws IOException {
        token="";
        boolean real=false;
        while(Character.isDigit(c)){
            token+=c;
            chr=src.read();
            c=(char)chr;
            if(c=='.'){
                real=true;
                token+=c;
                chr=src.read();
                c=(char)chr;
            }
        }
        src.unread(c);
        if(real){return new lexeme("REAL",token);}
        return new lexeme("INTEGER",token);
    }

    private static lexeme lexMinus() throws IOException {
        token="";
        token+=c;
        c=(char)src.read();
        boolean real=false;
        if(!(Character.isDigit(c))){src.unread(c);return new lexeme("MINUS","-");}
        else{
            while(Character.isDigit(c)){
                token+=c;
                c=(char) src.read();
                    if(c=='.'){
                    real=true;
                    token+=c;
                    chr=src.read();
                    c=(char)chr;
                }
            }
            src.unread(c);
            if(real){return new lexeme("REAL",token);}
            return new lexeme("INTEGER",token);
        }
    }

    private static lexeme lexDivide() throws IOException {
        token="";
        c=(char)src.read();
        if(c=='/'){return new lexeme("INTDIVDE","//");}
        else{src.unread(c);return new lexeme("DIVIDE","/");}
    }
    

}