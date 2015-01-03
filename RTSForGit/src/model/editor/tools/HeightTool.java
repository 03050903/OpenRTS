/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.editor.tools;

import java.util.ArrayList;
import math.MyRandom;
import model.map.Tile;
import model.editor.ToolManager;
import model.editor.Pencil;
import tools.LogUtil;

/**
 *
 * @author Benoît
 */
public class HeightTool extends Tool {
    private static final String RAISE_LOW_OP = "raise/low";
    private static final String NOISE_SMOOTH_OP = "noise/smooth";
    private static final String UNIFOMR_RESET_OP = "uniform/reset";
    
    double amplitude = 0.2;
    double maintainedElevation;
    
    public HeightTool(ToolManager manager, Pencil selector) {
        super(manager, selector, RAISE_LOW_OP, NOISE_SMOOTH_OP, UNIFOMR_RESET_OP);
    }

    @Override
    public void primaryAction() {
        ArrayList<Tile> group = pencil.getTiles();
        switch (actualOp){
            case RAISE_LOW_OP : raise(group); break;
            case NOISE_SMOOTH_OP : noise(group); break;
            case UNIFOMR_RESET_OP : uniform(group); break;
        }
        manager.updateParcels(group);
    }

    @Override
    public void secondaryAction() {
        ArrayList<Tile> group = pencil.getTiles();
        switch (actualOp){
            case RAISE_LOW_OP : low(group); break;
            case NOISE_SMOOTH_OP : smooth(group); break;
            case UNIFOMR_RESET_OP : reset(group); break;
        }
        manager.updateParcels(group);
    }
    
    private void raise(ArrayList<Tile> tiles){
        for(Tile t : tiles)
            t.elevation += amplitude*pencil.getApplicationRatio(t.getPos2D());
    }
    
    private void low(ArrayList<Tile> tiles){
        for(Tile t : tiles)
            t.elevation -= amplitude*pencil.getApplicationRatio(t.getPos2D());
    }
    
    private void uniform(ArrayList<Tile> tiles){
        if(!pencil.maintained){
            pencil.maintain();
            maintainedElevation = manager.encounter.map.getGroundAltitude(pencil.getPos());
        }
        for(Tile t : tiles){
            double diff = maintainedElevation-t.elevation;
            double attenuatedAmplitude = amplitude*pencil.getApplicationRatio(t.getPos2D());
            if(diff > 0)
                t.elevation += Math.min(diff, attenuatedAmplitude);
            else if(diff < 0)
                t.elevation += Math.max(diff, -attenuatedAmplitude);
        }
    }
    private void noise(ArrayList<Tile> tiles){
        for(Tile t : tiles){
            t.elevation += amplitude*MyRandom.between(-1.0, 1.0)*pencil.getApplicationRatio(t.getPos2D());
        }
    }

    private void smooth(ArrayList<Tile> tiles){
        for(Tile t : tiles){
            double average = 0;
            for(Tile n : t.get4Neighbors())
                average += n.elevation;
            average /= t.get4Neighbors().size();
            
            double diff = average-t.elevation;
            double attenuatedAmplitude = amplitude*pencil.getApplicationRatio(t.getPos2D());
            if(diff > 0)
                t.elevation += Math.min(diff, attenuatedAmplitude);
            else if(diff < 0)
                t.elevation += Math.max(diff, -attenuatedAmplitude);
        }
    }
    
    private void reset(ArrayList<Tile> tiles){
        for(Tile t : tiles)
            t.elevation = 0;
    }

    @Override
    public void toggleSet() {
        LogUtil.logger.info("Height tool has no set.");
    }
}
