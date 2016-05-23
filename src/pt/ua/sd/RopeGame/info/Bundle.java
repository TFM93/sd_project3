package pt.ua.sd.RopeGame.info;

/**
 * This data type implements a bundle.
 * A bundle contains a vector timestamp and an object value.
 *
 */
public class Bundle implements BundleInterface {

    /**
     * Vector timestamp
     * @serialField vectorTimestamp
     */
    private final VectorTimestamp vectorTimestamp;
    
    /**
     * Value
     * @serialField value
     */
    private final Object value;
    
    /**
     * Instantiates a bundle.
     * @param vectorTimestamp vector timestamp
     * @param value value
     */
    public Bundle(VectorTimestamp vectorTimestamp, Object value) {
        this.vectorTimestamp = vectorTimestamp;
        this.value = value;
    }
    
    /**
     * Instantiates a bundle.
     * @param vectorTimestamp vector timestamp
     */
    public Bundle(VectorTimestamp vectorTimestamp) {
        this(vectorTimestamp, null);
    }
    
    @Override
    public VectorTimestamp getVectorTimestamp() {
        return vectorTimestamp;
    }

    @Override
    public Object getValue() {
        return value;
    }
    
}
