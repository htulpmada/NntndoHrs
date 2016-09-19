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
    public static void main(String[] args) throws FileNotFoundException, IOException {
        src = new PushbackReader(
        new InputStreamReader(
        new FileInputStream(args[0]),
        Charset.forName("UTF-8")));
        //Reader should be ready to read til EOF//
        lex();
    }
    
    public static lexeme lex() throws IOException{    
        skipWhitSpace();
        chr=src.read();
        if(chr!=-1){c=(char)chr;}
        else{return new lexeme("ENDofINPUT");}
        switch(c){
            case ' ': return new lexeme("");
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
    
    
    
}
