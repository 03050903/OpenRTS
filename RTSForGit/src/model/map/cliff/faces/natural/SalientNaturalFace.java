/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.map.cliff.faces.natural;

import collections.Ring;
import geometry.Point2D;
import geometry3D.Point3D;
import geometry3D.Polygon3D;
import geometry3D.Triangle3D;
import java.util.ArrayList;
import math.Angle;
import math.MyRandom;
import model.map.Tile;
import model.map.cliff.Trinket;
import model.map.cliff.Cliff;

/**
 *
 * @author Benoît
 */
public class SalientNaturalFace extends NaturalFace {
    
    public SalientNaturalFace(NaturalFace face){
        super(face);
        buildMesh();
    }

    @Override
    protected void extrudeProfile() {
        int i = 0;
        double curve = MyRandom.between(0.7, 1);
        for(Point3D v : parentProfile)
            grid[0][i++] = v.get2D().getRotation(Angle.RIGHT).get3D(v.z);
        i = 0;
        for(Point3D v : middleProfile)
            grid[1][i++] = v.get2D().getRotation(Angle.RIGHT/2*MyRandom.between(1+MIDDLE_EDGE_VARIATION, 1-MIDDLE_EDGE_VARIATION)).getMult(curve).get3D(v.z);
        i = 0;
        for(Point3D v : childProfile)
            grid[2][i++] = v;
    }

    @Override
    public ArrayList<Ring<Point3D>> getGrounds() {
        Point3D sw = new Point3D(-0.5, -0.5, 0);
        Point3D se = new Point3D(0.5, -0.5, 0);
        Point3D ne = new Point3D(0.5, 0.5, 0);
        Point3D nw = new Point3D(-0.5, 0.5, 0);

        Ring<Point3D> lowerPoints = new Ring<>();
        Ring<Point3D> upperPoints = new Ring<>();

        lowerPoints.add(se);
        lowerPoints.add(ne);
        lowerPoints.add(nw);
        for(int i=0; i<NaturalFace.NB_VERTEX_COL; i++)
            lowerPoints.add(grid[i][0]);

        upperPoints.add(sw);
        for(int i=NaturalFace.NB_VERTEX_COL-1; i>=0; i--)
            upperPoints.add(grid[i][NaturalFace.NB_VERTEX_ROWS-1]);
        
        ArrayList<Ring<Point3D>> res = new ArrayList<>();
        res.add(lowerPoints);
        res.add(upperPoints);
        return res;
    }
}
