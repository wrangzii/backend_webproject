//package com.project.web.service;
//
//import com.google.api.services.drive.Drive;
//import com.google.api.services.drive.model.File;
//import com.google.api.services.drive.model.FileList;
//import com.project.web.payload.response.ResponseObject;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.io.OutputStream;
//import java.security.GeneralSecurityException;
//import java.util.Collections;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class FileService {
//
//    private final GoogleDriveService googleDriveServ;
//
//    public List<File> listEverything() throws IOException, GeneralSecurityException {
//        // Print the names and IDs for up to 10 files.
//        FileList result = googleDriveServ.getInstance().files().list()
//                .setPageSize(10)
//                .setFields("nextPageToken, files(id, name)")
//                .execute();
//        return result.getFiles();
//    }
//
//    public List<File> listFolderContent(String parentId) throws IOException, GeneralSecurityException {
//        if (parentId == null) {
//            parentId = "root";
//        }
//        String query = "'" + parentId + "' in parents";
//        FileList result = googleDriveServ.getInstance().files().list()
//                .setQ(query)
//                .setPageSize(10)
//                .setFields("nextPageToken, files(id, name)")
//                .execute();
//        return result.getFiles();
//    }
//
//    public ResponseEntity<ResponseObject> downloadFile(String id, OutputStream outputStream) throws IOException, GeneralSecurityException {
//        if (id != null) {
//            googleDriveServ.getInstance().files().get(id).executeMediaAndDownloadTo(outputStream);
//            return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.toString(),"Download file with id :" + id + " successfully!"));
//        }
//        return ResponseEntity.badRequest().body(new ResponseObject(HttpStatus.NOT_FOUND.toString(),"Not found file "));
//    }
//
//    public ResponseEntity<ResponseObject> deleteFile(String fileId) throws Exception {
//        googleDriveServ.getInstance().files().delete(fileId).execute();
//        return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.toString(),"Delete file with id :" + fileId + " successfully!"));
//    }
//
//    public String getFolderId(String path) throws Exception {
//        String parentId = null;
//        String[] folderNames = path.split("/");
//
//        Drive driveInstance = googleDriveServ.getInstance();
//        for (String name : folderNames) {
//            parentId = findOrCreateFolder(parentId, name, driveInstance);
//        }
//        return parentId;
//    }
//
//    private String findOrCreateFolder(String parentId, String folderName, Drive driveInstance) throws Exception {
//        String folderId = searchFolderId(parentId, folderName, driveInstance);
//        // Folder already exists, so return id
//        if (folderId != null) {
//            return folderId;
//        }
//        //Folder don't exist, create it and return folderId
//        File fileMetadata = new File();
//        fileMetadata.setMimeType("application/vnd.google-apps.folder");
//        fileMetadata.setName(folderName);
//
//        if (parentId != null) {
//            fileMetadata.setParents(Collections.singletonList(parentId));
//        }
//        return driveInstance.files().create(fileMetadata)
//                .setFields("id")
//                .execute()
//                .getId();
//    }
//
//    private String searchFolderId(String parentId, String folderName, Drive service) throws Exception {
//        String folderId = null;
//        String pageToken = null;
//        FileList result = null;
//
//        File fileMetadata = new File();
//        fileMetadata.setMimeType("application/vnd.google-apps.folder");
//        fileMetadata.setName(folderName);
//
//        do {
//            String query = " mimeType = 'application/vnd.google-apps.folder' ";
//            if (parentId == null) {
//                query = query + " and 'root' in parents";
//            } else {
//                query = query + " and '" + parentId + "' in parents";
//            }
//            result = service.files().list().setQ(query)
//                    .setSpaces("drive")
//                    .setFields("nextPageToken, files(id, name)")
//                    .setPageToken(pageToken)
//                    .execute();
//
//            for (File file : result.getFiles()) {
//                if (file.getName().equalsIgnoreCase(folderName)) {
//                    folderId = file.getId();
//                }
//            }
//            pageToken = result.getNextPageToken();
//        } while (pageToken != null && folderId == null);
//
//        return folderId;
//    }
//}
