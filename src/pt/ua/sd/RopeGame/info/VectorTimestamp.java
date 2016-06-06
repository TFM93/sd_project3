package pt.ua.sd.RopeGame.info;

import java.util.Arrays;

/**
 * This data type implements a vector timestamp.
 * Contains the vectors timestamp of all active entities
 *
 */
public class VectorTimestamp implements VectorTimestampInterface {

    /**
     * Vector timestamp
     */
    private int[] vectorTimestamp;
    
    /**
     * Vector timestamp id
     */
    private final int vectorTimestampId;
    
    /**
     * Instantiates a vector timestamp.
     * @param vectorTimestampId vector timestamp id
     * @param vectorTimestampSize vector timestamp size
     */
    public VectorTimestamp(int vectorTimestampId, int vectorTimestampSize) {
        this.vectorTimestampId = vectorTimestampId;
        this.vectorTimestamp = new int[vectorTimestampSize];
        Arrays.fill(vectorTimestamp, 0);
    }
    
    private VectorTimestamp(int vectorTimestampId, int[] vectorTimestamp) {
        this.vectorTimestampId = vectorTimestampId;
        this.vectorTimestamp = vectorTimestamp;
    }
    
    @Override
    public int[] getVectorTimestampArray() {
        return vectorTimestamp.clone();
    }

    @Override
    public void incrementVectorTimestamp() {
        vectorTimestamp[vectorTimestampId]++;
    }

    @Override
    public void updateVectorTimestamp(VectorTimestampInterface otherVectorTimestamp) {        
        for (int i=0 ; i<vectorTimestamp.length ; i++) {
            if (otherVectorTimestamp.getVectorTimestampArray()[i] > vectorTimestamp[i]) {
                vectorTimestamp[i] = otherVectorTimestamp.getVectorTimestampArray()[i];
            }
        }
    }
    
    /**
     * Clones this instance.
     * @return vector timestamp clone
     */
    public VectorTimestamp clone() {
        VectorTimestamp clone = new VectorTimestamp(vectorTimestampId, vectorTimestamp.clone());
        return clone;
    }

    @Override
    public void setVectorTimestamp(VectorTimestampInterface vectorTimestamp) {
        this.vectorTimestamp = vectorTimestamp.getVectorTimestampArray();
    }
    
}