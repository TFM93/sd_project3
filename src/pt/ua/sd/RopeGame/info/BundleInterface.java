package pt.ua.sd.RopeGame.info;

import java.io.Serializable;

/**
 * Interface that defines the Bundle.
 *
 * @author André Santos (<a href="mailto:andre.jeronimo@ua.pt">andre.jeronimo@ua.pt</a>)
 * @author André Marques (<a href="mailto:marques.andre@ua.pt">marques.andre@ua.pt</a>)
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
