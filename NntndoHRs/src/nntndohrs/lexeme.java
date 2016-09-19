//////////////////////////
//      adampluth       //
//      CS 403          //
//      Designer        //
//      Language        //
//      9/18/2016       //
//      (Lexeme)        //
//////////////////////////

package nntndohrs;

import java.lang.reflect.Type;



/**
 *
 * @author adam pluth
 */
 class lexeme implements Type{
     
        String type;
        String string;
        int integer;
        double real;

    lexeme(String t) {
        type=t;
    }
}

