package edu.rutgers.winlab.javafaces;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

public class FaceDetect {

	private String annotation=null;

	/**
	 * Method to detect and recognize face in an image
	 * @param fileName String
	 * @return String the name or object identified
	 * @throws IOException
	 * @throws ScriptException 
	 * @throws NoSuchMethodException 
	 */
	public String run(String fileName) throws IOException, ScriptException, NoSuchMethodException {
		// Retrieving the Javascript engine
        ScriptEngine se = new ScriptEngineManager().getEngineByName("javascript");
//        if (Compilable.class.isAssignableFrom(se.getClass()) ) {
//
//            // We can compile our JS code
//            Compilable c = (Compilable) se;
//            CompiledScript cs = c.compile("E:/workspace/MJFaceServer/scripts/scripts.js");
//
//            System.out.println("From compiled JS:");
//            cs.eval();
//
//        } else {
//
//            // We can't compile our JS code
//            System.out.println("From interpreted JS:");
//            se.eval("/scripts/scripts.js");
//
//        }
//
//        // Can we invoke myFunction()?
//        if ( Invocable.class.isAssignableFrom(se.getClass()) ) {
//
//            Invocable i = (Invocable) se;
//            System.out.println("myFunction(2) returns: "
//                + i.invokeFunction("detect()", fileName));
//
//        } else {
//
//            System.out.println(
//                "Method invocation not supported!");
//
//        }

//        try {
//            FileReader reader = new FileReader("E:/workspace/MJFaceServer/scripts/scripts.js");
//            se.eval(reader);
//            reader.close();
//          } catch (Exception e) {
//            e.printStackTrace();
//          }
        
        Bindings vars = new SimpleBindings();
        vars.put("fileName", fileName);
        // Run DemoScript.js
        FileReader reader = new FileReader("E:/workspace/MJFaceServer/scripts/scripts.js");
        try {
        se.eval(reader, vars);
        } finally {
        	reader.close();
        }
      if (se instanceof Invocable) {

          Invocable i = (Invocable) se;
          Object result = i.invokeFunction("detect",fileName);
          System.out.println("[Java] result: " + result);
          
      } else {

          System.out.println(
              "Method invocation not supported!");

      }
        Object comp = vars.get("comp");
        System.out.println("[Java] demoVar: " + comp);

		return annotation;
	}
	
	public static void main(String[] args) throws NoSuchMethodException, IOException, ScriptException {
		FaceDetect f=new FaceDetect();
		f.run("C:\\Users\\Saumya\\Pictures\\pics\\gsa.jpg");
	}
}
