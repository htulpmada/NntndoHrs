
package nntndohrs;

import java.util.ArrayList;
import static nntndohrs.parser.fatal;

/**
 *
 * @author AdamPluth
 */
public class evaluator {
    static lexeme E;
    
    public evaluator(lexeme e){
        E=create();
        evaluate(NntndoHRs.tree,E);
    }
//----------------------------------------utility f(x)'s--------------------------------------------------------------//
    
    public static lexeme cons(String type,lexeme l, lexeme r){return new lexeme(type,type,l,r);}
    public lexeme car(lexeme cell){return cell.left;}
    public lexeme cdr(lexeme cell){return cell.right;}
    public void setcar(lexeme cell,lexeme l){cell.left=l;}
    public void setcdr(lexeme cell,lexeme r){cell.right=r;}
    public String type(lexeme cell){return cell.type;}
    public void displayEnv(lexeme env){//need to test
        System.out.print("Printing envronments:\n");
        lexeme table;
        lexeme vars;
        lexeme vals;
        while(env!=null){
            table=car(env);
            vars=car(table);
            vals=cdr(table);
            while(vars!=null){
                System.out.print(car(vars).string+" : "+car(vals).string+"\n");
                vars=cdr(vars);
                vals=cdr(vals);
            }
            System.out.print("-----------------------\n");
            env=cdr(env);
        }
    }

    public void displayLocalEnv(lexeme env){//need to test
        lexeme table=car(env);
        lexeme vars=car(table);
        lexeme vals=cdr(table);
            while(vars!=null){
                System.out.print(car(vars).string+" : "+car(vals).string+"\n");
                vars=cdr(vars);
                vals=cdr(vals);
            }
    }
    
    public static lexeme create(){return extend(null,null,null);}
    
    public static lexeme extend(lexeme vars, lexeme vals, lexeme env){
        return cons("ENV",makeTable(vars,vals),env);
    }
    
    public static lexeme makeTable(lexeme vars,lexeme vals){
        return cons("TABLE",vars,vals);
    }
    
    public lexeme lookup(lexeme env,String var,int l){
        lexeme table;
        lexeme vars;
        lexeme vals;
//        displayEnv(env);
        while(env!=null){
            table=car(env);
            vars=car(table);
            vals=cdr(table);
            while(vars!=null){
                if(vars.left!=null){
                    if(var.equals(car(vars).string)){return car(vals);}
                }
                vars=cdr(vars);
                vals=cdr(vals);
            }
            env=cdr(env);
        }
        parser.fatal("variable "+ var + " is undefined" ,l);
        return null;
    }
    
    public lexeme update(lexeme env,String var, lexeme val,int l){
        lexeme table;
        lexeme vars;
        lexeme vals;
        while(env!=null){
            table=car(env);
            vars=car(table);
            vals=cdr(table);
            while(vars!=null){
                if(var.equals(car(vars).string)){
                    setcar(vals,new lexeme(val.type,val.string,null,null,val.strings));
                    setcar(vars,new lexeme(var,var,null,null));
                    return car(vals);
                }
                vars=cdr(vars);
                vals=cdr(vals);
            }
            env=cdr(env);
        }
        fatal("variable "+ var + " is undefined",l);
        return null;
    }
    
    public lexeme insert(lexeme var, lexeme val,lexeme env){
        lexeme table=car(env);
        setcar(table,cons("JOIN",var,car(table)));
        setcdr(table,cons("JOIN",val,cdr(table)));
        return val;
    }
    public lexeme makeArray(lexeme l){
        lexeme list=l.left;
        if (list==null){return l;}
        while(list.type=="JOIN"){
            l.strings.add(list.left);
            list=list.right;
            if(list.type!="JOIN"){l.strings.add(list);}
        }
        return l;
    }
//---------------------------helpers for function calls------------------------------------------------------//
    public lexeme getArgs(lexeme tree){
        if(tree.right==null){return null;}
        return tree.right.right.left;
    }
    public lexeme getFunction(lexeme tree){
        return tree.left;
    }
    public lexeme getEnv(lexeme tree){
        return tree.right.right;
    }
    public lexeme getBody(lexeme tree){
        return tree.right.left;
    }

    public lexeme getParams(lexeme tree){
        return tree.left;
    }
    
    
//---------------------------------------Eval f(x)'s----------------------------------------------------------//
    public lexeme evaluate(lexeme tree, lexeme env){
    if(tree==null){return new lexeme("NIL","NIL",null,null);}    
    if(tree.type == "PARSE"){return evalPARSE(tree, env);}
    else if(tree.type == "PRO"){return evalPRO(tree, env);}
    else if(tree.type == "DEF"){return evalDEF(tree, env);}
    else if(tree.type == "VDEF"){return evalVDEF(tree, env);}
    else if(tree.type == "FDEF"){return evalFDEF(tree, env);}
    else if(tree.type == "FCALL"){return evalFUNCCALL(tree, env);}
    else if(tree.type == "IDDEF"){return evalIDDEF(tree, env);}
    else if(tree.type == "OPTPLIST"){return evalOPTPLIST(tree, env);}
    else if(tree.type == "PLIST"){return evalPLIST(tree, env);}
    else if(tree.type == "OPTEXPRLIST"){return evalOPTEXPRLIST(tree, env);}
    else if(tree.type == "EXPRLIST"){return evalEXPRLIST(tree, env);}
    else if(tree.type == "EXPR"){return evalEXPR(tree, env);}
    else if(tree.type == "OPTSTATELIST"){return evalOPTSTATELIST(tree, env);}
    else if(tree.type == "STATELIST"){return evalSTATELIST(tree, env);}
    else if(tree.type == "STATE"){return evalSTATE(tree, env);}
    else if(tree.type == "UNARY"){return evalUNARY(tree, env);}
    else if(tree.type == "OP"){return evalOPERATOR(tree, env);}
    else if(tree.type == "BLOCK"){return evalBLOCK(tree, env);}
    else if(tree.type == "WHILELOOP"){return evalWHILELOOP(tree, env);}
    else if(tree.type == "IFSTATE"){return evalIFSTATE(tree, env);}
    else if(tree.type == "OPTELSESTATE"){return evalOPTELSESTATE(tree, env);}
    else if(tree.type == "ELSESTATE"){return evalELSESTATE(tree, env);}
    else if(tree.type == "LAMBDA"){return evalLAMBDA(tree, env);}
    else if(tree.type == "BREAK"){return evalBREAK(tree, env);}
    else if(tree.type == "ARRAYACCESS"){return evalARRAYACCESS(tree, env);}
    else if(tree.type == "ARRAY"){return evalARRAY(tree, env);}
    else if(tree.type == "APPEND"){return evalAPPEND(tree, env);}
    else if(tree.type == "INSERT"){return evalINSERT(tree, env);}
    else if(tree.type == "REMOVE"){return evalREMOVE(tree, env);}
    else if(tree.type == "SET"){return evalSET(tree, env);}
    else if(tree.type == "LENGTH"){return evalLENGTH(tree, env);}
//-----------^^^^^^these are from parser not lexer^^^^^^^-----------------//    
    else if(tree.type == "STRING"){return evalSTRING(tree, env);}
    else if(tree.type == "INTEGER"){return evalINTEGER(tree, env);}
    else if(tree.type == "REAL"){return evalREAL(tree, env);}
    else if(tree.type == "RETURN"){return evalRETURN(tree, env);}
    else if(tree.type == "ID"){return evalID(tree, env);}
    else if(tree.type == "NIL"){return evalNIL(tree, env);}
    else if(tree.type == "BOOLEAN"){return evalBOOLEAN(tree, env);}
    else if(tree.type == "PRINT"){return evalPRINT(tree, env);}
    else if(tree.type == "sNodeV"){return evalSetNodeV(tree, env);}
    else if(tree.type == "sNodeL"){return evalSetNodeL(tree, env);}
    else if(tree.type == "sNodeR"){return evalSetNodeR(tree, env);}
    else if(tree.type == "gNodeV"){return evalGetNodeV(tree, env);}
    else if(tree.type == "gNodeL"){return evalGetNodeL(tree, env);}
    else if(tree.type == "gNodeR"){return evalGetNodeR(tree, env);}
//-----------operators-------------vvvvvvvvvv-------//
    else if(tree.type == "EQUAL"){return evalASSIGN(tree, env);}
    else if(tree.type == "NOTEQUAL"){return evalNOTEQUAL(tree, env);}
    else if(tree.type == "NOT"){return evalUNARY(tree, env);}
    else if(tree.type == "GREATER"){return evalGREATER(tree, env);}
    else if(tree.type == "LESS"){return evalLESS(tree, env);}
    else if(tree.type == "GREATEREQUAL"){return evalGREATEREQUAL(tree, env);}
    else if(tree.type == "LESSEQUAL"){return evalLESSEQUAL(tree, env);}
    else if(tree.type == "PLUS"){return evalPLUS(tree, env);}
    else if(tree.type == "MINUS"){return evalMINUS(tree, env);}
    else if(tree.type == "TIMES"){return evalMULTIPLY(tree, env);}
    else if(tree.type == "DIVIDE"){return evalDIVIDE(tree, env);}
    else if(tree.type == "INTEGERDIVIDE"){return evalINTEGERDIVIDE(tree, env);}
    else if(tree.type == "POWER"){return evalPOWER(tree, env);}
    else if(tree.type == "AND"){return evalAND(tree, env);}
    else if(tree.type == "OR"){return evalOR(tree, env);}
    else if(tree.type == "DOUBLEEQUAL"){return evalDOUBLEEQUAL(tree, env);}
    
    else{
        fatal(tree.type+" : "+tree.string+"line#: "+tree.line);}
        return null;
}

    private lexeme evalPARSE(lexeme tree, lexeme env) {
        return evaluate(tree.left, env);
        }

    private lexeme evalPRO(lexeme tree, lexeme env) {
        while(tree.right != null){
            evaluate(tree.left, env);
            tree = tree.right.left;
        }
        return evaluate(tree.left, env);
   }

    private lexeme evalDEF(lexeme tree, lexeme env) {
        return evaluate(tree.left, env);
    }

    private lexeme evalVDEF(lexeme tree, lexeme env) {
        lexeme variable = tree.right.left;
        lexeme value = evaluate(tree.right.right.right.left, env);
        if(tree.left.string=="node"){value.type="NODE";value.makeArr();value.strings.add(null);value.strings.add(null);}
        if(value.type=="ARRAY"){
            lexeme temp=value.left;
            while(temp!=null&&temp.type=="JOIN"){
                value.strings.add(temp.left);
                temp=temp.right;
            }
            value.strings.add(temp);
            value.left=null;
        }
        lexeme ret = insert(variable, value, env);
        return ret;//returning newly inserted value
    }

    private lexeme evalFDEF(lexeme tree, lexeme env) {
        if(tree.right.right.right.right.left!=null&&tree.right.right.right.right.left.type=="SEMI"){
        //passing a function from one var to another
            lexeme variable = tree.right.left;
            lexeme close = evaluate(tree.right.right.right.left,env);
            lexeme ret = insert(variable, close, env);
            return ret;
        }
        lexeme variable = tree.right.left;
        lexeme params = tree.right.right.right.left.left;
        lexeme body = tree.right.right.right.right.right.left;
        lexeme right = cons("JOIN", body, env);
        lexeme close = cons("CLOSURE", params, right);
        lexeme ret = insert(variable, close, env);
        return ret;
    }

    private lexeme evalIDDEF(lexeme tree, lexeme env) {
        return evaluate(tree.left, env);
    }

    private lexeme evalARRAYACCESS(lexeme tree, lexeme env) {
        lexeme arr = lookup(env,tree.left.string,tree.left.line);
        lexeme place = evaluate(tree.right.right.left, env);
        int p = Integer.parseInt(place.string);
        try{return arr.strings.get(p);}
        catch(IndexOutOfBoundsException i){
            int len=arr.strings.size();
            fatal("Woops: array[max index] is "+len+" and you asked for "+p,place.line);
        return null;}
    }

    private lexeme evalFUNCCALL(lexeme tree, lexeme env) {
        int i;
        int j;
        lexeme args = getArgs(tree);
        lexeme funcName = getFunction(tree);
        lexeme closure = evaluate(funcName, env);
        if(closure == null){
            fatal("Closure was None",funcName.line);
        }
        else if(closure.type != "CLOSURE"){
            fatal("Tried to call "+closure.string+" as function.",funcName.line);
        }
        lexeme denv = getEnv(closure);
        lexeme body = getBody(closure);
        lexeme params = getParams(closure);
        if(args.left!=null){args=args.left;}
        lexeme eargs = makeArgs(args, env);
        if(eargs!=null&&params!=null){
        j=params.size();
        i=eargs.size();
        //System.out.println("# of params: "+j);
        //System.out.println("# of args: "+i);
        }
        if((args.left==null&&params!=null)||(args.left!=null&&params==null)){
            fatal("Wrong number of arguments",funcName.line);
        }
        if((eargs!=null&&params!=null) && (eargs.size() != params.size())){
            fatal("Wrong number of arguments",funcName.line);
        }
        lexeme xenv = extend(params, eargs, denv);
        return evaluate(body, xenv);
    }

    private lexeme makeArgs(lexeme tree, lexeme env) {
        lexeme r = null;
        lexeme n = null;
        if(tree.right == null){
            return cons("JOIN",evaluate(tree.left, env),null);
        }
        if(tree.right.left != null){
            r = makeArgs(tree.right, env);
            n = new lexeme("JOIN", "JOIN", evaluate(tree.left, env), r);
        }
        return n;
    }
    
    private lexeme evalOPTPLIST(lexeme tree, lexeme env) {
            if(tree.left != null){
                return evaluate(tree.left, env);
            }
        return null;

    }

    private lexeme evalPLIST(lexeme tree, lexeme env) {
        lexeme r = null;
        lexeme n = null;
        if(tree.right == null){
            return tree.left;
        }
        if(tree.right.left != null){
            r = evaluate(tree.right,env);
            n = new lexeme("JOIN", "JOIN", tree.left, cons("JOIN",r, null));
        }
        return n;
    }

    private lexeme evalOPTEXPRLIST(lexeme tree, lexeme env) {
        if(tree.left != null){
            return evaluate(tree.left, env);
        }
        return null;
    }

    private lexeme evalEXPRLIST(lexeme tree, lexeme env) {
        lexeme r = null;
        lexeme n = null;
        if(tree.right == null){
            return evaluate(tree.left,env);
        }
        if(tree.right.left != null){
            r = evaluate(tree.right, env);
            n = new lexeme("JOIN", "JOIN", evaluate(tree.left, env), r);
        }
        return n;
    }

    private lexeme evalEXPR(lexeme tree, lexeme env) {
        return evaluate(tree.left, env);
    }

    private lexeme not(lexeme l){
        if(l.type=="BOOLEAN"){l.string=Boolean.toString(!Boolean.parseBoolean(l.string));return l;}//bitwise or with integers
       // else if(l.type=="INTEGER"){l.string=Integer.toString(Integer.parseInt(l.string)0);return l;}//bitwise or with integers
        else{fatal("Can't Not type:"+l.type,l.line);}
        return null;
    }

    private lexeme evalUNARY(lexeme tree, lexeme env) {
        lexeme elements = null;
        if(tree.right == null){
            return evaluate(tree.left, env);
        }
        else if(tree.left.type == "OPAREN"){
            return evaluate(tree.right.left, env);
        }        
        else if(tree.left.type == "OBRACKET"){
            elements = evaluate(tree.right.left, env);
        }
        else if(tree.left.type == "NOT"){
            return not(evaluate(tree.right.left, env));
        }
        return evaluate(new lexeme("ARRAY", "ARRAY", elements, null), env);
    }

    private lexeme evalOPERATOR(lexeme tree, lexeme env) {
        lexeme l = tree.left;
        lexeme r = tree.right;
        String op = tree.string;
        lexeme n = new lexeme(op, op, l, r);
        return evaluate(n, env);//i think is an issue not sure
    }

    private lexeme evalBLOCK(lexeme tree, lexeme env) {
        return evaluate(tree.right.left, env);
    }

    private lexeme evalOPTSTATELIST(lexeme tree, lexeme env) {
        if(tree.left != null){
            return evaluate(tree.left, env);
        }
    return null;
    }

    private lexeme evalSTATELIST(lexeme tree, lexeme env) {
        lexeme result=null;
        lexeme prev=null;
        while(tree!=null){
            prev=result;
            result = evaluate(tree.left, env);
            if(result!=null&&result.type=="RETURNED"){
                result=result.left;
                break;
            }
            if(result!=null&&result.left!=null&&result.left.type=="RETURN"){
                result=cons("RETURNED",evaluate(result.right.left,env),null);
                break;
            }    
            if(result!=null&&result.type=="BREAK"){
                if(result.right==null){
                    result.right=prev;
                }
                break;
            }
            if(tree.right!=null){
                tree = tree.right.left;
            }
            else{break;}
        }
        return result;
    }

    private lexeme evalSTATE(lexeme tree, lexeme env) {
        if(tree.right == null){
            return evaluate(tree.left, env);
        }
        else if(tree.left.type == "EXPR"){
            return evaluate(tree.left, env);
        }
        else if(tree.left.type == "RETURN"){
            return tree;
        }
        else if(tree.left.type == "PRINT"){
            return evaluate(tree.left, env);
        }
        else{
            fatal("BAD STATEMENT ",tree.line);
        }
        return null;
    }

    private lexeme evalWHILELOOP(lexeme tree, lexeme env) {
        lexeme conditional = tree.right.right.left;
        lexeme block = tree.right.right.right.right.left;
        lexeme x = null;
        while((evaluate(conditional, env)).string == "TRUE"){
            x = evaluate(block, env);
            if(x.type=="BREAK"){return x.right;}
        }
        return x;
    }

    private lexeme evalIFSTATE(lexeme tree, lexeme env) {
        lexeme conditional = tree.right.right.left;
        lexeme block = tree.right.right.right.right.left;
        lexeme optElse = tree.right.right.right.right.right.left;
        if((evaluate(conditional, env)).string == "TRUE"){
            return evaluate(block, env);
        }
        else{
            return evaluate(optElse, env);
        }
    }

    private lexeme evalOPTELSESTATE(lexeme tree, lexeme env) {
        if(tree.left != null){
            return evaluate(tree.left, env);
        }
        return null;
    }

    private lexeme evalELSESTATE(lexeme tree, lexeme env) {
        return evaluate(tree.right.left, env);
    }

    private lexeme evalLAMBDA(lexeme tree, lexeme env) {
        lexeme params = evaluate(tree.right.right.left,env);
        lexeme body = tree.right.right.right.right.left;
        lexeme right = new lexeme("JOIN", "JOIN", body, env);
        lexeme close = new lexeme("CLOSURE", "CLOSURE", params, right);
        return close;

    }

    private lexeme evalSTRING(lexeme tree, lexeme env) {
        return tree;
    }

    private lexeme evalINTEGER(lexeme tree, lexeme env) {
        return tree;
    }
    
    private lexeme evalREAL(lexeme tree, lexeme env) {
        return tree;
    }

    private lexeme evalRETURN(lexeme tree, lexeme env) {
        return tree;
    }

    //private lexeme evalINCLUDE(lexeme tree, lexeme env) {}

    private lexeme evalID(lexeme tree, lexeme env) {
        return lookup(env, tree.string,tree.line);
    }

    private lexeme evalNIL(lexeme tree, lexeme env) {
        return tree;
    }

    private lexeme evalBOOLEAN(lexeme tree, lexeme env) {
        return tree;
    }

    private lexeme evalPRINT(lexeme tree, lexeme env) {
        lexeme eargs;
        if(tree.right!=null){eargs = evaluate(tree.right.left, env);}
        else{eargs = evaluate(tree, env);}
        try{
            if(eargs.type=="JOIN"){System.out.print(eargs.left.string+"\n");return eargs;}
            if(eargs.type=="ARRAY"){
                System.out.print("[ ");
                for(lexeme l : eargs.strings){
                    if((l!=null)&&(l.type=="NIL")){System.out.print(", ");continue;}
                    if((l!=null)&&(l.type!="ARRAY")){System.out.print(l.string+", ");}
                    else if (l!=null){//should be nested array
                        evalPRINT(l,env,false);
                    }
                }
                System.out.print("] \n");
                
            }
            else{System.out.print(eargs.string+"\n");}
        }
        catch(NullPointerException n){fatal("null pointer ",tree.line);}//should rename error message
        return eargs;
    }
     private lexeme evalPRINT(lexeme tree, lexeme env,boolean newl) {
        lexeme eargs;
        if(tree.right!=null){eargs = evaluate(tree.right.left, env);}
        else{eargs = evaluate(tree, env);}
        try{
            if(eargs.type=="JOIN"){System.out.print(eargs.left.string+"\n");return eargs;}
            if(eargs.type=="ARRAY"){
                System.out.print("[ ");
                for(lexeme l : eargs.strings){
                    if((l!=null)&&(l.type=="NIL")){System.out.print(", ");continue;}
                    if((l!=null)&&(l.type!="ARRAY")){System.out.print(l.string+", ");}
                    else if (l!=null){//should be nested array
                        evalPRINT(l,env,true);
                    }
                }
                System.out.print("] ");
                
            }
            else{System.out.print(eargs.string+"\n");}
        }
        catch(NullPointerException n){fatal("null pointer ",tree.line);}
        return eargs;
    }

    private lexeme evalEQUAL(lexeme tree, lexeme env) {
        lexeme l = evaluate(tree.left, env);
        lexeme r = evaluate(tree.right, env);
        String v;
        int i;
        if((l.type == "INTEGER") && (r.type == "INTEGER")){
            i=l.string.compareTo(r.string);
            v=(i==0)?"TRUE":"FALSE";
            return new lexeme("BOOLEAN", v);}
        else if((l.type == "STRING") && (r.type == "STRING")){
            i=l.string.compareTo(r.string);
            v=(i==0)?"TRUE":"FALSE";
            return new lexeme("BOOLEAN", v);
        }
        else if((l.type == "BOOLEAN") && (r.type == "BOOLEAN")){
            i=l.string.compareTo(r.string);
            v=(i==0)?"TRUE":"FALSE";
            return new lexeme("BOOLEAN", v);
        }
        else if((l.type == "INTEGER") && (r.type == "STRING")){
            i=l.string.compareTo(r.string);
            v=(i==0)?"TRUE":"FALSE";
            return new lexeme("BOOLEAN", v);
        }
        else if((l.type == "STRING") && (r.type == "INTEGER")){
            i=l.string.compareTo(r.string);
            v=(i==0)?"TRUE":"FALSE";
            return new lexeme("BOOLEAN", v);
        }
        else if((l.type == "REAL") && (r.type == "INTEGER")){
            i=l.string.compareTo(r.string);
            v=(i==0)?"TRUE":"FALSE";
            return new lexeme("BOOLEAN", v);
        }
        else if((l.type == "INTEGER") && (r.type == "REAL")){
            i=l.string.compareTo(r.string);
            v=(i==0)?"TRUE":"FALSE";
            return new lexeme("BOOLEAN", v);
        }
        else if((l.type == "REAL") && (r.type == "REAL")){
            i=l.string.compareTo(r.string);
            v=(i==0)?"TRUE":"FALSE";
            return new lexeme("BOOLEAN", v);
        }
        else if(l.type == "NIL"){
            if(r.type == "NIL"){
                return new lexeme("BOOLEAN", "TRUE");
            }
            else{
                return new lexeme("BOOLEAN", "FALSE");
            }
        }
        else if(r.type == "NIL"){
            return new lexeme("BOOLEAN", "FALSE");
        }
        else{
            fatal("Can't equate: "+l.string+" and "+r.string,l.line);
            return null;
        }
    }
    
    private lexeme evalNOTEQUAL(lexeme tree, lexeme env) {
        lexeme l = evaluate(tree.left, env);
        lexeme r = evaluate(tree.right, env);
        String v;
        int i;
        if((l.type == "INTEGER") && (r.type == "INTEGER")){
            i=l.string.compareTo(r.string);
            v=(i!=0)?"TRUE":"FALSE";
            return new lexeme("BOOLEAN", v);}
        else if((l.type == "STRING") && (r.type == "STRING")){
            i=l.string.compareTo(r.string);
            v=(i!=0)?"TRUE":"FALSE";
            return new lexeme("BOOLEAN", v);
        }
        else if((l.type == "INTEGER") && (r.type == "STRING")){
            i=l.string.compareTo(r.string);
            v=(i!=0)?"TRUE":"FALSE";
            return new lexeme("BOOLEAN", v);
        }
        else if((l.type == "STRING") && (r.type == "INTEGER")){
            i=l.string.compareTo(r.string);
            v=(i!=0)?"TRUE":"FALSE";
            return new lexeme("BOOLEAN", v);
        }
        else if(l.type == "NIL"){
            if(r.type != "NIL"){
                return new lexeme("BOOLEAN", "TRUE");
            }
            else{
                return new lexeme("BOOLEAN", "FALSE");
            }
        }
        else if(r.type == "NIL"){
            return new lexeme("BOOLEAN", "FALSE");
        }
        else{
            fatal("Can't NOT equate: "+l.string+" and "+r.string,l.line);
            return null;
        }
    }

    private lexeme evalGREATER(lexeme tree, lexeme env) {
        lexeme l = evaluate(tree.left, env);
        lexeme r = evaluate(tree.right, env);
        String v;
        int i;
        if((l.type == "INTEGER") && (r.type == "INTEGER")){
            i=l.string.compareTo(r.string);
            v=(i>0)?"TRUE":"FALSE";
            return new lexeme("BOOLEAN", v);}
        else if((l.type == "STRING") && (r.type == "STRING")){
            i=l.string.compareTo(r.string);
            v=(i>0)?"TRUE":"FALSE";
            return new lexeme("BOOLEAN", v);
        }
        else if((l.type == "INTEGER") && (r.type == "STRING")){
            i=l.string.compareTo(r.string);
            v=(i>0)?"TRUE":"FALSE";
            return new lexeme("BOOLEAN", v);
        }
        else if((l.type == "STRING") && (r.type == "INTEGER")){
            i=l.string.compareTo(r.string);
            v=(i>0)?"TRUE":"FALSE";
            return new lexeme("BOOLEAN", v);
        }
        else if((l.type == "REAL") && (r.type == "INTEGER")){
            i=l.string.compareTo(r.string);
            v=(i>0)?"TRUE":"FALSE";
            return new lexeme("BOOLEAN", v);
        }
        else if((l.type == "INTEGER") && (r.type == "REAL")){
            i=l.string.compareTo(r.string);
            v=(i>0)?"TRUE":"FALSE";
            return new lexeme("BOOLEAN", v);
        }
        else if((l.type == "REAL") && (r.type == "REAL")){
            i=l.string.compareTo(r.string);
            v=(i>0)?"TRUE":"FALSE";
            return new lexeme("BOOLEAN", v);
        }
        else if(l.type == "NIL"){
            if(r.type == "NIL"){
                return new lexeme("BOOLEAN", "TRUE");
            }
            else{
                return new lexeme("BOOLEAN", "FALSE");
            }
        }
        else if(r.type == "NIL"){
            return new lexeme("BOOLEAN", "FALSE");
        }
        else{
            fatal("Can't compare greater: "+l.type+" :"+l.string+" and "+r.type+" :"+r.string,l.line);
            return null;
        }
    }

    private lexeme evalLESS(lexeme tree, lexeme env) {
        lexeme l = evaluate(tree.left, env);
        lexeme r = evaluate(tree.right, env);
        String v;
        int i;
        if((l.type == "INTEGER") && (r.type == "INTEGER")){
            i=l.string.compareTo(r.string);
            v=(i<0)?"TRUE":"FALSE";
            return new lexeme("BOOLEAN", v);}
        else if((l.type == "STRING") && (r.type == "STRING")){
            i=l.string.compareTo(r.string);
            v=(i<0)?"TRUE":"FALSE";
            return new lexeme("BOOLEAN", v);
        }
        else if((l.type == "INTEGER") && (r.type == "STRING")){
            i=l.string.compareTo(r.string);
            v=(i<0)?"TRUE":"FALSE";
            return new lexeme("BOOLEAN", v);
        }
        else if((l.type == "STRING") && (r.type == "INTEGER")){
            i=l.string.compareTo(r.string);
            v=(i<0)?"TRUE":"FALSE";
            return new lexeme("BOOLEAN", v);
        }
        else if((l.type == "REAL") && (r.type == "INTEGER")){
            i=l.string.compareTo(r.string);
            v=(i<0)?"TRUE":"FALSE";
            return new lexeme("BOOLEAN", v);
        }
        else if((l.type == "INTEGER") && (r.type == "REAL")){
            i=l.string.compareTo(r.string);
            v=(i<0)?"TRUE":"FALSE";
            return new lexeme("BOOLEAN", v);
        }
        else if((l.type == "REAL") && (r.type == "REAL")){
            i=l.string.compareTo(r.string);
            v=(i<0)?"TRUE":"FALSE";
            return new lexeme("BOOLEAN", v);
        }
        else if(l.type == "NIL"){
            if(r.type == "NIL"){
                return new lexeme("BOOLEAN", "TRUE");
            }
            else{
                return new lexeme("BOOLEAN", "FALSE");
            }
        }
        else if(r.type == "NIL"){
            return new lexeme("BOOLEAN", "FALSE");
        }
        else{
            fatal("Can't compare less: "+l.string+" and "+r.string,l.line);
            return null;
        }
    }

    private lexeme evalGREATEREQUAL(lexeme tree, lexeme env) {
        lexeme l = evaluate(tree.left, env);
        lexeme r = evaluate(tree.right, env);
        String v;
        int i;
        if((l.type == "INTEGER") && (r.type == "INTEGER")){
            i=l.string.compareTo(r.string);
            v=(i>=0)?"TRUE":"FALSE";
            return new lexeme("BOOLEAN", v);}
        else if((l.type == "STRING") && (r.type == "STRING")){
            i=l.string.compareTo(r.string);
            v=(i>=0)?"TRUE":"FALSE";
            return new lexeme("BOOLEAN", v);
        }
        else if((l.type == "INTEGER") && (r.type == "STRING")){
            i=l.string.compareTo(r.string);
            v=(i>=0)?"TRUE":"FALSE";
            return new lexeme("BOOLEAN", v);
        }
        else if((l.type == "STRING") && (r.type == "INTEGER")){
            i=l.string.compareTo(r.string);
            v=(i>=0)?"TRUE":"FALSE";
            return new lexeme("BOOLEAN", v);
        }
                else if((l.type == "REAL") && (r.type == "INTEGER")){
            i=l.string.compareTo(r.string);
            v=(i>=0)?"TRUE":"FALSE";
            return new lexeme("BOOLEAN", v);
        }
        else if((l.type == "INTEGER") && (r.type == "REAL")){
            i=l.string.compareTo(r.string);
            v=(i>=0)?"TRUE":"FALSE";
            return new lexeme("BOOLEAN", v);
        }
        else if((l.type == "REAL") && (r.type == "REAL")){
            i=l.string.compareTo(r.string);
            v=(i>=0)?"TRUE":"FALSE";
            return new lexeme("BOOLEAN", v);
        }
        else if(l.type == "NIL"){
            if(r.type == "NIL"){
                return new lexeme("BOOLEAN", "TRUE");
            }
            else{
                return new lexeme("BOOLEAN", "FALSE");
            }
        }
        else if(r.type == "NIL"){
            return new lexeme("BOOLEAN", "FALSE");
        }
        else{
            fatal("Can't compare greater equal: "+l.string+" and "+r.string,l.line);
            return null;
        }
    }

    private lexeme evalLESSEQUAL(lexeme tree, lexeme env) {
        lexeme l = evaluate(tree.left, env);
        lexeme r = evaluate(tree.right, env);
        String v;
        int i;
        if((l.type == "INTEGER") && (r.type == "INTEGER")){
            i=l.string.compareTo(r.string);
            v=(i<=0)?"TRUE":"FALSE";
            return new lexeme("BOOLEAN", v);}
        else if((l.type == "STRING") && (r.type == "STRING")){
            i=l.string.compareTo(r.string);
            v=(i<=0)?"TRUE":"FALSE";
            return new lexeme("BOOLEAN", v);
        }
        else if((l.type == "INTEGER") && (r.type == "STRING")){
            i=l.string.compareTo(r.string);
            v=(i<=0)?"TRUE":"FALSE";
            return new lexeme("BOOLEAN", v);
        }
        else if((l.type == "STRING") && (r.type == "INTEGER")){
            i=l.string.compareTo(r.string);
            v=(i<=0)?"TRUE":"FALSE";
            return new lexeme("BOOLEAN", v);
        }
                else if((l.type == "REAL") && (r.type == "INTEGER")){
            i=l.string.compareTo(r.string);
            v=(i<=0)?"TRUE":"FALSE";
            return new lexeme("BOOLEAN", v);
        }
        else if((l.type == "INTEGER") && (r.type == "REAL")){
            i=l.string.compareTo(r.string);
            v=(i<=0)?"TRUE":"FALSE";
            return new lexeme("BOOLEAN", v);
        }
        else if((l.type == "REAL") && (r.type == "REAL")){
            i=l.string.compareTo(r.string);
            v=(i<=0)?"TRUE":"FALSE";
            return new lexeme("BOOLEAN", v);
        }
        else if(l.type == "NIL"){
            if(r.type == "NIL"){
                return new lexeme("BOOLEAN", "TRUE");
            }
            else{
                return new lexeme("BOOLEAN", "FALSE");
            }
        }
        else if(r.type == "NIL"){
            return new lexeme("BOOLEAN", "FALSE");
        }
        else{
            fatal("Can't compare less equal: "+l.string+" and "+r.string,l.line);
            return null;
        }
    }

    private lexeme evalPLUS(lexeme tree, lexeme env) {
        lexeme l = evaluate(tree.left, env);
        lexeme r = evaluate(tree.right, env);
      try{
        if((l.type == "INTEGER") && (r.type == "INTEGER")){
            return new lexeme("INTEGER", Integer.toString((Integer.parseInt(l.string) + Integer.parseInt(r.string))));
        }
        else if((l.type == "INTEGER") && (r.type == "STRING")){
            return new lexeme("INTEGER", Integer.toString((Integer.parseInt(l.string) + Integer.parseInt(r.string))));
        }
        else if((l.type == "STRING") && (r.type == "INTEGER")){
            return new lexeme("STRING", l.string+r.string);
        }
        else if((l.type == "STRING") && (r.type == "STRING")){
            lexeme temp=new lexeme("STRING", l.string+r.string);
            return temp;
        }
        else if((l.type == "STRING") && (r.type == "BOOLEAN")){
            lexeme temp=new lexeme("STRING", l.string+r.string);
            return temp;
        }
        else if((l.type == "BOOLEAN") && (r.type == "STRING")){
            lexeme temp=new lexeme("STRING", l.string+r.string);
            return temp;
        }
        else if((l.type == "REAL") && (r.type == "INTEGER")){
             return new lexeme("REAL", Float.toString((Float.parseFloat(l.string) + Float.parseFloat(r.string))));
        }
        else if((l.type == "INTEGER") && (r.type == "REAL")){
             return new lexeme("REAL", Float.toString((Float.parseFloat(l.string) + Float.parseFloat(r.string))));
        }
        else if((l.type == "REAL") && (r.type == "REAL")){
             return new lexeme("REAL", Float.toString((Float.parseFloat(l.string) + Float.parseFloat(r.string))));
        }
        else{
            fatal("Can't add: "+l.type+" and "+r.type,l.line);
            return null;
        }
      }
      catch(NumberFormatException n){fatal("oops thats not a number ",l.line);return null;}

    }

    private lexeme evalMINUS(lexeme tree, lexeme env) {
        lexeme l = evaluate(tree.left, env);
        lexeme r = evaluate(tree.right, env);
      try{
        if((l.type == "INTEGER") && (r.type == "INTEGER")){
            return new lexeme("INTEGER", Integer.toString((Integer.parseInt(l.string) - Integer.parseInt(r.string))));
        }
        else if((l.type == "INTEGER") && (r.type == "STRING")){
            return new lexeme("INTEGER", Integer.toString((Integer.parseInt(l.string) - Integer.parseInt(r.string))));
        }//parse real<-------vvvvvv
        else if((l.type == "REAL") && (r.type == "INTEGER")){
             return new lexeme("REAL", Float.toString((Float.parseFloat(l.string) - Float.parseFloat(r.string))));
        }
        else if((l.type == "INTEGER") && (r.type == "REAL")){
             return new lexeme("REAL", Float.toString((Float.parseFloat(l.string) - Float.parseFloat(r.string))));
        }
        else if((l.type == "REAL") && (r.type == "REAL")){
             return new lexeme("REAL", Float.toString((Float.parseFloat(l.string) - Float.parseFloat(r.string))));
        }
        else{
            fatal("Can't subtract: "+l.type+" and "+r.type,l.line);
            return null;
        }
      }
      catch(NumberFormatException n){fatal("oops thats not a number ",l.line);return null;}

    }

    private lexeme evalMULTIPLY(lexeme tree, lexeme env) {
        lexeme l = evaluate(tree.left, env);
        lexeme r = evaluate(tree.right, env);
     try{
        if((l.type == "INTEGER") && (r.type == "INTEGER")){
            return new lexeme("INTEGER", Integer.toString((Integer.parseInt(l.string) * Integer.parseInt(r.string))));
        }
        else if((l.type == "INTEGER") && (r.type == "STRING")){
            return new lexeme("INTEGER", Integer.toString((Integer.parseInt(l.string) * Integer.parseInt(r.string))));
        }//parse real<-------vvvvvv
        else if((l.type == "REAL") && (r.type == "INTEGER")){
             return new lexeme("REAL", Float.toString((Float.parseFloat(l.string) * Float.parseFloat(r.string))));
        }
        else if((l.type == "INTEGER") && (r.type == "REAL")){
             return new lexeme("REAL", Float.toString((Float.parseFloat(l.string) * Float.parseFloat(r.string))));
        }
        else if((l.type == "REAL") && (r.type == "REAL")){
             return new lexeme("REAL", Float.toString((Float.parseFloat(l.string) * Float.parseFloat(r.string))));
        }
        else{
            fatal("Can't multiply: "+l.type+" and "+r.type,l.line);
            return null;
        }
     }
     catch(NumberFormatException n){fatal("oops thats not a number ",l.line);return null;}

    }

    private lexeme evalDIVIDE(lexeme tree, lexeme env) {
        lexeme l = evaluate(tree.left, env);
        lexeme r = evaluate(tree.right, env);
     try{   
        if(r.string=="0"){fatal("Divide by zero Paradox???????",l.line);return null;}
        if((l.type == "INTEGER") && (r.type == "INTEGER")){
           try{ return new lexeme("INTEGER", Integer.toString((Integer.parseInt(l.string) / Integer.parseInt(r.string))));
           }
           catch(ArithmeticException a){fatal("Divide by zero Paradox???????",l.line);return null;}
        }
        else if((l.type == "INTEGER") && (r.type == "STRING")){
            return new lexeme("INTEGER", Integer.toString((Integer.parseInt(l.string) / Integer.parseInt(r.string))));
        }//parse real<-------vvvvvv
        else if((l.type == "REAL") && (r.type == "INTEGER")){
            return new lexeme("REAL", Float.toString((Float.parseFloat(l.string) / Float.parseFloat(r.string))));
        }
        else if((l.type == "INTEGER") && (r.type == "REAL")){
             return new lexeme("REAL", Float.toString((Float.parseFloat(l.string) / Float.parseFloat(r.string))));
        }
        else if((l.type == "REAL") && (r.type == "REAL")){
            return new lexeme("REAL", Float.toString((Float.parseFloat(l.string) / Float.parseFloat(r.string))));
        }
        else{
            fatal("Can't divide: "+l.type+" and "+r.type,l.line);
            return null;
        }
     }
     catch(NumberFormatException n){fatal("oops thats not a number ",l.line);return null;}

    }

    private lexeme evalINTEGERDIVIDE(lexeme tree, lexeme env) {
        lexeme l = evaluate(tree.left, env);
        lexeme r = evaluate(tree.right, env);
     try{
        if(r.string=="0"){fatal("Divide by zero Paradox???????",l.line);return null;}
        if((l.type == "INTEGER") && (r.type == "INTEGER")){
            return new lexeme("INTEGER", Integer.toString((Integer.parseInt(l.string) / Integer.parseInt(r.string))));
        }
        else if((l.type == "INTEGER") && (r.type == "STRING")){
            return new lexeme("INTEGER", Integer.toString((Integer.parseInt(l.string) / Integer.parseInt(r.string))));
        }
        else if((l.type == "REAL") && (r.type == "INTEGER")){
            return new lexeme("INTEGER", Integer.toString((Integer.parseInt(l.string) / Integer.parseInt(r.string))));
        }
        else if((l.type == "INTEGER") && (r.type == "REAL")){
            return new lexeme("INTEGER", Integer.toString((Integer.parseInt(l.string) / Integer.parseInt(r.string))));
        }
        else if((l.type == "REAL") && (r.type == "REAL")){
            return new lexeme("INTEGER", Integer.toString((Integer.parseInt(l.string) / Integer.parseInt(r.string))));
        }
        else{
            fatal("Can't divide: "+l.type+" and "+r.type,l.line);
            return null;
        }
     }
     catch(NumberFormatException n){fatal("oops thats not a number ",l.line);return null;}

    }

    private lexeme evalPOWER(lexeme tree, lexeme env) {
        lexeme l = evaluate(tree.left, env);
        lexeme r = evaluate(tree.right, env);
     try{   
        if((l.type == "INTEGER") && (r.type == "INTEGER")){
            return new lexeme("INTEGER", Integer.toString((int) Math.pow(Integer.parseInt(l.string) , Integer.parseInt(r.string))));
        }
        else if((l.type == "INTEGER") && (r.type == "STRING")){
            return new lexeme("INTEGER", Integer.toString((int) Math.pow(Integer.parseInt(l.string) , Integer.parseInt(r.string))));
        }
        else if((l.type == "REAL") && (r.type == "INTEGER")){
            return new lexeme("REAL", Float.toString((int) Math.pow(Float.parseFloat(l.string) , Float.parseFloat(r.string))));
        }
        else if((l.type == "INTEGER") && (r.type == "REAL")){
            return new lexeme("REAL", Float.toString((int) Math.pow(Float.parseFloat(l.string) , Float.parseFloat(r.string))));
        }
        else if((l.type == "REAL") && (r.type == "REAL")){
            return new lexeme("REAL",  Float.toString((int) Math.pow(Float.parseFloat(l.string) , Float.parseFloat(r.string))));
        }
        else{
            fatal("Can't raise: "+l.type+" to "+r.type+" power ",l.line);
            return null;
        }
     }
     catch(NumberFormatException n){fatal("oops thats not a number ",l.line);return null;}

    }

    private lexeme evalAND(lexeme tree, lexeme env) {
        lexeme l = evaluate(tree.left, env);
        lexeme r = evaluate(tree.right, env);
     try{   
        if((l.type == "INTEGER") && (r.type == "INTEGER")){
            return new lexeme("INTEGER", Integer.toString(Integer.parseInt(l.string) & Integer.parseInt(r.string)));
        }
        else if((l.type == "INTEGER") && (r.type == "STRING")){
            return new lexeme("INTEGER", Integer.toString(Integer.parseInt(l.string) & Integer.parseInt(r.string)));
        }
        else if((l.type == "REAL") && (r.type == "INTEGER")){
            return new lexeme("INTEGER", Integer.toString(Integer.parseInt(l.string) & Integer.parseInt(r.string)));
        }
        else if((l.type == "INTEGER") && (r.type == "REAL")){
            return new lexeme("INTEGER", Integer.toString(Integer.parseInt(l.string) & Integer.parseInt(r.string)));
        }
        else if((l.type == "REAL") && (r.type == "REAL")){// parses as integer needs to fix
            return new lexeme("INTEGER", Integer.toString(Integer.parseInt(l.string) & Integer.parseInt(r.string)));
        }
        else if((l.type == "BOOLEAN") && (r.type == "BOOLEAN")){
            return new lexeme("BOOLEAN", Boolean.toString(Boolean.parseBoolean(l.string) & Boolean.parseBoolean(r.string)));
        }
        else{
            fatal("Can't AND: "+l.type+" and "+r.type,l.line);
            return null;
        }
     }
     catch(NumberFormatException n){fatal("oops thats not a number ",l.line);return null;}

    }

    private lexeme evalOR(lexeme tree, lexeme env) {
        lexeme l = evaluate(tree.left, env);
        lexeme r = evaluate(tree.right, env);
        if((l.type == "INTEGER") && (r.type == "INTEGER")){
            return new lexeme("INTEGER", Integer.toString(Integer.parseInt(l.string) | Integer.parseInt(r.string)));
        }
        else if((l.type == "INTEGER") && (r.type == "STRING")){
            return new lexeme("INTEGER", Integer.toString(Integer.parseInt(l.string) | Integer.parseInt(r.string)));
        }
        else if((l.type == "REAL") && (r.type == "INTEGER")){
            return new lexeme("INTEGER", Integer.toString(Integer.parseInt(l.string) | Integer.parseInt(r.string)));
        }
        else if((l.type == "INTEGER") && (r.type == "REAL")){
            return new lexeme("INTEGER", Integer.toString(Integer.parseInt(l.string) | Integer.parseInt(r.string)));
        }
        else if((l.type == "REAL") && (r.type == "REAL")){// parses as integer needs to fix
            return new lexeme("INTEGER", Integer.toString(Integer.parseInt(l.string) | Integer.parseInt(r.string)));
        }
        else if((l.type == "BOOLEAN") && (r.type == "BOOLEAN")){
            return new lexeme("BOOLEAN", Boolean.toString(Boolean.parseBoolean(l.string) | Boolean.parseBoolean(r.string)));
        }
        else{
            fatal("Can't OR: "+l.type+" and "+r.type,l.line);
            return null;
        }

    }

    private lexeme evalASSIGN(lexeme tree, lexeme env) {
        lexeme var = tree.left.left.left;
        lexeme val = evaluate(tree.right.left, env);
        lexeme ret = update(env, var.string, val,var.line);
        return ret;
    }

    private lexeme evalDOUBLEEQUAL(lexeme tree, lexeme env) {
        return evalEQUAL(tree,env);
    }

    private lexeme evalARRAY(lexeme tree, lexeme env) {
        return tree;
    }

    private lexeme evalAPPEND(lexeme tree, lexeme env) {
        lexeme eargs = evaluate(tree.right.left, env);
        lexeme arr = eargs.left;
        lexeme value = eargs.right;
        arr.strings.add(value);
        return value;
    }

    private lexeme evalINSERT(lexeme tree, lexeme env) {
        lexeme eargs = evaluate(tree.right.left, env);
        lexeme arr = eargs.left;
        lexeme in = eargs.right.left;
        lexeme value = eargs.right.right;
        int index = Integer.parseInt((evaluate(in,env)).string);
        try{
            arr.strings.add(index,value);
            return value;
        }
        catch(IndexOutOfBoundsException i){           
        }
        arr.strings.add(value);
        return value;
    }
    
    private lexeme evalREMOVE(lexeme tree, lexeme env) {
        lexeme eargs = evaluate(tree.right.left, env);
        lexeme arr = eargs.left;
        lexeme in = eargs.right;
        int index = Integer.parseInt((evaluate(in,env)).string);
        try{
            return arr.strings.remove(index);
        }
        catch(IndexOutOfBoundsException i){
           try{return arr.strings.remove(0);}
                   catch(IndexOutOfBoundsException ie){
                           fatal("Can't remove from empty Array ",eargs.right.line);
                            return null;
                   }
        }
    }

    private lexeme evalSET(lexeme tree, lexeme env) {
        lexeme eargs = evaluate(tree.right.left, env);
        lexeme arr = eargs.left;
        lexeme in = eargs.right.left;
        lexeme value = eargs.right.right;
        int index = Integer.parseInt((evaluate(in,env)).string);
        try{
            return arr.strings.set(index,value);
        }
        catch(IndexOutOfBoundsException i){
        }
        arr.strings.add(value);
        return value;
    }

    private lexeme evalLENGTH(lexeme tree, lexeme env) {
        lexeme eargs = evaluate(tree.right.left, env);
        return new lexeme("INTEGER",Integer.toString(eargs.strings.size()),null,null);
    }
    
    private lexeme evalBREAK(lexeme tree, lexeme env){
        return tree;
    }

    private lexeme evalSetNodeV(lexeme tree, lexeme env) {
        lexeme node = getNodeId(tree, env);
        lexeme value = evaluate(tree.right.left, env).right;
        if(value.type!="NODE"){
                    value.type="NODE";
                    value.makeArr();
                    value.strings.add(null);
                    value.strings.add(null);
        }
        lexeme nvalue = update(env,node.string,value,value.line);
        return nvalue;
    }
    
    private lexeme evalSetNodeL(lexeme tree, lexeme env) {
        lexeme node = getNodeId(tree, env);
        lexeme nodeval = evaluate(node ,env);
        lexeme value = evaluate(tree.right.left, env).right;
        nodeval.strings.set(0,value);        
        lexeme nvalue = update(env,node.string,nodeval,value.line);
        return nvalue;
    }
    
    private lexeme evalSetNodeR(lexeme tree, lexeme env) {
        lexeme node = getNodeId(tree, env);
        lexeme nodeval = evaluate(node ,env);
        lexeme value = evaluate(tree.right.left, env).right;
        nodeval.strings.set(1,value);        
        lexeme nvalue = update(env,node.string,nodeval,value.line);
        return nvalue;
    }
    
    private lexeme evalGetNodeV(lexeme tree, lexeme env) {
        return evaluate(tree.right.left, env);
    }
    
    private lexeme evalGetNodeL(lexeme tree, lexeme env) {
        return evaluate(tree.right.left, env).strings.get(0);
    }
    
    private lexeme evalGetNodeR(lexeme tree, lexeme env) {
        
        return evaluate(tree.right.left, env).strings.get(1);
    }
    
    private lexeme getNodeId(lexeme tree,lexeme env){
        if(tree.right.left.left.left.left.left==null){
            return evaluate(tree.right.left.left.left,env);
        }
        return tree.right.left.left.left.left.left;
    }
    
}
