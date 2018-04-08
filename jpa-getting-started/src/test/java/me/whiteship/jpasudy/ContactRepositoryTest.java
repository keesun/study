package me.whiteship.jpasudy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ContactRepositoryTest {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void crud() {
        Contact contact = new Contact();
        contact.setHomeAddress("12314124 ne 12fw Redmond");
        contact.setCreated(LocalDateTime.now());
        contact.setNotes("notes");

        Name keesun = new Name();
        keesun.setFirst("Keesun");
        keesun.setLast("Baik");
        contact.setName(keesun);

        assertThat(contact.getId()).isNull();

        // Insert
        Contact newContact = contactRepository.saveAndFlush(contact);

        assertThat(newContact.getId()).isNotNull();
        assertThat(contact).isEqualTo(newContact);

        entityManager.clear();

        // Select
        Contact existingContact = contactRepository.findById(newContact.getId()).orElse(new Contact());
        assertThat(newContact).isNotEqualTo(existingContact);

        existingContact.setNotes("I like Soraka!");
        existingContact.setCreated(LocalDateTime.now());
        // Update
        contactRepository.flush();

        contactRepository.delete(existingContact);
        // Delete
        contactRepository.flush();
    }

}