package com.jayway.serviceregistry.domain;

import com.jayway.serviceregistry.boot.ServiceRegistryStart;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ServiceRegistryStart.class)
public class ServiceRepositoryTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Autowired
    ServiceRepository serviceRepository;

    @Before @After public void
    drop_mongo_service_collection() throws Exception {
        serviceRepository.deleteAll();
    }

    @Test public void
    finds_services_by_name() {
        // Given
        Service savedService = serviceRepository.save(new Service("my-service", "Johan", "http://www.google.com"));

        // When
        Service foundService = serviceRepository.findByName("my-service");

        // Then
        assertThat(foundService).isEqualTo(savedService);
    }

    @Test public void
    finds_services_by_creator() {
        // Given
        Service savedService1 = serviceRepository.save(new Service("my-service1", "Johan", "http://www.google.com"));
        Service savedService2 = serviceRepository.save(new Service("my-service2", "Johan", "http://www.google.com/search?q=my-service2"));
        serviceRepository.save(new Service("my-service3", "Someone Else", "http://www.google.com/search?q=my-service3"));

        // When
        List<Service> foundServices = serviceRepository.findByCreatedBy("Johan");

        // Then
        assertThat(foundServices).containsOnlyOnce(savedService1, savedService2);
    }

    @Test public void
    saving_two_services_with_same_name_throws_duplicate_key_exception() {
        exception.expect(DuplicateKeyException.class);

        // Given
        Service service1 = serviceRepository.save(new Service("my-service1", "Johan", "http://www.google.com"));
        Service service2 = serviceRepository.save(new Service("my-service1", "Johan2", "http://www.google.com"));
        serviceRepository.save(service1);

        // When
        serviceRepository.save(service2);
    }

    @Test public void
    deleting_service_that_doesnt_exists_returns_silently() {
        serviceRepository.delete("52d8d32eda06c15406ae8bad");
    }
}
