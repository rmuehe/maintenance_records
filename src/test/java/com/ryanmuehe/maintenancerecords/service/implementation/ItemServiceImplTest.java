package com.ryanmuehe.maintenancerecords.service.implementation;

import com.ryanmuehe.maintenancerecords.model.Item;
import com.ryanmuehe.maintenancerecords.model.User;
import com.ryanmuehe.maintenancerecords.model.repository.ItemRepository;
import com.ryanmuehe.maintenancerecords.model.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(ItemServiceImpl.class)
@DisplayName("ItemServiceImpl Test")
public class ItemServiceImplTest {

    @Autowired
    private ItemServiceImpl itemService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    @DisplayName("Find Items By User ID")
    public void shouldFindItemsByIdWhenExist() {
        // Given a user and items associated with that user in the database
        User user = new User();
        user.setEmail("thisGuy@example.com");
        user.setUsername("ThisGuy");
        user.setPassword("12345678");
        user.setDateCreated(LocalDateTime.now());
        user.setLastUpdated(LocalDateTime.now());
        userRepository.save(user);

        Item item1 = new Item();
        item1.setName("Thing1");
        item1.setOwner(user);
        item1.setDateCreated(LocalDateTime.now());
        item1.setLastUpdated(LocalDateTime.now());
        itemRepository.save(item1);

        Item item2 = new Item();
        item2.setName("Thing2");
        item2.setOwner(user);
        item2.setDateCreated(LocalDateTime.now());
        item2.setLastUpdated(LocalDateTime.now());
        itemRepository.save(item2);

        // When fetching items by user ID
        List<Item> items = itemService.findItemsByUserId(user.getId());

        // Then the retrieved items should match the saved items
        assertThat(items).hasSize(2).contains(item1, item2);
    }
}
