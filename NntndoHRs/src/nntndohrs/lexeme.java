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
import java.util.ArrayList;

//need to add: evalVariable() get/set
//          : line numbers

/**
 *
 * @author adam pluth
 */
 class lexeme implements Type{
     
        String type=null;
        String string=null;
        boolean bool;
        int line=0;
        double real=0;
        lexeme right=null;
        lexeme left=null;
        ArrayList<lexeme> strings;

    lexeme(String t) {
        type=t;
        getline();
    }

    lexeme(String t, String value, lexeme l, lexeme r) {
        type=t;
        string=value;
        left=l;
        right=r;
        getline();
    }
    lexeme(String t, String value) {
        type=t;
        string=value;
        getline();
    }
    public void getline(){line=lexer.lineNum;}
    
    public String toString(){
        String s="";
        if(type!=null){s+=type+" ";}
        if(string!=null){s+=string+" "+"line #: "+line;}
        if(left!=null){s+="\nLeft: "+left.toString();}
        if(right!=null){s+="\nRight: "+right.toString();}
        //try{s+=integer+" ";}
        //catch(Exception e){}
        //try{s+=real+" ";}
        //catch(Exception e){}
        return s;
    }
    public int size(){
        int i=0;
        return size(i);
    }
    public int size(int j){
        return right.size(j+1);
    }
}

