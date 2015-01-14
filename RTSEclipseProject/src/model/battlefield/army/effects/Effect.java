/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.battlefield.army.effects;

import model.builders.EffectBuilder;
import geometry.Point2D;
import geometry3D.Point3D;
import java.util.ArrayList;
import model.battlefield.army.components.Projectile;
import model.battlefield.army.components.Unit;

/**
 *
 * @author Benoît
 */
public abstract class Effect {
    protected final ArrayList<EffectBuilder> effectBuilders;
    
    public final EffectSource source;
    public final EffectTarget target;

    public Effect(ArrayList<EffectBuilder> effectBuilders, EffectSource source, EffectTarget target) {
        this.effectBuilders = effectBuilders;
        this.source = source;
        this.target = target;
    }
    
    public abstract void launch();
}
