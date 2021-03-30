package fr.michaelm.jump.feature.jgrapht;

import org.jgrapht.graph.*;

import com.vividsolutions.jump.feature.*;
import org.locationtech.jts.geom.Geometry;

import java.util.Objects;

/**
 * Encapsulates a Feature into a DefaultWeightedEdge
 * [NOTE : this is the only method I found to use weighted graph, because
 * AbstractBaseGraph uses the following assert :
 * assert (e instanceof DefaultWeightedEdge)]
 *
 * @author Michael Michaud
 * @version 1.0 (2021-03-19) for OpenJUMP 2
 * @version 0.1 (2007-04-21)
 */
public class FeatureAsEdge extends DefaultWeightedEdge implements Feature {

    private final Feature feature;

   /**
    * Create Feature as an edge of a weighted graph
    * @param feature the feature as an edge of a graph.
    */
    public FeatureAsEdge(Feature feature) {
        this.feature = feature;
    }
    
    public Feature getFeature() {
        return feature;
    }
    
    // Implementation of Feature interface using the Decorator pattern

    @Override
    public FeatureAsEdge clone() {
        return new FeatureAsEdge(feature.clone());
    }

    @Override
    public FeatureAsEdge clone(boolean deep) {
        return new FeatureAsEdge(feature.clone(deep));
    }

    @Override
    public FeatureAsEdge clone(boolean deep, boolean copyPK) {
        return new FeatureAsEdge(feature.clone(deep, copyPK));
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FeatureAsEdge)) return false;
        FeatureAsEdge that = (FeatureAsEdge) o;
        return Objects.equals(feature, that.feature);
    }

    @Override
    public int hashCode() {
        return Objects.hash(feature);
    }

    @Override
    public int compareTo(Object o) {
        return feature.compareTo(((FeatureAsEdge)o).getFeature());
    }
}
