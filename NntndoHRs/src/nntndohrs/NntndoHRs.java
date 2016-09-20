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
    public static void main(String[] args) throws FileNotFoundException, IOException {
        src = new PushbackReader(
        new InputStreamReader(
        new FileInputStream(args[0]),
        Charset.forName("UTF-8")));
        while(chr!=65535){l=lex();System.out.println(l);}
    }
    
    public static lexeme lex() throws IOException{    
        
       skipWhitSpace();
       chr=src.read();
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
        else if(token.equals("boolean")){}//boolean/p1
        else if(token.equals("break")){}//break/DK
        else if(token.equals("byte")){}//byte/pacman
        else if(token.equals("")){}//case/ketchum
        else if(token.equals("")){}//char/toadstool
        else if(token.equals("")){}//continue/ness
        else if(token.equals("")){}//default/pikachu
        else if(token.equals("")){}//else/luigi
        else if(token.equals("")){}//for/yoshi
        else if(token.equals("")){}//if/mario
        else if(token.equals("int")||token.equals("pit")){return new lexeme("INTEGER_TYPE");}//int/pit
        else if(token.equals("")){}//new/startGame
        else if(token.equals("")){}//return/gameOver
        else if(token.equals("")){}//switch/turn
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
