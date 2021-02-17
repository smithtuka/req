package com.galbern.req.service;

import com.galbern.req.jpa.entities.Item;
import com.galbern.req.jpa.entities.Project;
import com.galbern.req.jpa.entities.Stage;

import java.time.LocalDate;
import java.util.Set;


public interface ItemService {

    public Item addItem(Item i);
//    public List<Item> addItems(List<Item> items);
    public Item findItem(Long id);
    public Set<Item> findAll();

    public Set<Item> retrieveItems(Stage stage);
    public Set<Item> retrieveItems(Project project, Stage stage);
    public Set<Item> retrieveItems(Project project, Set<Stage> stages);
    public Set<Item> retrieveItems(Project project, LocalDate date);

    public Boolean updateItem(Item iNew);
    public Set<Item> updateItems(Stage stage);
    public Set<Item> updateItems(Set<Item> items);

}