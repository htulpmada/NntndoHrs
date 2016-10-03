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
     
        String type=null;
        String string=null;
        int integer=0;
        double real=0;
        lexeme right=null;
        lexeme left=null;

    lexeme(String t) {
        type=t;
    }

    lexeme(String t, int value) {
        type=t;
        integer=value;
    }
    
    lexeme(String t, String value) {
        type=t;
        string=value;
    }
    public String toString(){
        String s="";
        if(type!=null){s+=type+" ";}
        if(string!=null){s+=string+" ";}
        if(left!=null){s+="\n"+left.toString();}
        if(right!=null){s+="\n"+right.toString();}
        //try{s+=integer+" ";}
        //catch(Exception e){}
        //try{s+=real+" ";}
        //catch(Exception e){}
        return s;
    }
}

