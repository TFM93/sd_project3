package pt.ua.sd.RopeGame.info;

import java.io.Serializable;

/**
 * Interface that defines the Bundle.
 *
 */
public interface BundleInterface extends Serializable {
    
    /**
     * Gets the vector timestamp.
     * @return vector timestamp
     */
    VectorTimestamp getVectorTimestamp();
    
    /**
     * Gets the return value.
     * @return value
     */
    Object getValue();    
}
