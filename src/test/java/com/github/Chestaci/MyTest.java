package com.github.Chestaci;

import com.github.Chestaci.model.Pokemon;
import com.github.Chestaci.model.PokemonList;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import static io.restassured.RestAssured.given;

abstract public class MyTest {

    @BeforeAll
    static void beforeAll() {
        RestAssured.filters(new AllureRestAssured());
    }

    /**
     * Метод для получения RequestSpecification
     *
     * @return RequestSpecification
     * @see RequestSpecification
     */
    @Step("Получение RequestSpecification")
    protected static RequestSpecification getRequestSpecification() {
        return given()
                .baseUri("https://pokeapi.co/api/v2/pokemon/");
    }

    /**
     * Метод для получения Response по имени покемона
     *
     * @param name Имя покемона
     * @return ответ сервера на запрос
     */
    @Step("Получение Response по имени покемона")
    protected static Response getResponsePokemonName(String name) {
        return getRequestSpecification()
                .pathParams("name", name)
                .when()
                .get("{name}/");
    }

    /**
     * Метод для получения покемона
     *
     * @param response Ответ сервера на запрос
     * @return покемона
     */
    @Step("Получение покемона")
    protected static Pokemon getPokemon(Response response) {
        return response
                .then()
                .extract()
                .as(Pokemon.class);
    }

    /**
     * Метод для получения Response по ограничению списка покемонов
     *
     * @param limit Ограничение списка покемонов
     * @return ответ сервера на запрос
     */
    @Step("Получение Response по ограничению списка покемонов")
    protected static Response getResponsePokemonLimit(int limit) {
        return getRequestSpecification()
                .pathParams("limit", limit)
                .when()
                .get("?limit={limit}/");
    }

    /**
     * Метод для получения списка покемонов
     *
     * @param response Ответ сервера на запрос
     * @return список покемонов
     * @see PokemonList
     */
    @Step("Получение списка покемонов")
    protected static PokemonList getPokemonList(Response response) {
        return response
                .then()
                .extract()
                .body()
                .as(PokemonList.class);
    }

    @Step("Проверка Content Type")
    protected static void checkContentType(Response response, SoftAssertions softAssertions) {
        softAssertions
                .assertThat(response.contentType())
                .isEqualTo(ContentType.JSON.withCharset("utf-8"));
    }

    @Step("Проверка Status Code")
    protected static void checkStatusCode(Response response, SoftAssertions softAssertions) {
        softAssertions
                .assertThat(response.getStatusCode())
                .isEqualTo(200);
    }

    @AfterAll
    static void afterAll() {
        RestAssured.reset();
    }
}
