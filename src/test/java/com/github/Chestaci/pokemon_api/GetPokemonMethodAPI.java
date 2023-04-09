package com.github.Chestaci.pokemon_api;

import com.github.Chestaci.model.Pokemon;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.assertj.core.api.SoftAssertions;

public class GetPokemonMethodAPI extends GetMethodAPI {

    public GetPokemonMethodAPI(String baseUri) {
        super(baseUri);
    }

    /**
     * Метод для получения Response по имени покемона
     *
     * @param name Имя покемона
     * @return ответ сервера на запрос
     */
    @Step("Получение Response по имени покемона.")
    public Response getResponsePokemonName(String name) {
        return getResponseCallMethod("{name}/", "name", name);
    }

    /**
     * Метод для получения покемона
     *
     * @param response Ответ сервера на запрос
     * @return покемона
     */
    @Step("Получение покемона")
    public Pokemon getPokemon(Response response) {
        return response
                .then()
                .extract()
                .as(Pokemon.class);
    }

    @Step("Проверка отсутствия способности \"run-away\" у покемона")
    public void checkAbsenceAbility(Pokemon pokemon2, SoftAssertions softAssertions) {
        softAssertions
                .assertThat(pokemon2.getAbilities())
                .withFailMessage("Полученный результат: у покемона " +
                        pokemon2.getName() +
                        " есть способность убежать." +
                        "Ожидаемый результат: у покемона " +
                        pokemon2.getName() +
                        " не должно быть способности убежать.")
                .noneMatch(pokemonAbility -> pokemonAbility.getAbility().getName().equals("run-away"));
    }

    @Step("Проверка наличия у покемона способности \"run-away\"")
    public void checkPokemonAbility(Pokemon pokemon1, SoftAssertions softAssertions) {
        softAssertions
                .assertThat(pokemon1.getAbilities())
                .withFailMessage("Полученный результат: у покемона " +
                        pokemon1.getName() +
                        " нет способности убежать." +
                        "Ожидаемый результат: у покемона " +
                        pokemon1.getName() +
                        " должна быть способность убежать.")
                .anyMatch(pokemonAbility -> pokemonAbility.getAbility().getName().equals("run-away"));
    }

    @Step("Сравнение веса покемонов")
    public void pokemonWeightComparison(Pokemon pokemon1, Pokemon pokemon2, SoftAssertions softAssertions) {
        softAssertions
                .assertThat(pokemon1.getWeight())
                .withFailMessage("Полученный результат: вес покемона " +
                        pokemon1.getName() +
                        " больше, чем вес покемона " +
                        pokemon2.getName() +
                        "." +
                        "\n" +
                        "Ожидаемый результат: вес покемона " +
                        pokemon1.getName() +
                        " меньше, чем вес покемона " +
                        pokemon2.getName() +
                        ".")
                .isLessThan(pokemon2.getWeight());
    }
}
