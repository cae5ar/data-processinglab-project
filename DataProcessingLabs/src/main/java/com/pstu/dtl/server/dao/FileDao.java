package com.pstu.dtl.server.dao;

import org.springframework.stereotype.Repository;

import com.pstu.dtl.server.domain.File;

@Repository
public class FileDao extends JpaDao<File> {

    @Override
    public Class<File> getEntityClass() {
        return File.class;
    }
}
