package com.pstu.dtl.shared.dto;


@SuppressWarnings("serial")
public class FileDto extends EntityDto {

    private String name;
    private Long size;

    public FileDto() {
        super();
    }

    public FileDto(Long id, String name, long size) {
        this.id = id;
        this.name = name;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }
}
