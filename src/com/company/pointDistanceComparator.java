package com.company;

import java.util.Comparator;

/**
 * Created by charlesanderson on 9/21/16.
 */
public class pointDistanceComparator implements Comparator<point>{

    @Override
    public int compare(point a, point b) {
        return Double.compare(a.distance, b.distance);
    }
}
