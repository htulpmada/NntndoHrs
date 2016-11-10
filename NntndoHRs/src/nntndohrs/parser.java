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
static ArrayList<lexeme> tree;
//static PushbackReader src;
static int chr=0;
static char c=0;
static String token="";
static lexeme l;
static lexeme t;
static lexeme n;

    public parser(String file,boolean go) throws FileNotFoundException, IOException {
        
    //src = new PushbackReader(
    //    new InputStreamReader(
    //    new FileInputStream(file),
    //    Charset.forName("UTF-8")));
        if(go){
            while(chr!=65535){
                l=lex();
                System.out.println(l);
                if(t==null){t=l;n=t;}
                else{n.left=l;n=n.left;}
                if(l.type.equals("ENDOFINPUT")){break;}
            }
            System.out.println(t);
        }
    }
    public lexeme parse() {
        lexeme tree=null;
        
        return tree;
    }
    
}
