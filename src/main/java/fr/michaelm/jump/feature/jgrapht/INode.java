package fr.michaelm.jump.feature.jgrapht;


import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;

/**
 * This interface is implemented by every object used as a Node in a graph
 * composed with JUMP features or JTS geometries.
 * INodes may correspond to features in a feature collection (ex. crossroads) or
 * to feature boundaries (ex. start point and end point of a linear road
 * segment).<br>
 * @author Michael Michaud
 * @version 1.0 (2021-03-19) for OpenJUMP 2
 * @version 0.2 (2007-04-20)
 */
public interface INode {

    GeometryFactory DEFAULT_GEOMETRY_FACTORY = new GeometryFactory();

   /**
    * Return the coordinate of this Node.
    */
    Coordinate getCoordinate();

   /**
    * Return a Geometry representing this Node.
    */
    Geometry getGeometry();

}

