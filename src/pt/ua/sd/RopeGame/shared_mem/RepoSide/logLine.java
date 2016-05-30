package pt.ua.sd.RopeGame.shared_mem.RepoSide;


import java.io.Serializable;

public class logLine implements Serializable, Comparable<logLine>{

    private  final int[] vectorTimestamp;
    private final String message;

    public logLine(int[] vectorTimestamp,String msg){
        this.vectorTimestamp = vectorTimestamp;
        this.message = msg;
    }
    public int[] getVectorTimestamp() {
        return vectorTimestamp;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public int compareTo(logLine other) {

        int[] otherVector = other.getVectorTimestamp();
        boolean less = false;

        for (int i=0 ; i<vectorTimestamp.length ; i++) {

            if (vectorTimestamp[i] > otherVector[i]) {
                System.out.println("Entrei!");
                return 1;
            } else if (vectorTimestamp[i] < otherVector[i]) {
                less = true;
            }
        }

        if (less) {
            return -1;
        }

        return 0;
    }
}
