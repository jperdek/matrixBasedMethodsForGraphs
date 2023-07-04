package parser;

public class AngularDetectionRegexes {

	AngularDetectionRegexes() {	
	}
	
	public static String getModuleDetectionRegex() {
		return "@NgModule(";
	}
	
	public static String getComponentDetectionRegex() {
		return "@Component(";
	}
	
	public static String getImportRegexp() {
		return "import\s+[^\"']+[\"'][^\"']+[\"']";
	}
	
	public static String getImportGroupRegexp() {
		return "import\s*\\{([^\\}]+)}\s*from\s+['\"]([^\"']+)[\"']";
	}
}
