package jolien.ast;

import java.io.BufferedReader;
import java.io.FileReader;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class AstCreation {
	/*
	 * Method that creates an AST for a compilation unit
	 */
	public static CompilationUnit createAstForFile(String path){
		if(!path.isEmpty())
		{
			String source = fileToString(path);
			ASTParser parser = ASTParser.newParser(AST.JLS8);
			parser.setSource(source.toCharArray());
			parser.setKind(ASTParser.K_COMPILATION_UNIT);
			parser.setResolveBindings(true);
			parser.setBindingsRecovery(true);
			
			CompilationUnit cu = (CompilationUnit) parser.createAST(null);
			return cu;
		}
		else {
			return null;
		}
	}
	
	//adapted from QwalkekoProjectModel
	private static String fileToString(String path) {
		StringBuilder data = new StringBuilder();
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(path));
			char[] buf = new char[10];
			int numRead = 0;
			while ((numRead = reader.read(buf)) != -1) {
				String readData = String.valueOf(buf, 0, numRead);
				data.append(readData);
				buf = new char[1024];
			}
			reader.close();
		} catch (Exception e) {
		}
		return  data.toString();
	}
}
