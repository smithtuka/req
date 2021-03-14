package com.galbern.req.utilities;

import com.galbern.req.jpa.entities.Requisition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicReference;

//@UtilityClass
@Component
public class FilesUtil {
    private static Logger LOGGER = LoggerFactory.getLogger(FilesUtil.class);


//    public static void main(String[] args) throws IOException {
//        Requisition requisition1 = Requisition.builder().id(123L).approvalStatus(ApprovalStatus.RECEIVED).build();
//        createDirectoryFile(requisition1);
//    }

    public String  createDirectoryFile(Requisition requisition) throws IOException {
        try {

            String fileName = String.format("requisitions%s%s%s%s.xls",
                    File.separator,
                    requisition.getApprovalStatus(),
                    File.separator,
                    Timestamp.from(Instant.now()).toString().replaceAll("[-:.\\s]", ""),
                    requisition.getId());
            File newFile = new File(fileName);
            OutputStream out = new FileOutputStream(newFile);
            out.close();
            LOGGER.info("New file successfully created: - {}", newFile);
            return fileName;
        } catch (Exception e) {
            LOGGER.info("[FILE-CREATION-FAILURE] - an error occurred.", e);
            LOGGER.error("[FILE-CREATION-FAILURE] - an error occurred.", e);
            createFile(requisition);
            throw e;
        }
    }

    public String createFile(Requisition requisition) throws IOException {
        LOGGER.info(" FILE CREATION - RETRY (2) STARTED");
        try {
            String fileName = String.format("%s%s.xls", Timestamp.from(Instant.now()).toString().replaceAll("[-:.\\s]", ""),
                    requisition.getId());
            AtomicReference<File> myObj = new AtomicReference<>(new File(fileName));
//            myObj.get().mkdir();
            LOGGER.info(myObj.get().createNewFile() ? "New file created: {}" : "File - {} already exists.", myObj.get().getName());
            LOGGER.info(" FILE CREATION - RETRY (2) ENDED");
            return fileName;
        } catch (IOException e) {
            LOGGER.error("[FILE-CREATION-FAILURE-2] - an error occurred.", e);
            throw e;
        }
    }
}
