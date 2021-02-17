package com.galbern.req.service.BO;

import com.galbern.req.dao.ItemDao;
import com.galbern.req.jpa.entities.Item;
import com.galbern.req.jpa.entities.Project;
import com.galbern.req.jpa.entities.Stage;
import com.galbern.req.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ItemDao itemDao;

    @Override
    public Item addItem(Item i) {
        return itemDao.save(i);
    }


//    @Override
//    public List<Item>addItems(List<Item> items) {
//        List<Item> items1 = itemDao.saveAll(items);
//        return (List<Item>)items1;
//    }

    @Override
    public Item findItem(Long id) {
        return itemDao.findById(id).orElse(null); // throw exception
    }

    @Override
    public Set<Item> findAll() {
        return (Set<Item> )itemDao.findAll();
    }

    @Override
    public Set<Item> retrieveItems(Stage stage) {
        return null;
    }

    @Override
    public Boolean updateItem(Item iNew) {
//     controller side - save() in spring data jpa is contextually for both merge() / update() and persist()/insert()
        Item i = findItem(iNew.getId());

        return true;
    }

    @Override
    public Set<Item> updateItems(Stage stage) {
        return null;
    }

    @Override
    public Set<Item> updateItems(Set<Item> items) {
        return null;
    }

    // go to Biz logic instead
    @Override
    public Set<Item> retrieveItems(Project project, Stage stage) {
        return null;
    }
    // go to Biz logic instead
    @Override
    public Set<Item> retrieveItems(Project project, Set<Stage> stages) {
        return null;
    }
    // go to Biz logic instead
    @Override
    public Set<Item> retrieveItems(Project project, LocalDate date) {
        return null;
    }
}
