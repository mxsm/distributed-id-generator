package com.github.mxsm;

import static org.junit.Assert.assertTrue;

import com.github.javafaker.Faker;
import java.util.Locale;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {





    }

    public static void main(String[] args) {
        assertTrue( true );
        Faker faker = new Faker(new Locale("zh-cn"));
        String ipV4Address = faker.internet().ipV4Address();
        String name = faker.name().fullName();
        int age = faker.number().numberBetween(1, 100);
        String email = faker.internet().emailAddress();
        String password = faker.internet().password(10, 20, true, true);
        String phoneNumber = faker.phoneNumber().phoneNumber();
    }

}
