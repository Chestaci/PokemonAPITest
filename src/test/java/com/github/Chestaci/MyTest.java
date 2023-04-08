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
    @Step("Получение Response по имени покемона. Отправка GET запроса")
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
    @Step("Получение Response по ограничению списка покемонов. Отправка GET запроса")
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

    @Step("Проверка отсутствия способности \"run-away\" у покемона")
    protected static void checkAbsenceAbility(Pokemon pokemon2, SoftAssertions softAssertions) {
        softAssertions.assertThat(pokemon2.getAbilities())
                .overridingErrorMessage("Полученный результат: у покемона " + pokemon2.getName() +
                        " есть способность убежать." + "Ожидаемый результат: у покемона " +
                        pokemon2.getName() + " не должно быть способности убежать.")
                .noneMatch(pokemonAbility -> pokemonAbility.getAbility().getName().equals("run-away"));
    }

    @Step("Проверка наличия у покемона способности \"run-away\"")
    protected static void checkPokemonAbility(Pokemon pokemon1, SoftAssertions softAssertions) {
        softAssertions.assertThat(pokemon1.getAbilities())
                .overridingErrorMessage("Полученный результат: у покемона " + pokemon1.getName() +
                        " нет способности убежать." + "Ожидаемый результат: у покемона " +
                        pokemon1.getName() + " должна быть способность убежать.")
                .anyMatch(pokemonAbility -> pokemonAbility.getAbility().getName().equals("run-away"));
    }

    @Step("Сравнение веса покемонов")
    protected static void pokemonWeightComparison(Pokemon pokemon1, Pokemon pokemon2, SoftAssertions softAssertions) {
        softAssertions.assertThat(pokemon1.getWeight())
                .overridingErrorMessage("Полученный результат: вес покемона " + pokemon1.getName() +
                        " больше, чем вес покемона " + pokemon2.getName() + "." + "\n" + "Ожидаемый результат: вес покемона "
                        + pokemon1.getName() + " меньше, чем вес покемона " + pokemon2.getName() + ".")
                .isLessThan(pokemon2.getWeight());
    }

    @Step("Проверка наличия имени у каждого покемона в ограниченном списке")
    protected static void checkPokemonName(PokemonList list, SoftAssertions softAssertions) {
        softAssertions.assertThat(list.getResults())
                .overridingErrorMessage("Имя есть не у всех покемонов в ограниченном списке. " +
                        "Ожидаемый результат: у всех покемонов в списке есть имя.")
                .noneMatch(pokemon -> pokemon.getName().isEmpty() && pokemon.getName().isBlank());
    }

    @Step("Проверка ограничения списка покемонов")
    protected static void checkPokemonListLimit(int limit, PokemonList list, SoftAssertions softAssertions) {
        softAssertions.assertThat(list.getResults().size())
                .overridingErrorMessage("Список покемонов не равен " + limit).isEqualTo(limit);
    }

    @AfterAll
    static void afterAll() {
        RestAssured.reset();
    }
}
