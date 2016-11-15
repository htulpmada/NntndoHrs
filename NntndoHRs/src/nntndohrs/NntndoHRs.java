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
static lexeme tree;
String file="";
static evaluator eval_er;
    public static void main(String[] args) throws FileNotFoundException, IOException {
        try{String file = args[0]; n=new lexer(file,true);}
        catch(ArrayIndexOutOfBoundsException e){n=new lexer("file.txt",true);}
        try{String file = args[0]; p=new parser();}
        catch(ArrayIndexOutOfBoundsException e){p=new parser();}
        tree=p.parse();
        //System.out.println(tree);
        eval_er=new evaluator();
        
    }
}
