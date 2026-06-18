package cr.ac.ucr.sga.model.structures.MST;

public class PrimStep {

    public int currentVertex;
    public int[] key;
    public int[] parent;
    public boolean[] inMST;
    public String description;
    public double totalWeight;

    public PrimStep(int currentVertex,
                    int[] key,
                    int[] parent,
                    boolean[] inMST,
                    String description,
                    double totalWeight) {

        this.currentVertex = currentVertex;
        this.key = key.clone();
        this.parent = parent.clone();
        this.inMST = inMST.clone();
        this.description = description;
        this.totalWeight = totalWeight;
    }
}