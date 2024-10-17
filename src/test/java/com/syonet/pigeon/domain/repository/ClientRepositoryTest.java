package com.syonet.pigeon.domain.repository;

import com.syonet.pigeon.domain.model.Client;
import com.syonet.pigeon.util.ClientCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
class ClientRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    ClientRepository repository;

    @Test
    @DisplayName("Find by email return client when successfull")
    void testFindByEmail_ReturnsClient_WhenSuccessfull() {
        Client client = ClientCreator.createClientToBeSaved();
        entityManager.persist(client);
        Optional<Client> clientFound = repository.findByEmail(client.getEmail());

        assertThat(clientFound).isNotEmpty().contains(client);;
        assertThat(clientFound.get().getEmail()).isEqualTo(client.getEmail());
    }

    @Test
    @DisplayName("Find by email return client when unsuccessfull")
    void testFindByEmail_ReturnsEmpty_WhenUnSuccessfull() {
        Client client = ClientCreator.createClientToBeSaved();
        entityManager.persist(client);
        Optional<Client> clientFound = repository.findByEmail("testeErro@hotmail.com");

        assertThat(clientFound).isEmpty();
    }

    @Test
    @DisplayName("Should save a client when record is valid")
    void testSaveSuccessful() {
        Client client = ClientCreator.createClientToBeSaved();
        final Client clientSaved = repository.save(client);

        final Client actual = entityManager.find(Client.class, clientSaved.getId());

        assertThat(clientSaved.getId()).isPositive();
        assertThat(clientSaved).isNotNull();
        assertThat(clientSaved.getName()).isEqualTo(client.getName());
        assertThat(clientSaved.getEmail()).isEqualTo(client.getEmail());
        assertThat(clientSaved.getDateBirth()).isEqualTo(client.getDateBirth());
        assertThat(actual).isEqualTo(clientSaved);
    }

    @Test
    @DisplayName("Should removes a client when record is valid")
    void testDeleteSuccessful() {
        Client client = ClientCreator.createClientToBeSaved();
        entityManager.persist(client);
        this.repository.delete(client);

        Optional<Client> clientOptional = repository.findById(client.getId());

        assertThat(clientOptional).isEmpty();
    }

}