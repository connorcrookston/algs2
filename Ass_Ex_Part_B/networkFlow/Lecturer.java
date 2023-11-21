package networkFlow;


public class Lecturer extends Vertex {
    private int capacity;
    private int lecturerID;

    public Lecturer(int label, int lecturerID, int capacity) {
        super(label);
        this.lecturerID = lecturerID;
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getLecturerID(){
        return lecturerID;
    }

    @Override
    public String toString() {
        return "Project{" +
                "label=" + getLabel() +
                ", capacity=" + capacity +
                ", lecturerId=" + lecturerID +
                '}';
    }
}
