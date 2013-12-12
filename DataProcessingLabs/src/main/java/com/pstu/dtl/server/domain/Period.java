package com.pstu.dtl.server.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.pstu.dtl.shared.dto.PeriodDto;

@Entity
@Audited
@Table(name = "T_PERIODS")
public class Period extends AbstractEntity {
    @Column(nullable = false, length = 1024, name = "c_name")
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "period", cascade = {CascadeType.MERGE, CascadeType.REMOVE,
            CascadeType.REFRESH})
    private List<Value> values = new ArrayList<Value>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Value> getValues() {
        return values;
    }

    public void setValues(List<Value> values) {
        this.values = values;
    }

    public PeriodDto toDto() {
        return new PeriodDto(id, name);
    }
}
