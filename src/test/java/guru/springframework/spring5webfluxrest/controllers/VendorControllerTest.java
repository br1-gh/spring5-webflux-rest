package guru.springframework.spring5webfluxrest.controllers;

import guru.springframework.spring5webfluxrest.domain.Vendor;
import guru.springframework.spring5webfluxrest.repositories.VendorRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class VendorControllerTest {

    WebTestClient webTestClient;

    @Mock
    VendorRepository vendorRepository;

    VendorController vendorController;

    private final String VENDORS_BASE_URI = "/api/v1/vendors";

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        vendorController = new VendorController(vendorRepository);
        webTestClient = WebTestClient.bindToController(vendorController).build();
    }

    @Test
    public void getAllVendors() {
        BDDMockito.given(vendorController.getAllVendors())
                .willReturn(Flux.just(Vendor.builder().firstName("Bob").lastName("Marley").build(),
                        Vendor.builder().firstName("Elvis").lastName("Presley").build()));

        webTestClient.get()
                .uri(VENDORS_BASE_URI + "/")
                .exchange()
                .expectBodyList(Vendor.class)
                .hasSize(2);
    }

    @Test
    public void getVendorById() {
        BDDMockito.given(vendorController.getVendorById("1"))
                .willReturn(Mono.just(Vendor.builder().id("1").firstName("Bob").lastName("Marley").build()));

        webTestClient.get()
                .uri(VENDORS_BASE_URI + "/1")
                .exchange()
                .expectBody(Vendor.class)
                .value(vendor -> {
                    assertThat(vendor.getId(), is("1"));
                    assertThat(vendor.getFirstName(), is("Bob"));
                    assertThat(vendor.getLastName(), is("Marley"));
                });
    }
}