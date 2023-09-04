package com.techelevator.model;

import org.jgrapht.alg.cycle.CycleDetector;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.TopologicalOrderIterator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CourseGraph {
    private DefaultDirectedGraph<Course, DefaultEdge> graph;

    public CourseGraph() {
        graph = new DefaultDirectedGraph<>(DefaultEdge.class);
    }

    public void addCourse(Course course) {
        if (!graph.containsVertex(course)) {
            graph.addVertex(course);
        } else {
            System.out.println("Course " + course.getCourseNumber() + " already exists in the graph.");
        }
    }

    public void addPrerequisite(Course course, Course prerequisite) {
        graph.addEdge(prerequisite, course);
    }

    public void removeCourse(Course course) {
        if (graph.containsVertex(course)) {
            graph.removeVertex(course);
        } else {

            System.out.println("Course " + course.getCourseNumber() + " does not exist in the graph.");
        }
    }

    public List<Course> performTopologicalSort() {
        TopologicalOrderIterator<Course, DefaultEdge> iterator = new TopologicalOrderIterator<>(graph);
        List<Course> sortedCourses = new ArrayList<>();
        while (iterator.hasNext()) {
            sortedCourses.add(iterator.next());
        }
        return sortedCourses;
    }

    public boolean hasCircularDependencies() {
        CycleDetector<Course, DefaultEdge> cycleDetector = new CycleDetector<>(graph);
        return cycleDetector.detectCycles();
    }
}
