package com.github.Chestaci;

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
        Response response1 = getResponsePokemonName(name1);
        Pokemon pokemon1 = getPokemon(response1);
        Response response2 = getResponsePokemonName(name2);
        Pokemon pokemon2 = getPokemon(response2);

        SoftAssertions softAssertions = new SoftAssertions();
        checkStatusCode(response1, softAssertions);
        checkStatusCode(response2, softAssertions);
        checkContentType(response1, softAssertions);
        checkContentType(response2, softAssertions);
        softAssertions.assertThat(pokemon1.getWeight())
                .overridingErrorMessage("Полученный результат: вес покемона " + pokemon1.getName() +
                        " больше, чем вес покемона " + pokemon2.getName() + "." + "\n" + "Ожидаемый результат: вес покемона "
                        + pokemon1.getName() + " меньше, чем вес покемона " + pokemon2.getName() + ".")
                .isLessThan(pokemon2.getWeight());
        softAssertions.assertThat(pokemon1.getAbilities())
                .overridingErrorMessage("Полученный результат: у покемона " + pokemon1.getName() +
                        " нет способности убежать." + "Ожидаемый результат: у покемона " +
                        pokemon1.getName() + " должна быть способность убежать.")
                .anyMatch(pokemonAbility -> pokemonAbility.getAbility().getName().equals("run-away"));
        softAssertions.assertThat(pokemon2.getAbilities())
                .overridingErrorMessage("Полученный результат: у покемона " + pokemon2.getName() +
                        " есть способность убежать." + "Ожидаемый результат: у покемона " +
                        pokemon2.getName() + " не должно быть способности убежать.")
                .noneMatch(pokemonAbility -> pokemonAbility.getAbility().getName().equals("run-away"));
        softAssertions.assertAll();
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
        Response response = getResponsePokemonLimit(limit);
        PokemonList list = getPokemonList(response);

        SoftAssertions softAssertions = new SoftAssertions();
        checkStatusCode(response, softAssertions);
        checkContentType(response, softAssertions);
        softAssertions.assertThat(list.getResults().size())
                .overridingErrorMessage("Список покемонов не равен " + limit).isEqualTo(limit);
        softAssertions.assertThat(list.getResults())
                .overridingErrorMessage("Имя есть не у всех покемонов в ограниченном списке. " +
                        "Ожидаемый результат: у всех покемонов в списке есть имя.")
                .noneMatch(pokemon -> pokemon.getName().isEmpty() && pokemon.getName().isBlank());
        softAssertions.assertAll();
    }
}
