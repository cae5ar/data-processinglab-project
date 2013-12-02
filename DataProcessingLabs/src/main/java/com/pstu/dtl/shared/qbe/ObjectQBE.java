package com.pstu.dtl.shared.qbe;

import com.pstu.dtl.shared.dto.DateRange;

@SuppressWarnings("serial")
public class ObjectQBE extends AbstractQBE {
    private String name = null;
    private String structureContains = null;
    private String thematicCategoryDIT = null;
    private String responsibleSubjectOIV = null;
    private DateRange uploadPeriod = null;
    private String status = null;
    
    public ObjectQBE() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStructureContains() {
        return structureContains;
    }

    public void setStructureContains(String structureContains) {
        this.structureContains = structureContains;
    }

    public String getThematicCategoryDIT() {
        return thematicCategoryDIT;
    }

    public void setThematicCategoryDIT(String thematicCategoryDIT) {
        this.thematicCategoryDIT = thematicCategoryDIT;
    }

    public String getResponsibleSubjectOIV() {
        return responsibleSubjectOIV;
    }

    public void setResponsibleSubjectOIV(String responsibleSubjectOIV) {
        this.responsibleSubjectOIV = responsibleSubjectOIV;
    }

    public DateRange getUploadPeriod() {
        return uploadPeriod;
    }

    public void setUploadPeriod(DateRange uploadPeriod) {
        this.uploadPeriod = uploadPeriod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
