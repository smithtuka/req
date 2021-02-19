package com.galbern.req.service.BO;

import com.galbern.req.dao.ItemDao;
import com.galbern.req.jpa.entities.Item;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.TransactionSystemException;

import java.util.List;

@Service
public class ItemServiceBO {

    public static Logger LOGGER = LoggerFactory.getLogger(ItemServiceBO.class);
    @Autowired
    private ItemDao itemDao;

    @Retryable(value = {DataAccessResourceFailureException.class, TransactionSystemException.class, CannotCreateTransactionException.class},
            maxAttempts = 2, backoff = @Backoff(delay = 500))
    public Item createItem(Item item) {
        try {
            itemDao.save(item);
        } catch (Exception e) {
            LOGGER.error("[SAVE-ITEM-FAILURE]- failed to save an item : {}", new Gson().toJson(item).replaceAll("[\n\r]+", ""));
        }
        return item;

    }


    @Retryable(value = {DataAccessResourceFailureException.class, TransactionSystemException.class, CannotCreateTransactionException.class},
            maxAttempts = 2, backoff = @Backoff(delay = 500))
    public Item findItemById(Long id) {
        Item item;
        try {
            item = itemDao.findById(id).get();
        } catch (Exception e) {
            LOGGER.error("[FIND-ITEM-BY-ID-FAILURE]- failed to find an item : {}", id);
            return null;
        }
        return item;
    }


    @Retryable(value = {DataAccessResourceFailureException.class, TransactionSystemException.class, CannotCreateTransactionException.class},
            maxAttempts = 2, backoff = @Backoff(delay = 500))
    public Item updateItem(Item newItem) {
        Item item;
        try {
            item = itemDao.findById(newItem.getId()).get();
            item = newItem;
            itemDao.save(item);
        } catch (Exception e) {
            LOGGER.error("[UPDATE-ITEM-FAILURE]- failed to update an item {}", newItem.getId());
            throw e;
        }
        return item;
    }


    @Retryable(value = {DataAccessResourceFailureException.class, TransactionSystemException.class, CannotCreateTransactionException.class},
            maxAttempts = 2, backoff = @Backoff(delay = 500))
    public void deleteItem(Long id) {
        try {
            itemDao.deleteById(id);
        } catch (Exception e) {
            LOGGER.error("[DELETE-ITEM-FAILURE]- could not  DELETE item: {}", id);
            throw e;
        }
    }

    @Retryable(value = {DataAccessResourceFailureException.class, TransactionSystemException.class, CannotCreateTransactionException.class},
            maxAttempts = 2, backoff = @Backoff(delay = 500))
    public List<Item> findItems(Long requisitionId, Long stageId, Long projectId, Long requesterId) {
        List<Item> items;
        try {
            if (null != requisitionId) items = itemDao.findItemsByRequisitionId(requisitionId);
            else if (null == stageId) items = itemDao.findItemsByRequisitionStageId(stageId);
            else if (null != projectId) items = itemDao.findItemsByRequisitionStageProjectId(projectId);
            else if(null != requesterId)items = itemDao.findItemsByRequisitionRequesterId(requesterId);
            else items = (List<Item>) itemDao.findAll();
        } catch (Exception e) {
            LOGGER.error("[FIND-ITEMS-FAILURE]- could not find items");
            throw e;
        }
        return items;


    }
}
