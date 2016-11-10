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
import java.util.ArrayList;

/**
 *
 * @author adam pluth
 */
public class NntndoHRs {        
static lexer n;
static parser p;
static ArrayList<lexeme> tree;
String file="";
    public static void main(String[] args) throws FileNotFoundException, IOException {
        try{String file = args[0]; n=new lexer(file,false);}
        catch(ArrayIndexOutOfBoundsException e){n=new lexer("file.txt",false);}
        try{String file = args[0]; p=new parser(file,true);}
        catch(ArrayIndexOutOfBoundsException e){p=new parser("file.txt",true);}
        tree=p.parse();
    }
}
