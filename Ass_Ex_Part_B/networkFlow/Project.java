package networkFlow;

public class Project extends Vertex{
    private int lecturerID;
    private int capacity;
    private boolean acceptableBySE;

    public Project(int label, int lecturerID, int capacity, boolean acceptableBySE) {
        super(label);
        this.lecturerID = lecturerID;
        this.capacity = capacity;
        this.acceptableBySE = acceptableBySE;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getLecturerID() {
        return lecturerID;
    }

    public void setLecturerID(int lecturerID) {
        this.lecturerID = lecturerID;
    }

    public boolean isAcceptableBySE() {
        return acceptableBySE;
    }

    public void setAcceptableBySE(Boolean isAcceptableBySE) {
        this.acceptableBySE = isAcceptableBySE;
    }

    @Override
    public String toString() {
        return "Project{" +
                "label=" + getLabel() +
                ", capacity=" + capacity +
                ", lecturerId=" + lecturerID +
                ", isSuitableForSE=" + acceptableBySE +
                '}';
    }
}

