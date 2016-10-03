//////////////////////////
//      adampluth       //
//      CS 403          //
//      Designer        //
//      Language        //
//      9/18/2016       //
//      (Main)          //
//////////////////////////

package nntndohrs;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PushbackReader;
import java.nio.charset.Charset;

/**
 *
 * @author adam pluth
 */
public class NntndoHRs {        
static PushbackReader src;
static int chr=0;
static char c=0;
static String token="";
static lexeme l;
static lexeme t;
static lexeme n;

    public static void main(String[] args) throws FileNotFoundException, IOException {
        src = new PushbackReader(
        new InputStreamReader(
        new FileInputStream(args[0]),
        Charset.forName("UTF-8")));
        while(chr!=65535){
            l=lex();
            //System.out.println(l);
            if(t==null){t=l;n=t;}
            else{n.left=l;n=n.left;}
        }
        System.out.println(t);
    }
    
    public static lexeme lex() throws IOException{    
        
       skipWhitSpace();
       chr=src.read();
       c=(char) c;
       if(c=='\uffff'){return new lexeme("ENDofINPUT");}
       switch(c){
            case '(': return new lexeme("OPAREN");
            case ')': return new lexeme("CPAREN");
            case ',': return new lexeme("COMMA");
            case '.': return new lexeme("DOT");
            case '+': return new lexeme("PLUS");
            case '-': return new lexeme("MINUS");
            case '*': return new lexeme("TIMES");
            case '/': return new lexeme("DIVIDE");
            case '{': return new lexeme("OCURLY");
            case '}': return new lexeme("CCURLY");
            case '[': return new lexeme("OBRACKET");
            case ']': return new lexeme("CBRACKET");
            case ';': return new lexeme("SEMICOLON");
            case '=': return new lexeme("ASSIGN");//==?
            case '%': return new lexeme("MODULOS");
            case '>': return new lexeme("GREATERTHAN");
            case '<': return new lexeme("LESSTHAN");
            //case ' ': return new lexeme("");
            //case ' ': return new lexeme("");
            
            
            default:
                //multi char tokens
                if(Character.isDigit(c)){
                    return lexNummber();
                }
                else if(Character.isLetter(c)){
                    return lexVariableorKeyword();
                }
                else if(c=='\"'){
                    return lexString();
                }
                else{//needs to be last
                    return new lexeme("unknown");
                }
        }
    }

    /**
     *  skips all whitespace and pushes back extra char after end of whitespace
     */
    private static void skipWhitSpace() throws IOException {
        chr=src.read();
        c=(char) chr;
        while(Character.isWhitespace(c)){
            chr=src.read();
            c=(char) chr;
        }
       src.unread(c);
    }
    
    private static lexeme lexVariableorKeyword() throws IOException{
        token="";
        while(Character.isLetter(c)||Character.isDigit(c)){
            token+=c;
            chr=src.read();
            c=(char)chr;
        }
        src.unread(chr);
        //read for keywords
        if(token.equals("if")){}//if
        else if(token.equals("boolean")||token.equals("p1")){return new lexeme("BOOLEAN_TYPE");}//boolean/p1
        else if(token.equals("break")||token.equals("DK")){return new lexeme("BREAK");}//break/DK
        else if(token.equals("byte")||token.equals("pacman")){return new lexeme("BYTE");}//byte/pacman
        else if(token.equals("case")||token.equals("ketchum")){return new lexeme("CASE");}//case/ketchum
        else if(token.equals("char")||token.equals("toadstool")){return new lexeme("CHAR");}//char/toadstool
        else if(token.equals("continue")||token.equals("ness")){return new lexeme("CONTINUE");}//continue/ness
        else if(token.equals("default")||token.equals("pikachu")){return new lexeme("DEFAULT");}//default/pikachu
        else if(token.equals("else")||token.equals("luigi")){return new lexeme("ELSE");}//else/luigi
        else if(token.equals("for")||token.equals("yoshi")){return new lexeme("FOR");}//for/yoshi
        else if(token.equals("if")||token.equals("mario")){return new lexeme("IF");}//if/mario
        else if(token.equals("int")||token.equals("pit")){return new lexeme("INTEGER_TYPE");}//int/pit
        else if(token.equals("new")||token.equals("startGame")){return new lexeme("NEW");}//new/startGame
        else if(token.equals("return")||token.equals("gameOver")){return new lexeme("RETURN");}//return/gameOver
        else if(token.equals("switch")||token.equals("turn")){return new lexeme("SWITCH");}//switch/turn
        else if(token.equals("while")||token.equals("pit")){return new lexeme("WHILE");}//while/kirby
        else {return new lexeme("VARIABLE",token);}
        
        return new lexeme(token);//probably wrong
    }

    private static lexeme lexString() throws IOException {
        token="\"";
        chr=src.read();
        c=(char)chr;
        token+=c;
        while(c!='\"'){
            chr=src.read();
            c=(char)chr;
            token+=c;
        }
        return new lexeme("STRING",token);
    }

    private static lexeme lexNummber() throws IOException {
        token="";
        while(Character.isDigit(c)){
            token+=c;
            chr=src.read();
            c=(char)chr;
        }
        src.unread(chr);
        return new lexeme("INTEGER",token);
    }
    
}
