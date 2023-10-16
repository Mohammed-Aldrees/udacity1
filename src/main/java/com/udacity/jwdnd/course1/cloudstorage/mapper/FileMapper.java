package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.entity.FileInfoEntity;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;

import java.sql.Blob;
import java.util.List;

@Mapper
public interface FileMapper {

    @Select("SELECT * FROM FILES WHERE userid = #{userid}")
    List<FileInfoEntity> getAllFilesFromUserId(Integer userid);

    @Select("SELECT * FROM FILES WHERE fileid = #{filedId}")
    File getOneFileInfo(Integer fileId);

    @Select("SELECT fileid FROM FILES WHERE ( filename, contenttype, filesize, userid) = ( #{fileName}, #{contentType}, #{fileSize}, #{userId})")
    Integer getFileId(FileInfoEntity fileInfoEntity);

    @Insert("INSERT INTO FILES ( filename, contenttype, filesize, userid) VALUES( #{fileName}, #{contentType}, #{fileSize}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    void insert(FileInfoEntity file);

    @Update("UPDATE FILES SET filedata= #{fileData} WHERE fileId = #{fileId}")
    void updateFileData(Blob fileData, int fileId);

    @Delete("DELETE FROM FILES WHERE fileId = #{fileId}")
    void deleteFile(int fileId);

}
