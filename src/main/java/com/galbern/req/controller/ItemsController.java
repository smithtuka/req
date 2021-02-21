package com.galbern.req.controller;


import com.galbern.req.jpa.entities.Item;
import com.galbern.req.service.BO.ItemServiceBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "controller for RMS service version v1", tags={"RMSV1ItemsController"})
@RestController
@RequestMapping("/v1/items")
public class ItemsController {

    @Autowired
    private ItemServiceBO itemServiceBO;
    public static Logger LOGGER = LoggerFactory.getLogger(ItemsController.class);


    @ApiOperation(value = "ping", nickname = "ping", notes = "to ping")
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS", response = String.class),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR", response = String.class)
    })
    @GetMapping("/ping")
    public String ping(@RequestParam(value="echo", required = false) String echo){
        LOGGER.info("GET /v1/items in {}", Thread.currentThread().getStackTrace()[1].getMethodName());
        return "ping pong!\n\t"+echo;
    }


    @ApiOperation(value = "createItem", nickname = "createAnItem", notes = "use to create an Item")
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS", response = Item.class),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR", response = Item.class)
    })
    @PostMapping
    public @ResponseBody
    ResponseEntity<Item> makeItem(@RequestBody Item item){

        try {
            LOGGER.info("POST /v1/items in {}", Thread.currentThread().getStackTrace()[1].getMethodName());
            return new ResponseEntity<>(itemServiceBO.createItem(item), HttpStatus.OK);
        } catch (Exception e){
            LOGGER.error("error - failed to create an item", e);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

    }



    @ApiOperation(value = "findItemById", nickname = "Items",
            notes = "to fetch a single Items by Id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS", response = Item.class),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR", response = String.class)
    })
    @GetMapping("/{itemId}")
    public ResponseEntity<Item> findItemById(@PathVariable("itemId") Long itemId){
        try{
            LOGGER.info("GET /v1/items {}", Thread.currentThread().getStackTrace()[1].getMethodName());
            return new ResponseEntity<>(itemServiceBO.findItemById(itemId), HttpStatus.OK);
        } catch (RuntimeException ex){
            LOGGER.error("error executing findItemById", ex);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ApiOperation(value = "findItems", nickname = "ALL",
            notes = "to fetch all items: all, requisitionId, stageId, projectId, requesterId")
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS", response = Item.class),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR", response = String.class)
    })
    @GetMapping
    public ResponseEntity<List<Item>> findItems(@RequestParam(value = "requisitionId", required = false) Long requisitionId,
                                                   @RequestParam(value = "stageId", required = false) Long stageId,
                                                   @RequestParam(value = "projectId", required = false) Long projectId,
                                                   @RequestParam(value = "requesterId", required = false) Long requesterId){
        try{
            LOGGER.info("GET /v1/items {}", Thread.currentThread().getStackTrace()[1].getMethodName());
            return new ResponseEntity<>(itemServiceBO.findItems(requisitionId, stageId, projectId, requesterId), HttpStatus.OK);
        } catch (RuntimeException ex){
            LOGGER.error("error executing findAllItems", ex);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ApiOperation(value = "deleteItem", nickname = "delete",
            notes = "to delete a single Item by Id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS", response = String.class),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR", response = String.class)
    })
    @DeleteMapping("/{itemId}")
    public ResponseEntity<String> deleteItem(@PathVariable("itemId") Long itemId){
        try{
            LOGGER.info("DELETE /v1/Items in {}", Thread.currentThread().getStackTrace()[1].getMethodName());
            itemServiceBO.deleteItem(itemId);
            return new ResponseEntity<>( "successfully deleted item : "+itemId, HttpStatus.OK);
        } catch (Exception ex){
            LOGGER.error("error executing deleteItem in handler", ex);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ApiOperation(value = "updateItem", nickname = "updateItem",
            notes = "to update Items")
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS", response = Item.class),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR", response = String.class)
    })
    @PutMapping
    public ResponseEntity<Item> updateItem(@RequestBody Item item){
        try{
            LOGGER.info("PUT /v1/items in {}", Thread.currentThread().getStackTrace()[1].getMethodName());
            return new ResponseEntity<>(itemServiceBO.updateItem(item), HttpStatus.OK);
        } catch (Exception ex){
            LOGGER.error("error updateItem in handler", ex);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }



}
