import networkFlow.*;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * The Class FordFulk. Contains main part of the Ford-Fulkerson implementation
 * and code for file input
 */
public class FordFulk {

    /**
     * The name of the file that encodes the given network.
     */
    private final String filename;

    /**
     * The network on which the Ford-Fulkerson algorithm is to be run.
     */
    private Network net;

    /**
     * Instantiates a new FordFulk object.
     *
     * @param s the name of the input file
     */
    public FordFulk(String s) {
        filename = s; // store name of input file
    }

    private int numStudents;
    private int numProjects;
    private int numLecturers;

    private Student[] students;
    private Project[] projects;
    private Lecturer[] lecturers;

    /**
     * Read in network from file. See assessed exercise specification for the
     * file format.
     */
    public void readNetworkFromFile() {
        FileReader fr = null;
        Scanner in = null;
        // open file with name given by filename
        try {
            try {
                fr = new FileReader(filename);
                in = new Scanner(fr);

                // get number of students, projects, and lecturers
                this.numStudents = Integer.parseInt(in.nextLine());
                this.numProjects = Integer.parseInt(in.nextLine());
                this.numLecturers = Integer.parseInt(in.nextLine());

                // create new network with desired number of vertices
                net = new Network(numStudents + numProjects + numLecturers + 2);

                this.students = new Student[numStudents];
                this.projects = new Project[numProjects];
                this.lecturers = new Lecturer[numLecturers];

                // read and process student details
                for (int i = 0; i < numStudents; i++) {
                    String line = in.nextLine();
                    String[] studentInfo = line.split(" ");
                    int studentID = Integer.parseInt(studentInfo[0]);
                    boolean isSEStudent = studentInfo[1].equals("Y");
                    students[i] = new Student(studentID, isSEStudent);

                    for (int j = 2; j < studentInfo.length; j++) {
                        int projectID = Integer.parseInt(studentInfo[j]);
                        students[i].addAcceptableProjects(projectID); // temporarily add all their listed projects
                    }
                }

//                for (Student student : students) {
//                    System.out.println(student.toString());
//                }

                for (int i = 0; i < numProjects; i++) {
                    String line = in.nextLine();
                    String[] projectInfo = line.split(" ");
                    int projectID = Integer.parseInt(projectInfo[0]);
                    boolean isAcceptableBySE = projectInfo[1].equals("Y");
                    int lecturerID = Integer.parseInt(projectInfo[2]);
                    int capacity = Integer.parseInt(projectInfo[3]);
                    projects[i] = new Project(numStudents + projectID, lecturerID, capacity, isAcceptableBySE);
                }

//                for (Project project : projects) {
//                    System.out.println(project.toString());
//                }

                for (int i = 0; i < numLecturers; i++) {
                    String line = in.nextLine();
                    String[] lecturerInfo = line.split(" ");
                    int lecturerID = Integer.parseInt(lecturerInfo[0]);
                    int capacity = Integer.parseInt(lecturerInfo[1]);
                    lecturers[i] = new Lecturer(numProjects + numStudents + lecturerID, lecturerID, capacity);
                }

                // Step 1: Add edges from source to students
                for (int i = 0; i <= numStudents; i++) {
                    net.addEdge(net.getSource(), net.getVertexByIndex(i), 1);
                }

                // Step 2: Add edges from students to projects
                for (Student student : students) {
                    for (int projectID : student.getAcceptableProjects()) {
                        Project project = projects[projectID - 1];
                        if (!student.isSEStudent() || (student.isSEStudent() && project.isAcceptableBySE())) {
                            net.addEdge(net.getVertexByIndex(student.getLabel()), net.getVertexByIndex(project.getLabel()), 1);
                        }
                    }
                }

                // Step 3: Add edges from projects to lecturers
                for (Project project : projects) {
                    int lecturerID = project.getLecturerID();
                    Lecturer lecturer = lecturers[lecturerID - 1];
                    net.addEdge(net.getVertexByIndex(project.getLabel()), net.getVertexByIndex(lecturer.getLabel()), project.getCapacity());
                }

                // Step 4: Add edges from lecturers to sink
                for (Lecturer lecturer : lecturers) {
                    net.addEdge(net.getVertexByIndex(lecturer.getLabel()), net.getSink(), lecturer.getCapacity());
                }


            } finally {
                if (fr != null) {
                    fr.close();
                }
                if (in != null) {
                    in.close();
                }
            }
        } catch (IOException e) {
            System.err.println("IO error:");
            System.err.println(e);
            System.exit(1);
        }
    }

    /**
     * Executes Ford-Fulkerson algorithm on the constructed network net.
     */
    public void fordFulkerson() {
        // complete this method as part of Task 2
        ResidualGraph residualGraph = new ResidualGraph(net);
        LinkedList<Edge> path = residualGraph.findAugmentingPath();


        while (path != null && !path.isEmpty()) {

//            System.out.println("path: " + path);

            net.augmentPath(path);
            residualGraph = new ResidualGraph(net);
            path = residualGraph.findAugmentingPath();

        }
    }

    /**
     * Get the maximum flow in the network. If fordFulkerson has not been
     * called, the return value of this function is zero.
     *
     * @return the flow in the network.
     */
    public int getFlow() {
        return net.getValue();
    }

    /**
     * Print the results of the execution of the Ford-Fulkerson algorithm.
     */
    public void printResults() {
        if (net.isFlow()) {
            // Student assignments
            for (int i = 0; i < numStudents; i++) {
                boolean assigned = false;
                for (int j = 0; j < numProjects; j++) {
                    Edge edge = net.getAdjMatrixEntry(net.getVertexByIndex(students[i].getLabel()), net.getVertexByIndex(projects[j].getLabel()));
                    if (edge != null && edge.getFlow() > 0) {
                        System.out.println("Student " + (i + 1) + " is assigned to project " + (j + 1));
                        assigned = true;
                        break;
                    }
                }
                if (!assigned) {
                    System.out.println("Student " + (i + 1) + " is unassigned");
                }
            }

            System.out.println();

            // Project assignments
            for (int j = 0; j < numProjects; j++) {
                int assignedStudents = 0;
                for (int i = 0; i < numStudents; i++) {
                    Edge edge = net.getAdjMatrixEntry(net.getVertexByIndex(students[i].getLabel()), net.getVertexByIndex(projects[j].getLabel()));
                    if (edge != null && edge.getFlow() > 0) {
                        assignedStudents++;
                    }
                }
                System.out.println("Project " + (j + 1) + " with capacity " + projects[j].getCapacity() + " is assigned " + assignedStudents + " student/s");
            }

            System.out.println();

            // Lecturer assignments
            for (int k = 0; k < numLecturers; k++) {
                int assignedStudents = 0;
                for (int j = 0; j < numProjects; j++) {
                    if (projects[j].getLecturerID() == lecturers[k].getLecturerID()) {
                        Edge edge = net.getAdjMatrixEntry(net.getVertexByIndex(projects[j].getLabel()), net.getVertexByIndex(lecturers[k].getLabel()));
                        if (edge != null && edge.getFlow() > 0) {
                            assignedStudents += edge.getFlow();
                        }
                    }
                }
                System.out.println("Lecturer " + (k + 1) + " with capacity " + lecturers[k].getCapacity() + " is assigned " + assignedStudents + " students");
            }
        } else {
            System.out.println("The assignment is not a valid flow");
        }
    }
}
