package com.github.Chestaci;

import com.github.Chestaci.pokemon_api.GetLimitPokemonListMethodAPI;
import com.github.Chestaci.pokemon_api.GetPokemonMethodAPI;
import com.github.Chestaci.model.Pokemon;
import com.github.Chestaci.model.PokemonList;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;


@DisplayName("Тесты проверки покемонов")
@Feature("Тесты проверки GET запросов")
public class APITest extends MyTest {

    /**
     * Тест для осуществления проверки, что у покемона rattata,
     * в отличие от покемона pidgeotto, меньше вес и есть умение
     * (ability) побег (run-away)
     *
     * @param name1 Имя первого покемона
     * @param name2 Имя второго покемона
     */
    @DisplayName("Проверка сравнения двух покемонов по весу и способности убежать")
    @Description("Тест для осуществления проверки сравнения покемонов rattata и pidgeotto по весу и способности убежать")
    @Story("Тест сравнения двух покемонов")
    @ParameterizedTest
    @CsvSource(value = {"rattata, pidgeotto"}, ignoreLeadingAndTrailingWhitespace = true)
    public void weightAndRunAwayAbilityPokemonComparisonTest(String name1, String name2) {
        GetPokemonMethodAPI getPokemonMethodAPI = new GetPokemonMethodAPI("https://pokeapi.co/api/v2/pokemon/");

        Response response1 = getPokemonMethodAPI.getResponsePokemonName(name1);
        Pokemon pokemon1 = getPokemonMethodAPI.getPokemon(response1);
        Response response2 = getPokemonMethodAPI.getResponsePokemonName(name2);
        Pokemon pokemon2 = getPokemonMethodAPI.getPokemon(response2);

        SoftAssertions.assertSoftly(softly -> {
            getPokemonMethodAPI.checkStatusCode(response1, softly);
            getPokemonMethodAPI.checkStatusCode(response2, softly);
            getPokemonMethodAPI.checkContentType(response1, softly);
            getPokemonMethodAPI.checkContentType(response2, softly);
            getPokemonMethodAPI.pokemonWeightComparison(pokemon1, pokemon2, softly);
            getPokemonMethodAPI.checkPokemonAbility(pokemon1, softly);
            getPokemonMethodAPI.checkAbsenceAbility(pokemon2, softly);
        });
    }

    /**
     * Параметризованный тест для осуществления проверки ограничения списка (limit) покемонов
     * и наличия имени у каждого покемона в ограниченном списке
     *
     * @param limit Ограничение списка покемонов
     */
    @DisplayName("Проверка ограничения списка покемонов и наличия имени")
    @Description("Параметризованный тест для осуществления проверки ограничения списка покемонов " + "и наличия имени у каждого покемона в ограниченном списке")
    @Story("Тест ограничения списка покемонов и проверки наличия имени")
    @ParameterizedTest
    @ValueSource(ints = {30, 50})
    public void listLimitCheckAndNameAvailabilityTest(int limit) {
        GetLimitPokemonListMethodAPI getLimitPokemonListMethodAPI =
                new GetLimitPokemonListMethodAPI("https://pokeapi.co/api/v2/pokemon/");

        Response response = getLimitPokemonListMethodAPI.getResponsePokemonLimit(limit);
        PokemonList list = getLimitPokemonListMethodAPI.getPokemonList(response);

        SoftAssertions.assertSoftly(softly -> {
            getLimitPokemonListMethodAPI.checkStatusCode(response, softly);
            getLimitPokemonListMethodAPI.checkContentType(response, softly);
            getLimitPokemonListMethodAPI.checkPokemonListLimit(limit, list, softly);
            getLimitPokemonListMethodAPI.checkPokemonName(list, softly);
        });
    }
}
