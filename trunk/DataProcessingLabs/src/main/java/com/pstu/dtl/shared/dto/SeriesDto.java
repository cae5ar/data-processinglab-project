package com.pstu.dtl.shared.dto;

import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class SeriesDto extends EntityDto {
    private String name = null;
    private Double[] toClasterDistance = null;
    private Map<Long, Double> values = new LinkedHashMap<Long, Double>();

    public SeriesDto() {
        super();
    }

    public SeriesDto(Long id) {
        super(id);
    }
    
    public SeriesDto(Long id, String name) {
        super(id);
        this.name = name;
    }

    public SeriesDto(Long id, String name, Map<Long, Double> values) {
        super(id);
        this.name = name;
        this.values = values;
    }
    
    public Double[] getToClasterDistance() {
        return toClasterDistance;
    }

    public void setToClasterDistance(Double[] toClasterDistance) {
        this.toClasterDistance = toClasterDistance;
    }

    public String getName() {
        return name;
    }

    public Map<Long, Double> getValues() {
        return values;
    }

    public void setValues(Map<Long, Double> values) {
        this.values = values;
    }

    public void setName(String name) {
        this.name = name;
    }

}
