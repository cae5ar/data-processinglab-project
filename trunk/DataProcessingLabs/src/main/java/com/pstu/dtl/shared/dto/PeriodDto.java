package com.pstu.dtl.shared.dto;

@SuppressWarnings("serial")
public class PeriodDto extends EntityDto {
    String name = null;

    public PeriodDto() {
        super();
    }

    public PeriodDto(Long id) {
        super(id);
    }

    public PeriodDto(Long id, String name) {
        super(id);
        this.name = name;
    }

    public PeriodDto(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
