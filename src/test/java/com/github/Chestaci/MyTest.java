package com.github.Chestaci;

import com.github.Chestaci.model.Pokemon;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

abstract public class MyTest {
    protected final RequestSpecification requestSpecification = given().baseUri("https://pokeapi.co/api/v2/pokemon/");

    /**
     * Метод для получения покемона по имени
     * @param name Имя покемона
     * @return покемона
     */
    @Step("Получение покемона {name}")
    protected Pokemon getPokemon(String name) {
        AllureRestAssured allureRestAssured = new AllureRestAssured();
        allureRestAssured.setRequestAttachmentName("Request for " + name);
        allureRestAssured.setResponseAttachmentName("Response for " + name);

        return requestSpecification
                .pathParams("name", name)
                .filter(allureRestAssured)
                .when()
                .get("{name}/")
                .then()
                .extract()
                .body()
                .as(Pokemon.class);
    }
}
