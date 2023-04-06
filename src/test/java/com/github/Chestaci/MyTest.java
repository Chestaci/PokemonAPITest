package com.github.Chestaci;

import com.github.Chestaci.model.Pokemon;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import static io.restassured.RestAssured.given;

abstract public class MyTest {

    @BeforeAll
    static void beforeAll() {
        RestAssured.filters(new AllureRestAssured());
    }

    /**
     * Метод для получения покемона по имени
     * @param name Имя покемона
     * @return покемона
     */
    @Step("Получение покемона {name}")
    protected static Pokemon getPokemon(String name) {
        return given()
                .baseUri("https://pokeapi.co/api/v2/pokemon/")
                .pathParams("name", name).when().get("{name}/")
                .then()
                .extract()
                .body()
                .as(Pokemon.class);
    }

    @AfterAll
    static void afterAll() {
        RestAssured.reset();
    }
}
