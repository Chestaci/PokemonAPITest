package com.github.Chestaci.pokemon_api;

import com.github.Chestaci.model.PokemonList;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.assertj.core.api.SoftAssertions;

public class GetLimitPokemonListMethodAPI extends GetMethodAPI {
    public GetLimitPokemonListMethodAPI(String baseUri) {
        super(baseUri);
    }

    /**
     * Метод для получения Response по ограничению списка покемонов
     *
     * @param limit Ограничение списка покемонов
     * @return ответ сервера на запрос
     */
    @Step("Получение Response по ограничению списка покемонов.")
    public Response getResponsePokemonLimit(int limit) {
        return getResponseCallMethod("?limit={limit}/", "limit", String.valueOf(limit));
    }

    /**
     * Метод для получения списка покемонов
     *
     * @param response Ответ сервера на запрос
     * @return список покемонов
     * @see PokemonList
     */
    @Step("Получение списка покемонов")
    public PokemonList getPokemonList(Response response) {
        return response
                .then()
                .extract()
                .body()
                .as(PokemonList.class);
    }

    @Step("Проверка наличия имени у каждого покемона в ограниченном списке")
    public void checkPokemonName(PokemonList list, SoftAssertions softAssertions) {
        softAssertions
                .assertThat(list.getResults())
                .withFailMessage("Имя есть не у всех покемонов в ограниченном списке. " +
                        "Ожидаемый результат: у всех покемонов в списке есть имя.")
                .noneMatch(pokemon -> pokemon.getName().isEmpty() && pokemon.getName().isBlank());
    }

    @Step("Проверка ограничения списка покемонов")
    public void checkPokemonListLimit(int limit, PokemonList list, SoftAssertions softAssertions) {
        softAssertions
                .assertThat(list.getResults().size())
                .withFailMessage("Список покемонов не равен " + limit)
                .isEqualTo(limit);
    }
}
