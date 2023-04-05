package com.github.Chestaci;

import com.github.Chestaci.model.Pokemon;
import com.github.Chestaci.util.JSONUtils;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;

import static io.restassured.RestAssured.given;

abstract public class MyTest {
    protected static final HashMap<String, Pokemon> pokemons = new HashMap<>();
    protected final RequestSpecification requestSpecification = given().baseUri("https://pokeapi.co/api/v2/pokemon/");

    /**
     * Метод для получения ответа на запрос по имени покемона
     *
     * @param name Имя покемона
     * @return ответ на запрос
     */
    @Step("Получение ответа на запрос по имени покемона")
    protected Response getPokemonResponse(String name) {
        Response response = requestSpecification.pathParams("name", name).when().get("{name}/");

        getPokemonJSON(name, response);

        return response;
    }

    /**
     * Метод для получения покемона по имени в формате JSON,
     * а так же для сохранения его в HashMap для дальнейшего использования
     *
     * @param name     Имя покемона
     * @param response Ответ на запрос
     */
    @Step("Получение покемона {name}")
    protected void getPokemonJSON(String name, Response response) {
        String pokemonJSON = response.then().extract().asString();
        if (!pokemons.containsKey(name)) {
            pokemons.put(name, JSONUtils.jsonToObject(pokemonJSON, Pokemon.class));
        }
    }
}
