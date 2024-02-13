package Project1_135.model;

public class Cell {

    // status: 0 = wall, 1 = ground,  2 = ground with rat, 3 = ground with Food
    private int status;
    private boolean visited = false;

    public Cell(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean getVisited() {
        return visited;
    }

    public void setVisited(boolean status) {
        this.visited = status;
    }


}
