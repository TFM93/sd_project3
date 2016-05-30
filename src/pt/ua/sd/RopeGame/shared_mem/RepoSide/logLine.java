package pt.ua.sd.RopeGame.shared_mem.RepoSide;


public class logLine {

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
}
