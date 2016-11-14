/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nntndohrs;

/**
 *
 * @author AdamPluth
 */
public class evaluator {
    static lexeme E;
    
    public evaluator(){
        E=create();
        //eval(parser.tree,E);
    }
    //--------------------utility f(x)'s------------------------//
    public lexeme cons(String type,lexeme l, lexeme r){return new lexeme(type,type,l,r);}
    public lexeme car(lexeme cell){return cell.left;}
    public lexeme cdr(lexeme cell){return cell.right;}
    public void setcar(lexeme cell,lexeme l){cell.left=l;}
    public void setcdr(lexeme cell,lexeme r){cell.right=r;}
    public String type(lexeme cell){return cell.type;}
    
    public void displayEnv(lexeme e){}

    public void displayLocalEnv(lexeme e){}
    
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
                if(var.equals(car(vars).type)){return car(vals);}
                vars=cdr(vars);
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
                if(var.equals(car(vars).type)){//maybe an issue not sure yett
                    setcar(vals,new lexeme(val,val,null,null));
                    setcar(vars,new lexeme(var,var,null,null));
                    return;
                }
                vars=cdr(vars);
                vals=cdr(vals);
            }
            env=cdr(env);
        }
        parser.fatal("variable "+ var + " is undefined");
        return;
    }
    
    public void insert(lexeme env,String var, String val){
        lexeme table=car(env);
        setcar(table,cons("GLUE",cons(var,null,null),car(table)));
        setcdr(table,cons("GLUE",cons(val,null,null),car(table)));
    }
    
    
    //--------------------Eval f(x)'s-----------------------------//
    
    
}
