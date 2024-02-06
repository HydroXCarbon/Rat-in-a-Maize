package Project1_135.model;

import java.util.List;

public class Cell {

    // status: 0 = wall, 1 = ground,  2 = ground with rat, 3 = ground with Food
    // edges: 0 = top, 1 = right, 2 = bottom, 3 = left
    private int status;
    private List<Integer> edges;
    private boolean visited = false;

    public Cell (int status, List<Integer> edges){
        this.status = status;
        this.edges = edges;
    }

    public int getStatus(){
        return status;
    }

    public List<Integer> getEdges(){
        return edges;
    }
}
