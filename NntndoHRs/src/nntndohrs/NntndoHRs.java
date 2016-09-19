//////////////////////////
//      adampluth       //
//      CS 403          //
//      Designer        //
//      Language        //
//      9/18/2016       //
//      (Main)          //
//////////////////////////

package nntndohrs;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Scanner;

/**
 *
 * @author adam pluth
 */
public class NntndoHRs {        
static Scanner src;
    public static void main(String[] args) throws FileNotFoundException, IOException {
        BufferedReader reader = new BufferedReader(
        new InputStreamReader(
        new FileInputStream(args[0]),
        Charset.forName("UTF-8")));
        int c=reader.read();
        
        char character = (char) c;
        lexeme l= lex(character);
    }
    
    public static lexeme lex(char c){    
        
    }

    private void skipWhitSpace() {
      
    }
    
}
