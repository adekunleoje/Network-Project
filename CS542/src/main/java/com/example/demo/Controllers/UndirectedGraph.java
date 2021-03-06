package com.example.demo.Controllers;

import java.util.Iterator;

public class UndirectedGraph
{
    private Bag<Edge>[] graph;
    private double[][] matrix;

    public UndirectedGraph(String filename)
    {
        ReadMatrix square = new ReadMatrix(filename);
        matrix = square.getMatrix();
        buildGraph();
    }

    private void buildGraph()
    {
        int length = matrix.length;
        graph = new Bag[length+1];
        for(int i = 1; i < length+1; i++)
            graph[i] = new Bag();
        for(int row = 0; row < length; row++)
        {
            for(int column = 0; column < length; column++)
            {
                double number = matrix[row][column];
                if(number != 0 && number != Double.POSITIVE_INFINITY)
                {
                    Edge e = new Edge(row+1, column+1, number);
                    graph[row+1].add(e);
                }
            }
        }
    }

    //Add a new router.
    public void addNode(int from, int to, int distance)
    {
        //'form' is the number of new router. If it less or equal to a router
        //that is already in graph, this is not a new router, just return.
        if(from <= graph.length-1) return;

        Edge edgeFrom = new Edge(from, to, distance);
        Edge edgeTo = new Edge(to, from, distance);
        Bag<Edge>[] newGraph = (Bag<Edge>[]) new Bag[from+ 1];
        for (int i = 1; i < graph.length; i++)
        {
            newGraph[i] = graph[i];
        }
        for(int i = graph.length; i < from + 1; i++) newGraph[i] = new Bag<>();
        newGraph[from].add(edgeFrom);
        newGraph[to].add(edgeTo);
        graph = newGraph;
    }

    //Delete a router.
    public void deleteNode(int number)
    {
        if(number >= graph.length) return;
        Bag bag = graph[number];
        Iterator<Edge> iterator = bag.iterator();
        while(iterator.hasNext())
        {
            Edge edge = iterator.next();
            int child = edge.either(number);
            Bag newBag = new Bag();
            Iterator<Edge> childIterator = graph[child].iterator();
            while(childIterator.hasNext())
            {
                Edge e = childIterator.next();
                if(e.either(child) != number) newBag.add(e);
            }
            graph[child] = newBag;
        }
        graph[number] = new Bag<>();
    }

    public void printGraph()
    {
        for(int i = 1; i < graph.length; i++)
        {
            Iterator<Edge> iterator = graph[i].iterator();
            while(iterator.hasNext())
            {
                System.out.println(iterator.next());
            }
        }
    }

    public Bag<Edge>[] getGraph() { return graph; }

    public static void main(String[] args)
    {
        UndirectedGraph undirectedGraph = new UndirectedGraph("square.txt");
        undirectedGraph.deleteNode(2);
        undirectedGraph.printGraph();
    }
}

