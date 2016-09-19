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
static int chr;
static char c;
static String token="";
static lexeme l;
    public static void main(String[] args) throws FileNotFoundException, IOException {
        src = new PushbackReader(
        new InputStreamReader(
        new FileInputStream(args[0]),
        Charset.forName("UTF-8")));
        //Reader should be ready to read til EOF//
        chr=src.read();
        while(chr!=-1){l=lex();System.out.println(l);}
        //lex();
    }
    
    public static lexeme lex() throws IOException{    
        skipWhitSpace();
        chr=src.read();
        if(chr!=-1){c=(char)chr;}
        else{return new lexeme("ENDofINPUT");}
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
                    src.unread(c);
                    return lexNummber();
                }
                else if(Character.isLetter(c)){
                    src.unread(c);
                    return lexVariableorKeyword();
                }
                else if(c=='\"'){//might be wrong needs to be just " accomidate for escape chars
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
        int ch=src.read();
        if(ch==-1){src.unread(ch);return;}
        int cw=(char) ch;
        while(Character.isWhitespace(cw)){
            ch=src.read();
            if(ch==-1){src.unread(ch);return;}
            cw=(char) ch;
        }
        src.unread(cw);
    }
    
    private static lexeme lexVariableorKeyword() throws IOException{
        char ch;
        int i;
        token="";
        i=src.read();
        if(i==-1){src.unread(i);return new lexeme("ENDofINPUT");}
        ch=(char)i;
        token+=ch;
        while(Character.isLetter(ch)||Character.isDigit(ch)){
            i=src.read();
            if(i==-1){break;}
            ch=(char)i;
            token+=ch;
        }
        src.unread(i);
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
        else if(token.equals("")){}//int/pit
        else if(token.equals("")){}//new/startGame
        else if(token.equals("")){}//return/gameOver
        else if(token.equals("")){}//switch/turn
        else if(token.equals("")){}//while/kirby
        else {return new lexeme("VARIABLE",token);}
        
        return new lexeme(token);//probably wrong
    }

    private static lexeme lexString() throws IOException {
        char ch;
        int i;
        token="";
        i=src.read();
        if(i==-1){src.unread(i);return new lexeme("ENDofINPUT");}
        ch=(char)i;
        token+=ch;
        while(ch!='"'){
            i=src.read();
            if(i==-1){break;}
            ch=(char)i;
            token+=ch;
        }
        src.unread(i);
        return new lexeme("STRING",token);
    }

    private static lexeme lexNummber() throws IOException {
        char ch;
        int i;
        token="";
        i=src.read();
        if(i==-1){src.unread(i);return new lexeme("ENDofINPUT");}
        ch=(char)i;
        token+=ch;
        while(Character.isDigit(ch)){
            i=src.read();
            if(i==-1){break;}
            ch=(char)i;
            if(!Character.isDigit(ch)){break;}
            token+=ch;
        }
        src.unread(i);
        return new lexeme("INTEGER",Integer.parseInt(token));
    }
    
}
