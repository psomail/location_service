package ru.logisticplatform.rest;

import org.junit.Assert;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestRestControllerTest {

    public static String testVar;

    @DisplayName("FirstTest runs")

    @BeforeAll
    public void setUp(){
        testVar = "Hello world";
    }

    @Test
    @Tag("name_of_test")
    void getUser() {
        Assert.assertEquals("should be - Hello world", testVar, "Hello world ");
    }

    @Test
    @Disabled
    @Tag("disabled_test")
    void disabledTest() {

    }

    @AfterEach
    public void doItAfterEachTestMethod(){

    }

    @AfterAll
    public void lastMethodBeforTestOver(){

    }


}