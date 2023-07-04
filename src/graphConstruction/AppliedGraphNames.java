package graphConstruction;

import java.util.Set;
import java.util.Arrays;
import java.util.HashSet;


public interface AppliedGraphNames {
	public static final String PUZZLE_TO_PLAY = "puzzleToPlay";
	public static final String DESIGN_3D = "design3D";
	
	public static final String CLUSTERS_PUZZLE_DESIGN_MERGED = "puzzleDesign3D";
	public static final String CLUSTERS_PUZZLE_DESIGN_MERGED_EXTENDED = "puzzleDesign3DExtended";
	
	public static final HashSet<String> ALLOWED_NAMES = new HashSet<String>(Arrays.asList(
			"Component", "Module", "Service"));
}
