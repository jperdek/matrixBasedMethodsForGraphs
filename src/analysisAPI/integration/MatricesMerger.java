package analysisAPI.integration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import graphSimilarities.EdgeToNodeMapping;


public class MatricesMerger {

	private int numberOfVertices = -1;
	

	public MatricesMerger() {		
	}

	public String[] transferNamesAccordingMapping(int vertexMapping[], String namesSmallMatrix1[], String namesLargeMatrix2[]) {
		String mergedNames[] = new String[this.numberOfVertices];
		for (int index = 0; index < namesLargeMatrix2.length; index++) {
			mergedNames[index] = namesLargeMatrix2[index];
		}
			
		int originalSmallMatrixIndex =namesLargeMatrix2.length;
		for (int index = 0; index < namesSmallMatrix1.length; index++) {
			if (vertexMapping[index] != -1) {
				mergedNames[index] = mergedNames[index] + " | " + namesSmallMatrix1[vertexMapping[index]];
			} else {
				mergedNames[originalSmallMatrixIndex] = namesSmallMatrix1[index];
				originalSmallMatrixIndex++;
			}
		}
		
		return mergedNames;
	}
	
	public Map<String, EdgeToNodeMapping> transferNodeEdgesAccordingMappingUnion(int vertexMapping[], 
			EdgeToNodeMapping[] edgesSmallMatrixMapping1, 
			EdgeToNodeMapping[] edgesLargeMatrixMapping2,
			String namesSmallMatrix1[], String namesLargeMatrix2[]) {
		Map<String, EdgeToNodeMapping> mergedNodeMapping = new HashMap<String, EdgeToNodeMapping>();
		Map<Long, Long> mappingNewNodes = new HashMap<Long, Long>();
		long startNodeIndex, endNodeIndex;
		String edgeIdentifier;
		int newVerticesMapping[] = new int[this.numberOfVertices];
		EdgeToNodeMapping edgeToNodeMapping, alreadyStoredEdgeNodeMapping;
	
		// traverse first larger matrix and creates mapping based on native indexes 
		// (index of nodes from creation match with resulting matrix, thus are copied)
		for (int index = 0; index < edgesLargeMatrixMapping2.length; index++) {
			edgeToNodeMapping = edgesLargeMatrixMapping2[index];
			startNodeIndex = edgeToNodeMapping.getStartNodeIndex();
			endNodeIndex = edgeToNodeMapping.getEndNodeIndex();
			edgeIdentifier = Long.toString(startNodeIndex) + "," + Long.toString(endNodeIndex);
			mergedNodeMapping.put(edgeIdentifier, edgeToNodeMapping);
		}
		
		// GETTING MAPPING OF COLUMNS (not mapped nodes as -1 in 
		// vertexMapping[index] are still available in final merged matrix by union)
		// but on the other side nodes from other matrix should also extend matrix - 
		// thus mapping (extension node index) is provided for them in newVerticesMapping[index]
		int originalSmallMatrixIndex = namesLargeMatrix2.length;
		for (int index = 0; index < vertexMapping.length; index++) {
			if (vertexMapping[index] == -1) {
				newVerticesMapping[index] = originalSmallMatrixIndex;
				originalSmallMatrixIndex++;
			} else {
				newVerticesMapping[index] = -1;
			}
		}

		for (int index = 0; index < edgesSmallMatrixMapping1.length; index++) {
			edgeToNodeMapping = edgesSmallMatrixMapping1[index];
			startNodeIndex = edgeToNodeMapping.getStartNodeIndex();
			endNodeIndex = edgeToNodeMapping.getEndNodeIndex();
	
			// point from second smaller matrix is mapped into first smaller matrix
			if (vertexMapping[(int) startNodeIndex] != -1) {
				startNodeIndex = vertexMapping[(int) startNodeIndex];
			
			// point is not mapped, thus in union will be added as index greater then larger matrix size (behind)
			} else {
				startNodeIndex = newVerticesMapping[(int) startNodeIndex];
			}
			
			// point from second smaller matrix is mapped into first smaller matrix
			if (vertexMapping[(int) endNodeIndex] != -1) {
				endNodeIndex = vertexMapping[(int) endNodeIndex];
				
			// point is not mapped, thus in union will be added as index greater then larger matrix size (behind)
			} else {
				endNodeIndex = newVerticesMapping[(int) endNodeIndex];
			}
			
			edgeIdentifier = Long.toString(startNodeIndex) + "," + Long.toString(endNodeIndex);

			//edges from both graphs collide
			if (mergedNodeMapping.containsKey(edgeIdentifier)) {
				alreadyStoredEdgeNodeMapping = mergedNodeMapping.get(edgeIdentifier);
				if (alreadyStoredEdgeNodeMapping != edgeToNodeMapping) {
					//"OVERLAPPING OF EDGES (NOT HANDLED")
				}
			//edges not collide, thus should be added
			} else {
				mergedNodeMapping.put(edgeIdentifier, edgeToNodeMapping);
			}
		}
		
		return mergedNodeMapping;
	}
	
	private int getNumberOfValidVerticesUnion(int vertexMapping[], int largeMatrixSize) {
		int numberOfVertices = largeMatrixSize;
		for (int index = 0; index < vertexMapping.length; index++) {
			if (vertexMapping[index] == -1) {
				numberOfVertices = numberOfVertices + 1;
			}
		}
		return numberOfVertices;
	}
	
	private int getNumberOfValidVerticesIntersection(int vertexMapping[]) {
		int numberOfVertices = 0;
		for (int index = 0; index < vertexMapping.length; index++) {
			if (vertexMapping[index] != -1) {
				numberOfVertices = numberOfVertices + 1;
			}
		}
		return numberOfVertices;
	}
	
	private int[] getVerticesInNewMatrixMapping(
			 int vertexMapping[], int smallMatrixSize, int largeMatrixSize) {
		int verticesPositionMapping[];
		int assignedIndex = largeMatrixSize;
		Set<Integer> mappedFromFirstMatrix = new HashSet<Integer>();
		
		for (int index = 0; index < vertexMapping.length; index++) {
			if (vertexMapping[index] == -1) {
				mappedFromFirstMatrix.add(index); 
			}
		}
		
		verticesPositionMapping = new int[smallMatrixSize];
		for (int indexFromFirstMatrix = 0; indexFromFirstMatrix < smallMatrixSize; indexFromFirstMatrix++) {
			if (mappedFromFirstMatrix.contains(indexFromFirstMatrix)) {
				verticesPositionMapping[indexFromFirstMatrix] = assignedIndex;
				assignedIndex = assignedIndex + 1;
			} else {
				verticesPositionMapping[indexFromFirstMatrix] = -1;
			}
		}
		return verticesPositionMapping;
	}

	public double[][] mergeMatricesAccordingMappingFromSecondOneUnion(
			double smallerMatrix1[][], double largerMatrix2[][], int vertexMapping[]) {
		int numberOfValidVertices = this.getNumberOfValidVerticesUnion(vertexMapping, largerMatrix2.length);
		int secondIndexesMapping[] = this.getVerticesInNewMatrixMapping(
				vertexMapping, smallerMatrix1.length, largerMatrix2.length);
		double[][] mergedMatrix = new double[numberOfValidVertices][];
		
		this.numberOfVertices = numberOfValidVertices;
		for (int rowIndex = 0; rowIndex < numberOfValidVertices; rowIndex++) {
			mergedMatrix[rowIndex] = new double[numberOfValidVertices];
			for (int colIndex = 0; colIndex < numberOfValidVertices; colIndex++) {
				if (rowIndex < largerMatrix2.length && colIndex < largerMatrix2[0].length) {
					mergedMatrix[rowIndex][colIndex] = largerMatrix2[rowIndex][colIndex];
				} else {
					mergedMatrix[rowIndex][colIndex] = 0;
				}
			}
		}
		
		int mappingToNewPosition1, mappingToNewPosition2;
		for (int smallerMatrixIndex = 0; smallerMatrixIndex < secondIndexesMapping.length; smallerMatrixIndex++) {
			if (secondIndexesMapping[smallerMatrixIndex] != -1) {
				mappingToNewPosition1 = secondIndexesMapping[smallerMatrixIndex];
				for (int index = 0; index < smallerMatrix1.length; index++) {
					mappingToNewPosition2 = vertexMapping[index];
					if (mappingToNewPosition2 != -1) {
						mergedMatrix[mappingToNewPosition1][mappingToNewPosition2] = smallerMatrix1[smallerMatrixIndex][index];
						
						mergedMatrix[mappingToNewPosition2][mappingToNewPosition1] = smallerMatrix1[index][smallerMatrixIndex];
					} else {
						mappingToNewPosition2 = secondIndexesMapping[index];
						mergedMatrix[mappingToNewPosition1][mappingToNewPosition2] = smallerMatrix1[smallerMatrixIndex][index];
						
						mergedMatrix[mappingToNewPosition2][mappingToNewPosition1] = smallerMatrix1[index][smallerMatrixIndex];
					}
				}
			}
		}
		return mergedMatrix;
	}
	
	public long[][] mergeMatricesAccordingMappingFromSecondOneUnion(
			long smallerMatrix1[][], long largerMatrix2[][], int vertexMapping[]) {
		int numberOfValidVertices = this.getNumberOfValidVerticesUnion(vertexMapping, largerMatrix2.length);
		int secondIndexesMapping[] = this.getVerticesInNewMatrixMapping(
				vertexMapping, smallerMatrix1.length, largerMatrix2.length);
		long[][] mergedMatrix = new long[numberOfValidVertices][];
		
		this.numberOfVertices = numberOfValidVertices;
		for (int rowIndex = 0; rowIndex < numberOfValidVertices; rowIndex++) {
			mergedMatrix[rowIndex] = new long[numberOfValidVertices];
			for (int colIndex = 0; colIndex < numberOfValidVertices; colIndex++) {
				if (rowIndex < largerMatrix2.length && colIndex < largerMatrix2[0].length) {
					mergedMatrix[rowIndex][colIndex] = largerMatrix2[rowIndex][colIndex];
				} else {
					mergedMatrix[rowIndex][colIndex] = 0;
				}
			}
		}
		
		int mappingToNewPosition1, mappingToNewPosition2;
		for (int smallerMatrixIndex = 0; smallerMatrixIndex < secondIndexesMapping.length; smallerMatrixIndex++) {
			if (secondIndexesMapping[smallerMatrixIndex] != -1) {
				mappingToNewPosition1 = secondIndexesMapping[smallerMatrixIndex];
				for (int index = 0; index < smallerMatrix1.length; index++) {
					mappingToNewPosition2 = vertexMapping[index];
					if (mappingToNewPosition2 != -1) {
						mergedMatrix[mappingToNewPosition1][mappingToNewPosition2] = smallerMatrix1[smallerMatrixIndex][index];
						
						mergedMatrix[mappingToNewPosition2][mappingToNewPosition1] = smallerMatrix1[index][smallerMatrixIndex];
					} else {
						mappingToNewPosition2 = secondIndexesMapping[index];
						mergedMatrix[mappingToNewPosition1][mappingToNewPosition2] = smallerMatrix1[smallerMatrixIndex][index];
						
						mergedMatrix[mappingToNewPosition2][mappingToNewPosition1] = smallerMatrix1[index][smallerMatrixIndex];
					}
				}
			}
		}
		return mergedMatrix;
	}
}
