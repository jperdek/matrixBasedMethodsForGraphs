package jackdawParsing;

import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.openjdk.nashorn.internal.ir.Block;
import org.openjdk.nashorn.internal.ir.FunctionNode;
import org.openjdk.nashorn.internal.ir.Statement;
import org.openjdk.nashorn.internal.parser.Parser;
import org.openjdk.nashorn.internal.runtime.Context;
import org.openjdk.nashorn.internal.runtime.ErrorManager;
import org.openjdk.nashorn.internal.runtime.Source;
import org.openjdk.nashorn.internal.runtime.options.Options;


public class Test {
	public Test() {
		
	}
	
	private void testParsingJavascript() throws ScriptException {
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		
		Object result = engine.eval(
				   "var greeting='hello world';" +
				   "print(greeting);" +
				   "greeting");
		
		//Options options = new Options("nashorn");
		//options.set("anon.functions", true);
		//options.set("parse.only", true);
		//options.set("scripting", true);

		//ErrorManager errors = new ErrorManager();
		//Context context = new Context(options, errors, Thread.currentThread().getContextClassLoader());
		//Source source  = new Source("test", "", "var a = 10; var b = a + 1; function someFunction() { return b + 1; }  ");
		//Parser parser = new Parser(context.getEnv(), source, errors);
		//FunctionNode functionNode = parser.parse();
		//Block block = functionNode.getBody();
		//List<Statement> statements = block.getStatements();
	}
	
	private void test2() {
		Options options = new Options("nashorn");
		options.set("anon.functions", true);
		options.set("parse.only", true);
		options.set("scripting", true);

		ErrorManager errors = new ErrorManager();
		Context context = new Context(options, errors, Thread.currentThread().getContextClassLoader());
		Source source   = Source.sourceFor("test", "function someFunction() { return b + 1; }  ");
        Parser parser = new Parser(context.getEnv(), source, errors);
        FunctionNode functionNode = parser.parse();
        Block block = functionNode.getBody();
        List<Statement> statements = block.getStatements();

        for(Statement statement: statements){
            System.out.println(statement);
        }
	}
	
	
	public static void main(String args[]) throws ScriptException {
		Test test = new Test();
		test.test2();
	}
}
