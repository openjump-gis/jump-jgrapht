package fr.michaelm.jump.feature.jgrapht;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;

/**
 * This class is a wrapper around a Coordinate object which acts as a Node of 
 * the JGraphT library.
 * @author Michael Michaud
 * @version 1.0 (2021-03-19) for OpenJUMP 2
 * @version 0.3 (2007-05-28)
 */
public class Node3D implements INode {
    
    Coordinate c;
    
    public Node3D(Coordinate c) {this.c = c;}
    
   /**
    * Return the coordinate of this Node.
    */
    public Coordinate getCoordinate() {return c;}
    
   /**
    * Return a Geometry representing this Node.
    */
    public Geometry getGeometry() {return DEFAULT_GEOMETRY_FACTORY.createPoint(c);}
    
   /**
    * Return a Geometry representing this Node.
    */
    public Geometry getGeometry(GeometryFactory factory) {return factory.createPoint(c);}
    
   /**
    * Return true if obj is a Node2D with the same 3D coordinate as this node.
    */
    public boolean equals(Object obj){
        if (obj instanceof Node3D) {return c.equals3D(((Node3D)obj).c);}
        else return false;
    }
    
    public int hashCode(){return c.hashCode();}

    public String toString() {return "Node3D " + c.toString();}
}

