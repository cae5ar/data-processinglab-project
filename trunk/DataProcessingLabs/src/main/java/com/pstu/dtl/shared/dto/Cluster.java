package com.pstu.dtl.shared.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class Cluster implements Serializable, IsSerializable {

    private Double[] centroid = null;
    private List<SeriesDto> series = new ArrayList<SeriesDto>();

    public Cluster() {}

    public Cluster(Double[] centroid, List<SeriesDto> series) {
        super();
        this.centroid = centroid;
        this.series = series;
    }

    public Cluster(Double[] centroid) {
        super();
        this.centroid = centroid;
    }

    public Double[] getCentroid() {
        return centroid;
    }

    public void setCentroid(Double[] centroid) {
        this.centroid = centroid;
    }

    public List<SeriesDto> getSeries() {
        return series;
    }

    public void setSeries(List<SeriesDto> series) {
        this.series = series;
    }

}
