/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
       c=(char) c;
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
                else if(c=='<'|c=='>'|c=='='|c=='='|c=='!'){
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
        if(token.equals("if")||token.equals("mario")){return new lexeme("IF","if");}//if/mario
        else if(token.equals("else")||token.equals("luigi")){return new lexeme("ELSE","else");}//else/luigi
        else if(token.equals("boolean")||token.equals("p1")){return new lexeme("TYPE","boolean");}//boolean/p1
        else if(token.equals("int")||token.equals("pit")){return new lexeme("TYPE","int");}//int/pit
        else if(token.equals("string")||token.equals("diddy")){return new lexeme("TYPE","string");}//int/pit
        else if(token.equals("real")||token.equals("link")){return new lexeme("TYPE","real");}//int/pit
        else if(token.equals("while")||token.equals("kirby")){return new lexeme("WHILE","while");}//while/kirby
        else if(token.equals("return")||token.equals("gameOver")){return new lexeme("RETURN","return");}//return/gameOver
        else if (token.equals("func")){return new lexeme("FUNC", "func");}
        else if (token.equals("nil")){return new lexeme("NIL", "nil");}
        else if (token.equals("true")){return new lexeme("BOOLEAN", "TRUE");}
        else if (token.equals("false")){return new lexeme("BOOLEAN", "FALSE");}
        else if (token.equals("print")){return new lexeme("PRINT", "print");}
        else if (token.equals("array")){return new lexeme("TYPE", "array");}
        else if (token.equals("append")){return new lexeme("APPEND", "append");}
        else if (token.equals("insert")){return new lexeme("INSERT", "insert");}
        else if (token.equals("remove")){return new lexeme("REMOVE", "remove");}
        else if (token.equals("set")){return new lexeme("SET", "set");}
        else if (token.equals("length")){return new lexeme("LENGTH", "length");}
        else if (token.equals("break")){return new lexeme("BREAK", "break");}
        else if (token.equals("lambda")){return new lexeme("LAMBDA", "lambda");}
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