/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nntndohrs;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author adam pluth on 9/18/2016
 */
public class sourceReader {
   FileInputStream in;
   String f;
   public sourceReader(String file){
       f=file;
   } 
   public FileInputStream read(){
        try{
            in = new FileInputStream(f);
            }
        catch(IOException i){
            i.printStackTrace();
        }
        return in;
   }
   
}