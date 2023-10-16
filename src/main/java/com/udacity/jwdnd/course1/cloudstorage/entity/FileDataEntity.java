package com.udacity.jwdnd.course1.cloudstorage.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.units.qual.A;

import java.sql.Blob;
@AllArgsConstructor
@Setter
@Getter
public class FileDataEntity {
    private Blob fileData;
}
