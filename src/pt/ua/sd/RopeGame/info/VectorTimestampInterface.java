package pt.ua.sd.RopeGame.info;

import java.io.Serializable;

/**
 * Interface that defines the Vector Timestamp.
 *
 */
public interface VectorTimestampInterface extends Serializable {
    
    /**
     * Gets vector timestamp array.
     * @return vector timestamp
     */
    int[] getVectorTimestampArray();

    /**
     * Increment vector timestamp.
     */
    void incrementVectorTimestamp();
    
    /**
     * Update vector timestamp.
     * @param otherVectorTimestamp vector timestamp to use in update
     */
    void updateVectorTimestamp(VectorTimestampInterface otherVectorTimestamp);
    
    /**
     * Sets the vector timestamp.
     * @param vectorTimestamp vector timestamp
     */
    void setVectorTimestamp(VectorTimestampInterface vectorTimestamp);
}
