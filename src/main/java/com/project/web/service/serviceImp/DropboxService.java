package com.project.web.service.serviceImp;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.Metadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Service
public class DropboxService {

    private static final Logger logger = LoggerFactory.getLogger(DropboxService.class);

    @Autowired
    DbxClientV2 dropboxClient;

    public void uploadFile(MultipartFile file, String filePath) throws IOException, DbxException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(file.getBytes());
        Metadata uploadMetaData = dropboxClient.files().uploadBuilder(filePath).uploadAndFinish(inputStream);
        logger.info("upload meta data =====> {}", uploadMetaData.toString());
        inputStream.close();
    }

    public void downloadFile(String cateName) throws IOException, DbxException {
        OutputStream outputStream = new FileOutputStream(cateName);
        dropboxClient.files().downloadZipBuilder("/" + cateName).download(outputStream);
    }

    public void deleteFile(String filePath) throws DbxException {
        dropboxClient.files().deleteV2(filePath);
    }
}
