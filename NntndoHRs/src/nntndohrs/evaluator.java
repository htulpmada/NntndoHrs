/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nntndohrs;

import java.util.ArrayList;
import static nntndohrs.parser.fatal;

/**
 *
 * @author AdamPluth
 */
public class evaluator {
    static lexeme E;
    
    public evaluator(){
        E=create();
        evaluate(NntndoHRs.tree,E);
    }
//----------------------------------------utility f(x)'s--------------------------------------------------------------//
    
    public lexeme cons(String type,lexeme l, lexeme r){return new lexeme(type,type,l,r);}
    public lexeme car(lexeme cell){return cell.left;}
    public lexeme cdr(lexeme cell){return cell.right;}
    public void setcar(lexeme cell,lexeme l){cell.left=l;}
    public void setcdr(lexeme cell,lexeme r){cell.right=r;}
    public String type(lexeme cell){return cell.type;}
    public void displayEnv(lexeme env){//need to test
        lexeme table;
        lexeme vars;
        lexeme vals;
        while(env!=null){
            table=car(env);
            vars=car(table);
            vals=cdr(table);
            while(vars!=null){
                System.out.print(vars.type+": "+vals.type);
                vars=car(vars);
                vals=cdr(vals);
            }
            env=cdr(env);
        }
    }

    public void displayLocalEnv(lexeme env){//need to test
        lexeme table=car(env);
        lexeme vars=car(table);
        lexeme vals=cdr(table);
            while(vars!=null){
                System.out.print(vars.type+": "+vals.type);
                vars=car(vars);
                vals=cdr(vals);
            }
    }
    
    public lexeme create(){return extend(null,null,null);}
    
    public lexeme extend(lexeme env, lexeme vars, lexeme vals){
        return cons("ENV",makeTable(vars,vals),env);
    }
    
    public lexeme makeTable(lexeme vars,lexeme vals){
        return cons("TABLE",vars,vals);
    }
    
    public lexeme lookup(lexeme env,String var){
        lexeme table;
        lexeme vars;
        lexeme vals;
        while(env!=null){
            table=car(env);
            vars=car(table);
            vals=cdr(table);
            while(vars!=null){
                if(var.equals(car(vars).string)){return car(vals);}
                vars=car(vars);
                vals=cdr(vals);
            }
            env=cdr(env);
        }
        parser.fatal("variable "+ var + " is undefined");
        return null;
    }
    
    public void update(lexeme env,String var, String val){
        lexeme table;
        lexeme vars;
        lexeme vals;
        while(env!=null){
            table=car(env);
            vars=car(table);
            vals=cdr(table);
            while(vars!=null){
                if(var.equals(car(vars).string)){//maybe an issue not sure yett
                    setcar(vals,new lexeme(val,val,null,null));
                    setcar(vars,new lexeme(var,var,null,null));
                    return;
                }
                vars=cdr(vars);
                vals=cdr(vals);
            }
            env=cdr(env);
        }
        fatal("variable "+ var + " is undefined");
        return;
    }
    
    public lexeme insert(lexeme var, lexeme val,lexeme env){
        lexeme table=car(env);
        setcar(table,cons("JOIN",var,car(table)));
        setcdr(table,cons("JOIN",val,car(table)));
        return val;
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
    else if(tree.type == "UNARY"){return evalUNARY(tree, env);}//need to change to unary
    else if(tree.type == "OP"){return evalOPERATOR(tree, env);}
    else if(tree.type == "BLOCK"){return evalBLOCK(tree, env);}
    else if(tree.type == "WHILELOOP"){return evalWHILELOOP(tree, env);}
    else if(tree.type == "IFSTATE"){return evalIFSTATE(tree, env);}
    else if(tree.type == "OPTELSESTATE"){return evalOPTELSESTATE(tree, env);}
    else if(tree.type == "ELSESTATE"){return evalELSESTATE(tree, env);}
    else if(tree.type == "LAMBDA"){return evalLAMBDA(tree, env);}
    else if(tree.type == "ARRAYACCESS"){return evalARRAYACCESS(tree, env);}
    //else if(tree.type == "APPEND"){return evalAPPEND(tree, env);}
    //else if(tree.type == "INSERT"){return evalINSERT(tree, env);}
    //else if(tree.type == "REMOVE"){return evalREMOVE(tree, env);}
    //else if(tree.type == "SET"){return evalSET(tree, env);}
    //else if(tree.type == "LENGTH"){return evalLENGTH(tree, env);}
//-----------^^^^^^these are from parser not lexer^^^^^^^-----------------//    
    else if(tree.type == "STRING"){return evalSTRING(tree, env);}
    else if(tree.type == "INTEGER"){return evalINTEGER(tree, env);}
    else if(tree.type == "RETURN"){return evalRETURN(tree, env);}
    else if(tree.type == "ID"){return evalID(tree, env);}
    else if(tree.type == "NIL"){return evalNIL(tree, env);}
    else if(tree.type == "BOOLEAN"){return evalBOOLEAN(tree, env);}
    else if(tree.type == "PRINT"){return evalPRINT(tree, env);}
    else if(tree.type == "EQUAL"){return evalASSIGN(tree, env);}//maybe needs to be evalASSIGN()
    else if(tree.type == "NOTEQUAL"){return evalNOTEQUAL(tree, env);}
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
    else if(tree.type == "DOUBLEEQUAL"){return evalDOUBLEEQUAL(tree, env);}//maybe needs to be evalEQUAL()
    else if(tree.type == "ARRAY"){return evalARRAY(tree, env);}

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
            //if(tree.right == null){
                return evaluate(tree.left, env);
        //}
            //return null;
    }

    private lexeme evalDEF(lexeme tree, lexeme env) {
        return evaluate(tree.left, env);
    }

    private lexeme evalVDEF(lexeme tree, lexeme env) {
        lexeme variable = tree.right.left;
        lexeme value = evaluate(tree.right.right.right.left, env);
        lexeme ret = insert(variable, value, env);
        return ret;
    }

    private lexeme evalFDEF(lexeme tree, lexeme env) {
        lexeme variable = tree.right.left;
        lexeme params = tree.right.right.right.left.left;
        lexeme body = tree.right.right.right.right.right.left;
        lexeme right = new lexeme("JOIN", "JOIN", body, env);
        lexeme close = new lexeme("CLOSURE", "CLOSURE", params, right);
        lexeme ret = insert(variable, close, env);
        return ret;
    }

    private lexeme evalIDDEF(lexeme tree, lexeme env) {
        return evaluate(tree.left, env);
    }

    private lexeme evalARRAYACCESS(lexeme tree, lexeme env) {
        int p;
        lexeme arr = lookup(env,tree.left.string);
        lexeme place = evaluate(tree.right.right.left, env);
        p = Integer.parseInt(place.string);
        return arr.strings.get(p);
    }

    private lexeme evalFUNCCALL(lexeme tree, lexeme env) {
        lexeme args = getArgs(tree);
        lexeme funcName = getFunction(tree);
        lexeme closure = evaluate(funcName, env);
        if(closure == null){
            fatal("Closure was None");
        }
        else if(closure.type != "CLOSURE"){
            fatal("Tried to call "+closure.string+" as function.");
        }
        lexeme denv = getEnv(closure);
        lexeme body = getBody(closure);
        lexeme params = getParams(closure);
        lexeme eargs = evaluate(args, env);
        //lexeme eparams = makeParamList(params);
        //lexeme eeargs = makeArgList(eargs, env);
        if((eargs!=null||params!=null)&&(eargs.size() != params.size())){
            fatal("Wrong number of arguments.");
        }
        lexeme xenv = extend(params, eargs, denv);
        return evaluate(body, xenv);
    }

   /* public lexeme makeParamList(lexeme params){
        ArrayList<lexeme> pArr=null;
        while(params!=null){
            if(params.type == "PARAMLIST"){
                pArr.add(params.left);
                if(params.right!=null){
                    params = params.right.right.left;
                }
                else{
                    params = params.right;
                }
            }
        }
        return listTolex(pArr);
    }*/

    /*public lexeme makeArgList(lexeme args,lexeme env){
    ArrayList<lexeme> argArr = null;
    while(args!=null){
        if(args.type=="LIST")){
            for(lexeme x:args){
                argArr.add(x);
            }
            break;
        else if(args.type == "CLOSURE"){
            argArr.append(args)
            break
        else if(args.type != "JOIN"):
            argArr.append(args)
            args = args.right
        else{
            argArr.append(args.left)
            args = args.right
    return argArr
*/


    private lexeme evalPLIST(lexeme tree, lexeme env) {
        lexeme r = null;
        lexeme n = null;
        if(tree.right == null){
            return evaluate(tree.left, env);
        }
        if(tree.right.right.left != null){
            r = evaluate(tree.right.right.left, env);
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
            return evaluate(tree.left, env);
        }
        if(tree.right.right.left != null){
            r = evaluate(tree.right.right.left, env);
            n = new lexeme("JOIN", "JOIN", evaluate(tree.left, env), r);
        }
        return n;
    }

    private lexeme evalEXPR(lexeme tree, lexeme env) {
        if(tree.right == null){
            return evaluate(tree.left, env);
        }
        else{
            return evaluate(tree.right, env);
        }
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
            //arr = makeArgList(elements, env);
        }
        return evaluate(new lexeme("ARRAY", "ARRAY", elements, null), env);//not sure about that
    }

    private lexeme evalOPERATOR(lexeme tree, lexeme env) {
        lexeme l = tree.left;
        lexeme r = tree.right;
        String op = tree.string;
        lexeme n = new lexeme(op, op, l, r);
        return evaluate(n, env);
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
        while(tree!=null){
            result = evaluate(tree.left, env);
            if(tree.right!=null){
                tree = tree.right.left;
            }
            else{
                break;
            }
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
            return evaluate(tree.right.left, env);
        }
        else if(tree.left.type == "PRINT"){
            return evaluate(tree.left, env);
        }
        else{
            fatal("BAD STATEMENT");
        }
        return null;
    }

    private lexeme evalWHILELOOP(lexeme tree, lexeme env) {
        lexeme conditional = tree.right.right.left;
        lexeme block = tree.right.right.right.right.left;
        lexeme x = null;
        while((evaluate(conditional, env)).string == "TRUE"){
            x = evaluate(block, env);
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
        lexeme params = tree.right.right.left;
        lexeme body = tree.right.right.right.right.left;
        lexeme right = new lexeme("JOIN", "JOIN", body, env);
        lexeme close = new lexeme("CLOSURE", "CLOSURE", params, right);
        return close;

    }

    //private lexeme evalJOIN(lexeme tree, lexeme env) {}

    private lexeme evalSTRING(lexeme tree, lexeme env) {
        return tree;
    }

    private lexeme evalINTEGER(lexeme tree, lexeme env) {
        return tree;
    }

    private lexeme evalRETURN(lexeme tree, lexeme env) {
        return evaluate(tree,env);
    }

    //private lexeme evalINCLUDE(lexeme tree, lexeme env) {}

    private lexeme evalID(lexeme tree, lexeme env) {
        return lookup(env, tree.string);
    }

    private lexeme evalNIL(lexeme tree, lexeme env) {
        return tree;
    }

    private lexeme evalBOOLEAN(lexeme tree, lexeme env) {
        return tree;
    }

    private lexeme evalPRINT(lexeme tree, lexeme env) {
        lexeme eargs = evaluate(tree.right.right.left, env);
        System.out.print(eargs.string+"\n");
        return eargs;//might be wrong 
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
            fatal("Can't equate: "+l.string+" and "+r.string);
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
            fatal("ERROR: Can't equate: "+l.string+" and "+r.string);
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
            fatal("ERROR: Can't equate: "+l.string+" and "+r.string);
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
            fatal("ERROR: Can't equate: "+l.string+" and "+r.string);
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
            fatal("ERROR: Can't equate: "+l.string+" and "+r.string);
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
            fatal("Can't equate: "+l.string+" and "+r.string);
            return null;
        }
    }

    private lexeme evalPLUS(lexeme tree, lexeme env) {
        lexeme l = evaluate(tree.left, env);
        lexeme r = evaluate(tree.right, env);
        int i=l.string.length();
        int j=r.string.length();
        if((l.type == "INTEGER") && (r.type == "INTEGER")){
            return new lexeme("INTEGER", "\""+Integer.toString((Integer.parseInt(l.string) + Integer.parseInt(r.string)))+"\"");
        }
        else if((l.type == "STRING") && (r.type == "STRING")){
            return new lexeme("STRING", "\""+l.string.substring(1,i-1)+r.string+"\"");
        }
        else if((l.type == "STRING") && (r.type == "INTEGER")){
            return new lexeme("STRING", "\""+l.string.substring(1,i-1)+r.string+"\"");
        }
        else{
            fatal("ERROR: Can't add: "+l.type+" and "+r.type);
            return null;
        }
    }

    private lexeme evalMINUS(lexeme tree, lexeme env) {
        lexeme l = evaluate(tree.left, env);
        lexeme r = evaluate(tree.right, env);
        if((l.type == "INTEGER") && (r.type == "INTEGER")){
            return new lexeme("INTEGER", Integer.toString((Integer.parseInt(l.string) - Integer.parseInt(r.string))));
        }
        else{
            fatal("ERROR: Can't subtract: "+l.type+" and "+r.type);
            return null;
        }
    }

    private lexeme evalMULTIPLY(lexeme tree, lexeme env) {
        lexeme l = evaluate(tree.left, env);
        lexeme r = evaluate(tree.right, env);
        if((l.type == "INTEGER") && (r.type == "INTEGER")){
            return new lexeme("INTEGER", Integer.toString((Integer.parseInt(l.string) * Integer.parseInt(r.string))));
        }
        else{
            fatal("ERROR: Can't multiply: "+l.type+" and "+r.type);
            return null;
        }
    }

    private lexeme evalDIVIDE(lexeme tree, lexeme env) {
        lexeme l = evaluate(tree.left, env);
        lexeme r = evaluate(tree.right, env);
        if(r.string=="0"){fatal("EEROR: divide by zero Paradox???????");return null;}
        if((l.type == "INTEGER") && (r.type == "INTEGER")){
            return new lexeme("INTEGER", Integer.toString((Integer.parseInt(l.string) + Integer.parseInt(r.string))));
        }
        else{
            fatal("ERROR: Can't divide: "+l.type+" and "+r.type);
            return null;
        }
    }

    private lexeme evalINTEGERDIVIDE(lexeme tree, lexeme env) {
        lexeme l = evaluate(tree.left, env);
        lexeme r = evaluate(tree.right, env);
        if(r.string=="0"){fatal("EEROR: divide by zero Paradox???????");return null;}
        if((l.type == "INTEGER") && (r.type == "INTEGER")){
            return new lexeme("INTEGER", Integer.toString((Integer.parseInt(l.string) / Integer.parseInt(r.string))));
        }
        else{
            fatal("ERROR: Can't divide: "+l.type+" and "+r.type);
            return null;
        }
    }

    private lexeme evalPOWER(lexeme tree, lexeme env) {
        lexeme l = evaluate(tree.left, env);
        lexeme r = evaluate(tree.right, env);
        if((l.type == "INTEGER") && (r.type == "INTEGER")){
            return new lexeme("INTEGER", Integer.toString((int) Math.pow(Integer.parseInt(l.string) , Integer.parseInt(r.string))));
        }
        else{
            fatal("ERROR: Can't add: "+l.type+" and "+r.type);
            return null;
        }
    }

    private lexeme evalAND(lexeme tree, lexeme env) {
        lexeme l = evaluate(tree.left, env);
        lexeme r = evaluate(tree.right, env);
        if((l.type == "INTEGER") && (r.type == "INTEGER")){
            return new lexeme("INTEGER", Integer.toString(Integer.parseInt(l.string) & Integer.parseInt(r.string)));
        }
        else if((l.type == "BOOLEAN") && (r.type == "BOOLEAN")){
            return new lexeme("BOOLEAN", Boolean.toString(Boolean.parseBoolean(l.string) & Boolean.parseBoolean(r.string)));
        }
        else{
            fatal("ERROR: Can't and: "+l.type+" and "+r.type);
            return null;
        }

    }

    private lexeme evalOR(lexeme tree, lexeme env) {
        lexeme l = evaluate(tree.left, env);
        lexeme r = evaluate(tree.right, env);
        if((l.type == "INTEGER") && (r.type == "INTEGER")){
            return new lexeme("INTEGER", Integer.toString(Integer.parseInt(l.string) | Integer.parseInt(r.string)));
        }
        else if((l.type == "BOOLEAN") && (r.type == "BOOLEAN")){
            return new lexeme("BOOLEAN", Boolean.toString(Boolean.parseBoolean(l.string) | Boolean.parseBoolean(r.string)));
        }
        else{
            fatal("ERROR: Can't and: "+l.type+" and "+r.type);
            return null;
        }

    }

    private lexeme evalASSIGN(lexeme tree, lexeme env) {
        lexeme var = tree.left.left.left;
        lexeme val = evaluate(tree.right.left, env);
        lexeme ret = insert(var, val, env);
        return ret;
    }

    private lexeme evalDOUBLEEQUAL(lexeme tree, lexeme env) {
        return evalEQUAL(tree,env);
    }

    private lexeme evalARRAY(lexeme tree, lexeme env) {
        return tree;
    }

    //private lexeme evalAPPEND(lexeme tree, lexeme env) {}

    //private lexeme evalINSERT(lexeme tree, lexeme env) {}

    //private lexeme evalREMOVE(lexeme tree, lexeme env) {}

    //private lexeme evalSET(lexeme tree, lexeme env) {}

    //private lexeme evalLENGTH(lexeme tree, lexeme env) {}
    
}
