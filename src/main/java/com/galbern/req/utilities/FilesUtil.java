package com.galbern.req.utilities;

import com.galbern.req.jpa.entities.ApprovalStatus;
import com.galbern.req.jpa.entities.Requisition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

//@UtilityClass
@Component
public class FilesUtil {
    private static Logger LOGGER = LoggerFactory.getLogger(FilesUtil.class);


//    public static void main(String[] args) throws IOException {
//        Requisition requisition1 = Requisition.builder().id(123L).approvalStatus(ApprovalStatus.RECEIVED).build();
//        createDirectoryFile(requisition1);
//    }
//    public FilePermission fp = new FilePermission("<<ALL FILES>>", "write, read");

    public static String  createDirectoryFile(Requisition requisition) throws IOException {
        try {
            String fileName = String.format("requisitions%s%s%s%s.xls",
//                    "/",
                   File.separator,
                    requisition.getApprovalStatus().toString().toLowerCase(),
//                    "/",
                    File.separator,
                    Timestamp.from(Instant.now()).toString().replaceAll("[-:.\\s]", ""),
                    requisition.getId());
            File newFile = new File(fileName);
//            if(newFile.exists()){
                newFile.setReadable(true);
                newFile.setWritable(true);
                FilePermission fp = new FilePermission("<<ALL FILES>>", "write, read");
//            }
            LOGGER.info("New file successfully created: - {}", newFile.getAbsolutePath());
            OutputStream out = new FileOutputStream(newFile);
            out.close();

            LOGGER.info("New file successfully created: - {}", newFile.getAbsolutePath());
            return fileName;
        } catch (Exception e) {
            LOGGER.error("[FILE-CREATION-FAILURE] - an error occurred.", e);
            createFile(requisition);
            throw e;
        }
    }
    public static String createFile(Requisition requisition) throws IOException {
        LOGGER.info(" FILE CREATION - RETRY (2) STARTED");
        try {
            String fileName = String.format("requisitions/%s/%s%s.xls", requisition.getApprovalStatus().toString().toLowerCase(),Timestamp.from(Instant.now()).toString().replaceAll("[-:.\\s]", ""),
                    requisition.getId());
            LOGGER.info(" FILE {}", fileName);
            File myObj =  new File(fileName);
            LOGGER.info(myObj.createNewFile() ? "New file created: {}" : "File - {} already exists.", myObj.getName());
            LOGGER.info(" FILE CREATION - RETRY (2) ENDED SUCCESSFULLY");
            return fileName;
        } catch (IOException e) {
            LOGGER.error("[FILE-CREATION-FAILURE-2] - an error occurred.", e);
            throw e;
        }
    }
}
