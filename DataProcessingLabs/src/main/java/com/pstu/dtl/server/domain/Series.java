package com.pstu.dtl.server.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.pstu.dtl.shared.dto.SeriesDto;

@Audited
@Entity
@Table(name = "T_SERIES")
public class Series extends AbstractEntity {

    @Column(nullable = false, length = 1024, unique = true, name = "c_name")
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "series", cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE})
    private List<Value> values;

    @Column(nullable = true, name = "c_value")
    private Double value;
    
    public List<Value> getValues() {
        return values;
    }

    public void setValues(List<Value> values) {
        this.values = values;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SeriesDto toDto() {
        SeriesDto dto = new SeriesDto();
        dto.setId(id);
        dto.setName(name);
        dto.setValues(valueListToMap());
        return dto;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    private Map<Long, Double> valueListToMap() {
        Map<Long, Double> map = new HashMap<Long, Double>();
        for (Value v : values) {
            map.put(v.getPeriod().getId(), v.getValue());
        }
        return map;
    }

}
