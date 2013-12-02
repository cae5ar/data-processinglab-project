package com.pstu.dtl.server.domain;

import java.net.URLConnection;
import java.sql.Blob;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.sql.rowset.serial.SerialBlob;

import org.apache.commons.io.IOUtils;

import com.pstu.dtl.shared.dto.FileDto;

@Entity
@Table(name = "T_FILE")
public class File extends AbstractEntity {

    @Column(nullable = true, length = 1024)
    private String name;

    @Column(nullable = true)
    private Long size;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column
    private Blob content;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Long getSize() {
        return size;
    }

    public byte[] getContent() {
        try {
            return IOUtils.toByteArray(content.getBinaryStream());
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setContent(byte[] content) {
        try {
            this.content = new SerialBlob(content);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String guessContentType() {
        String res = URLConnection.guessContentTypeFromName(name);
        if (res == null && name.matches(".*doc(x)?|.*ppt(x)?|.*xls(x)?|.*xlt(x)?|.*xltm|.*dot(x)?|.*pot(x)?|.*pps(x)?")) {
            res = "application/vnd.openxmlformats";
        }
        return res;
    }

    public FileDto toDto() {
        return new FileDto(id, name, size);
    }
}
