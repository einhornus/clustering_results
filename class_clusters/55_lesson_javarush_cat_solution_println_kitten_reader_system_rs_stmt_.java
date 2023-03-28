import Enums.*;
import java.sql.Time;
import java.util.Collection;

/**
 * Created by med_high on 14.02.2015.
 * Реализует интерфейс для доступа к классу Deal
 */
public interface DealType {
    /*
     * возвращает массив из колекци сделок с трейдами (обычно 2 : открытия и закрытия позиции, но
     * возможен вариант и с множественной последовательной покупкой или продажей отдного и того же тикера)
     * (может и не понадобиться) samlol
     * @return
     */
    public Trade[] getTrades();
    /*
    возвращает объект класса Time но правильную информацию содержит только сама дата!
    время не содержит корректного значения
    метод для определения даты
    для времени открытия и закрытия сделок есть отдельные методы
     */
    public Time getTime();
    /*
    возвращает строку с названием тикера (кодовым названием акции, например : "FB","SGMO")
     */
    public String getTicker();
    /*
    возвращает тип перечисления TradeType с первоначальным направлением(с режимом входа первой сделки)
     */
    public TradeType getTradeType();
    /*
    возвращает время открытия первой сделки (только время а не дату)
     */
    public Time getOpenTime();
    /*
    возвращает время закрытия последней сделки (только время)
     */
    public Time getCloseTime();
    /*
    возвращает тип перечисления MS с напрвлением позиции (margin или short)
     */
    public MS getDirection();
    /*
    возвращает максимальное количество акций текущей сделки находившейся на руках
    (или макс число занятых акций) -> в таком случае возвращает чило по модулю!
    например было взято в займы 100 и продано, а потом эту же операцию повторили с тем же тикером,
    возвращаемое значение будет 200 !
     */
    public int getVolume();
    /*
    возвращает среднюю цену общего кол. акций купленых или проданых по первоначальному направлению
    значение в долларах (цена на 1 акцию)
     */
    public double getAverageOpenPrice();
    /*
    возвращает среднюю цену общего кол. акций купленых или проданых по конечному направлению
    значение в долларах (цена на 1 акцию)
     */
    public double getAverageClosePrice();
    /*
    возвращает налог брокера на сделку(двухсторонюю(купил+продал)) размером в 100 акций
    значение в долларах
     */
    public double getEcnTax100();
    /*
    возвращает общий налог на все совершенные трейды в данной сделке с учетом кол.ва акций
    значение в долларах
     */
    public double getEcnTax();
    /*
    общая цена всех акций с учетом количества по первоначальному направлению
    значение в долларах(общая стоимость всех акций открытия)
     */
    public double getMoneyVolumeOpen();
    /*
    общая цена всех акций с учетом количества по конечному направлению
    значение в долларах(общая стоимость всех акций закрытия)
     */
    public double getMoneyVolumeClose();
    /*
    кол заработанной общей приыли без учета налога брокера
     */
    public double getPlGross();
    /*
    кол заработанной общей приыли с учетом налога брокера
     */
    public double getPlNet();
    /*
    логика метода из таблицы
     */
    public double getVolOnStartGross();
    /*
    логика метода из табицы
     */
    public double getVolOnStartNet();
    /*
    новая сумма депозита с учетом всей прибыли без налога брокера
     */
    public double getVolOnCloseGross();
    /*
    новая сумма депозита с учетом всей прибыли с налогом брокера
     */
    public double getVolOnCloseNet();

}

--------------------

package test.level06.lesson05.task03;

/* По 50 000 объектов Cat и Dog
Создать в цикле по 50 000 объектов Cat и Dog. (Java-машина должна начать уничтожать неиспользуемые, и метод finalize хоть раз да вызовется).
*/

public class Solution
{
    public static void main(String[] args)
    {
        //Напишите тут ваш код
        for (int i = 0; i < 50000; i++) {
            Cat cat = new Cat();
            Dog dog = new Dog();
        }

    }
}
class Cat
{
    @Override
    protected void finalize() throws Throwable
    {
        super.finalize();
        System.out.println("Cat destroyed");
    }
}

class Dog
{
    @Override
    protected void finalize() throws Throwable
    {
        super.finalize();
        System.out.println("Dog destroyed");
    }
}
--------------------

package com.javarush.test.level08.lesson03.task02;

/* HashMap из 10 пар
Создать коллекцию HashMap<String, String>, занести туда 10 пар строк:
арбуз – ягода, банан – трава, вишня – ягода, груша – фрукт, дыня – овощ, ежевика – куст, жень-шень – корень,
земляника – ягода, ирис – цветок, картофель – клубень.
Вывести содержимое коллекции на экран, каждый элемент с новой строки.
Пример вывода (тут показана только одна строка):
картофель – клубень
*/

import java.util.HashMap;
import java.util.Map;

public class Solution
{
    public static void main(String[] args) throws Exception
    {
        //Напишите тут ваш код
        Map<String, String> map = new HashMap<String, String>();
        map.put("арбуз", "ягода");
        map.put("банан", "трава");
        map.put("вишня", "ягода");
        map.put("груша", "фрукт");
        map.put("дыня", "овощ");
        map.put("ежевика", "куст");
        map.put("жень-шень", "корень");
        map.put("земляника", "ягода");
        map.put("ирис", "цветок");
        map.put("картофель", "клубень");

        for (Map.Entry<String, String> pairs : map.entrySet()) {
            System.out.println(pairs.getKey() + " - " + pairs.getValue());
        }
    }
}

--------------------

