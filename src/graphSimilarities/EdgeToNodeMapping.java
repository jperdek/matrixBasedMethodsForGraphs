package graphSimilarities;

public class EdgeToNodeMapping {

	long startNodeIndex, endNodeIndex;
	long edgeOrder;
	
	public EdgeToNodeMapping(long startNodeIndex, long endNodeIndex) {
		this.startNodeIndex = startNodeIndex;
		this.endNodeIndex = endNodeIndex;
	}
	
	public EdgeToNodeMapping(long startNodeIndex, long endNodeIndex, long edgeOrder) {
		this.startNodeIndex = startNodeIndex;
		this.endNodeIndex = endNodeIndex;
		this.edgeOrder = edgeOrder;
	}
	
	public long getStartNodeIndex() { return this.startNodeIndex; }
	
	public long getEndNodeIndex() { return this.endNodeIndex; }
	
	public long getEdgeOrder() { return this.edgeOrder; }
}
