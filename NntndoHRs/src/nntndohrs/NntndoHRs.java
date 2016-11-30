//////////////////////////
//      adampluth       //
//      CS 403          //
//      Designer        //
//      Language        //
//      9/18/2016       //
//      (Main)          //
//////////////////////////

package nntndohrs;


import java.io.FileNotFoundException;
import java.io.IOException;

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
static lexeme e=evaluator.create();


    public static void main(String[] args) throws FileNotFoundException, IOException {
        StartGame();
        loadSavedGame();//<---load built-ins
        //true to print tree
        try{
            String file = args[0]; 
            boolean b =checkFileExtension(file);
            if(!b){parser.fatal("Bad File Extension: must be  .nes ");}
            n=new lexer(file,false);
        }
        catch(ArrayIndexOutOfBoundsException e){n=new lexer("file.nes",false);}
        catch(FileNotFoundException e){parser.fatal("Disc error game not found!!!!!");}
        p=new parser();
        tree=p.parse();
        //System.out.println(tree);
        eval_er=new evaluator(e);
        endOfGame();    
    }
    
    public static void loadSavedGame(){}

    private static boolean checkFileExtension(String s) {
        boolean b=false;
        if(s.endsWith(".NtdHrs")||s.endsWith(".nes")){
            b=true;
        }
        return b;
    }    
    public static void endOfGame(){
        System.out.print("\n <^^^^^^^^^^^^^^^^^^^^^^^^^^^>"
                       + "\n < Congratulations you win!! >"
                       + "\n <vvvvvvvvvvvvvvvvvvvvvvvvvvv>\n\n");
    }
    public static void StartGame(){
        System.out.print("\n <^^^^^^^^^^^^^^^^^^^^^^^^^^^>"
                       + "\n < Ready..  Set....     Go!! >"
                       + "\n <vvvvvvvvvvvvvvvvvvvvvvvvvvv>\n\n");
    }
    public static void gameOver(){
        System.out.print("\n <^^^^^^^^^^^^^^^^^^^^^^^^^^^>"
                       + "\n <        GAME OVER          >"
                       + "\n <vvvvvvvvvvvvvvvvvvvvvvvvvvv>\n\n");
    }
}
