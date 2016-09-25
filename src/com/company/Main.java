package com.company;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

public class Main {
    public static point start;
    public static point end;

    public static void main(String[] args) {
	// write your code here
        File file = new File("med_maze.txt");
        List<List<point>> maze = readFile(file);
        DFS(maze, start);
    }

    private static List<List<point>> readFile(File file) {
        List<List<point>> maze = new ArrayList<List<point>>();
        ArrayList<point> row = new ArrayList<point>();
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            int curr;
            while( (curr = fileInputStream.read()) != -1 ) {
                if(curr == 10) {
                    maze.add(row);
                    row = new ArrayList<point>();
                }
                else
                    row.add(new point(0,0,(char)curr));
                System.out.print((char)curr);

            }
        }
        catch(Exception e) {
            System.out.println("EXCEPTION: "+ e);
        }

        maze.add(row); // last row
        System.out.println("\n\n");
        // just making sure the maze is the same
        for(int i=0; i<maze.size(); i++) {
            for(int j=0; j<maze.get(i).size(); j++) {
                point curr = maze.get(i).get(j);
                System.out.print(curr.c);
                curr.x = i;
                curr.y = j;
                if(curr.c == 'P') {
                    start = curr;
                }
                else if(curr.c == '.') {
                    end = curr;
                }
            }
            System.out.println("");
        }

        return maze;
    }

    private static void printMaze(List<List<point>> maze) {
        System.out.println();
        for(int i=0; i<maze.size(); i++) {
            for(int j=0; j<maze.get(i).size(); j++) {
                point curr = maze.get(i).get(j);
                System.out.print(curr.c);
            }
            System.out.println("");
        }
        System.out.println();
    }

    private static void BFS(List<List<point>> maze, point start) {
        Queue queue = new LinkedList();
        queue.add(start);
        point curr;
        int x,y,c;
        while(!queue.isEmpty()) {
            curr = (point)queue.remove();
            x = curr.x;
            y = curr.y;
            c = curr.c;
            printMaze(maze);

            if(c == '.') {
                System.out.println("Found Goal!");
                printMaze(maze);
                break;
            }
            else {
                queue = checkPoint(maze, queue, x, y);
                curr.visited = true;
                curr.c = '@';
            }
        }
    }

    private static Queue checkPoint(List<List<point>> maze, Queue queue, int x, int y) {
        point right = maze.get(x+1).get(y);
        point left = maze.get(x-1).get(y);
        point down = maze.get(x).get(y+1);
        point up = maze.get(x).get(y-1);

        //  Do not make these Else-Ifs, all must be checked
        if(!right.visited && right.c != '%') {
            right.visited = true;
            queue.add(right);
        }
        if(!left.visited && left.c != '%') {
            left.visited = true;
            queue.add(left);
        }
        if(!down.visited && down.c != '%') {
            down.visited = true;
            queue.add(down);
        }
        if(!up.visited && up.c != '%') {
            up.visited = true;
            queue.add(up);
        }
        return queue;
    }

    private static void checkPointDFS(List<List<point>> maze, int x, int y) {
        point right = maze.get(x+1).get(y);
        point left = maze.get(x-1).get(y);
        point down = maze.get(x).get(y+1);
        point up = maze.get(x).get(y-1);

        //  Do not make these Else-Ifs, all must be checked
        if(!right.visited && right.c != '%') {
            DFS(maze, right);
        }
        if(!left.visited && left.c != '%') {
            DFS(maze, left);
        }
        if(!down.visited && down.c != '%') {
            DFS(maze, down);
        }
        if(!up.visited && up.c != '%') {
            DFS(maze, up);
        }
    }

    private static void DFS(List<List<point>> maze, point start) {
        start.visited = true;
        printMaze(maze);

        if(start.c == '.') {
            System.out.println("Found Goal!");
            printMaze(maze);
            return;
        }
        else {
            start.c = '@';
            checkPointDFS(maze, start.x, start.y);
        }
    }


    // TODO: finish logic to avoid repeated states
    private static void Greedy(List<List<point>> maze, point start, point end) {
        Queue<point> queue = new LinkedList<point>();
        Queue<point> explored = new LinkedList<point>();
        PriorityQueue<point> prio = new PriorityQueue<point>(0, new pointDistanceComparator());
        queue.add(start);
        point curr;
        int x,y,c;
        while(!queue.isEmpty()) {
            curr = (point)queue.remove();
            explored.add(curr);
            x = curr.x;
            y = curr.y;
            c = curr.c;
            printMaze(maze);

            if(c == '.') {
                System.out.println("Found Goal!");
                printMaze(maze);
                break;
            }
            else {
                prio = getClosest(maze, curr.x, curr.y, end, prio);
                curr.visited = true;
                curr.c = '@';
            }
        }
    }

    private static point closestPoint(point a, point b, point end) {
        if(Math.sqrt(Math.pow((end.x - a.x), 2) + Math.pow((end.y - a.y), 2)) > Math.sqrt(Math.pow((end.x - b.x), 2) + Math.pow((end.y - b.y), 2))){
            return b;
        }
        else
            return a;
    }

    private static double getDist(point a, point b) {
        return Math.sqrt(Math.pow((b.x - a.x), 2) + Math.pow((b.y - a.y), 2));
    }


    private static PriorityQueue<point> getClosest(List<List<point>> maze, int x, int y, point end, PriorityQueue<point> prio) {
        point right = maze.get(x+1).get(y);
        point left = maze.get(x-1).get(y);
        point down = maze.get(x).get(y+1);
        point up = maze.get(x).get(y-1);
        Queue queue = new LinkedList();

        double minDist = 999999;
        point currMin = right;
        if(right.c != '%') {
            double distRight = getDist(right, end);
            right.distance = distRight;
            prio.add(right);
            if(distRight < minDist) {
                minDist = distRight;
                currMin = right;
            }
        }
        if(left.c != '%') {
            double distLeft = getDist(left, end);
            left.distance = distLeft;
            prio.add(left);
            if (distLeft < minDist) {
                minDist = distLeft;
                currMin = left;
            }
        }
        if(down.c != '%') {
            double distDown = getDist(down, end);
            down.distance = distDown;
            prio.add(down);
            if(distDown < minDist) {
                minDist = distDown;
                currMin = down;
            }
        }
        if(up.c != '%') {
            double distUp = getDist(up, end);
            up.distance = distUp;
            prio.add(up);
            if(distUp < minDist) {
                minDist = distUp;
                currMin = up;
            }
        }

        return prio;
    }


    private double getMinFromQ(Queue q) {
        double currMin = 999999;
        double curr;
        for(int i = 0; i<q.size(); i++) {
            curr = (Double)q.remove();
            if(curr < currMin) {
                currMin = curr;
            }
        }
        return currMin;
    }






    private static void AStar(List<List<point>> maze, point start) {

    }


}

