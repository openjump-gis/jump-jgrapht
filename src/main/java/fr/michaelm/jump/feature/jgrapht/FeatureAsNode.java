package fr.michaelm.jump.feature.jgrapht;

import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureSchema;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;

/**
 * This class implements both Feature from jump library and INode from
 * jump-jgrapht bridge.<br>
 * initial release
 * @author Michael Michaud
 * @version 1.0 (2021-03-19) for OpenJUMP 2
 * @version 0.3 (2008-02-02)
 */
public class FeatureAsNode implements INode, Feature {

    private final Feature feature;

   /**
    * Create Feature as an edge of a weighted graph
    * @param feature the feature as an edge of a graph.
    */
    public FeatureAsNode(Feature feature) {
        this.feature = feature;
    }
    
    public Feature getFeature() {
        return feature;
    }
    
    // Implementation of Feature interface using the Decorator pattern

    @Override
    public FeatureAsNode clone() {
        return new FeatureAsNode(feature.clone());
    }

    @Override
    public FeatureAsNode clone(boolean deep) {
        return new FeatureAsNode(feature.clone(deep));
    }

    @Override
    public FeatureAsNode clone(boolean b, boolean b1) {
        return new FeatureAsNode(feature);
    }

    @Override
    public Object getAttribute(int i) {
        return feature.getAttribute(i);
    }

    @Override
    public Object getAttribute(String name) {
        return feature.getAttribute(name);
    }

    @Override
    public Object[] getAttributes() {
        return feature.getAttributes();
    }

    @Override
    public double getDouble(int attributeIndex) {
        return feature.getDouble(attributeIndex);
    }

    // This method is common to Feature interface and INode interface
    @Override
    public Geometry getGeometry() {
        return feature.getGeometry();
    }

    @Override
    public int getID() {
        return feature.getID();
    }

    @Override
    public int getInteger(int attributeIndex) {
        return feature.getInteger(attributeIndex);
    }

    @Override
    public FeatureSchema getSchema() {
        return feature.getSchema();
    }

    @Override
    public String getString(int attributeIndex) {
        return feature.getString(attributeIndex);
    }

    @Override
    public String getString(java.lang.String attributeName) {
        return feature.getString(attributeName);
    }

    @Override
    public void setAttribute(int attributeIndex, Object newAttribute) {
        feature.setAttribute(attributeIndex, newAttribute);
    }

    @Override
    public void setAttribute(String attributeName, Object newAttribute) {
        feature.setAttribute(attributeName, newAttribute);
    }

    @Override
    public void setAttributes(Object[] attributes) {
        feature.setAttributes(attributes);
    }

    @Override
    public void setGeometry(Geometry geometry) {
        feature.setGeometry(geometry);
    }

    @Override
    public void setSchema(FeatureSchema schema) {
        feature.setSchema(schema);
    }

    @Override
    public int compareTo(Object o) {
        return feature.compareTo(((FeatureAsNode)o).getFeature());
    }
    
   // INode implementation
   /**
    * Return the coordinate of this Node.
    */
    @Override
    public Coordinate getCoordinate() {
        return feature.getGeometry().getInteriorPoint().getCoordinate();
    }

    @Override
    public boolean equals(Object o) {
      return o instanceof FeatureAsNode &&
          getGeometry().equals(((FeatureAsNode)o).getGeometry());
    }

    @Override
    public int hashCode() {
      return getGeometry().hashCode();
    }

    @Override
    public String toString() {
      return "[" + getCoordinate() + "]";
    }

}

