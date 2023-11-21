package networkFlow;

import java.util.HashSet;
import java.util.Set;

public class Student extends Vertex{
    private boolean isSEStudent;
    private Set<Integer> acceptableProjects;

    public Student(int label, boolean isSEStudent) {
        super(label);
        this.isSEStudent = isSEStudent;
        this.acceptableProjects = new HashSet<>();
    }

    public void addAcceptableProjects(int projectID) {
        acceptableProjects.add(projectID);
    }

    public boolean isSEStudent(){
        return isSEStudent;
    }

    public Set<Integer> getAcceptableProjects() {
        return acceptableProjects;
    }

    @Override
    public String toString() {
        return "Student{" +
                "label=" + getLabel() +
                ", isSEStudent=" + isSEStudent +
                ", acceptableProjects=" + acceptableProjects +
                '}';
    }
}
