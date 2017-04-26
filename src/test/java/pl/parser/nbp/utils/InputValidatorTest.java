package pl.parser.nbp.utils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Wojtek on 2017-04-23.
 */
public class InputValidatorTest {
    @Test
    public void shouldReturnTrueIfCorrectInput() throws Exception {
        //given
        InputValidator correctInput = new InputValidator("EUR", "2015-01-04", "2016-01-06");

        //when
        boolean isCorrect = correctInput.validate();

        //then
        assertTrue(isCorrect);
    }

    @Test
    public void shouldReturnFalseIfIncorrectDate() throws Exception {
        //given
        InputValidator startDateAfterEndDate = new InputValidator("EUR", "2016-01-04", "2015-01-06");
        InputValidator before2002 = new InputValidator("EUR", "2001-01-04", "2016-01-06");

        //when
        boolean isIncorrect = startDateAfterEndDate.validate();
        boolean isIncorrect2 = before2002.validate();

        //then
        assertFalse(isIncorrect);
        assertFalse(isIncorrect2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfIncorrectCurrencyCode() throws Exception {
        //given
        InputValidator incorrectCurrencyCode = new InputValidator("EUR2", "2016-01-04", "2015-01-06");

        //when
        incorrectCurrencyCode.validate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfIncorrectFormatDate() throws Exception {
        //given
        InputValidator incorrectFormatDate = new InputValidator("EUR2", "04-01-2016", "2015/01/06");

        //when
        incorrectFormatDate.validate();

        //then throw IllegalArgumentException
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfDateIsAfterPresent() throws Exception {
        //given
        InputValidator futureEndDateInInput = new InputValidator("EUR2", "04-01-2016", "2020/01/06");

        //when
        futureEndDateInInput.validate();

        //then throw IllegalArgumentException
    }
}