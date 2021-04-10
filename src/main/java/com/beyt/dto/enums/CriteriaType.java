package com.beyt.dto.enums;


/**
 * Created by tdilber at 15-Sep-19
 */
public enum CriteriaType {
    NONE(0, ""),
    CONTAIN(1, "İçeriyor (String)"),
    DOES_NOT_CONTAIN(2, "İçermiyor (String)"),
    END_WITH(3, "İle bitiyor (String)"),
    START_WITH(4, "İle başlıyor (String)"),
    SPECIFIED(5, "Not Null ise 'true', Null ise 'false'"),
    EQUAL(6, "Eşittir"),
    NOT_EQUAL(7, "Eşit Değildir"),
    GREATER_THAN(8, "Büyüktür"),
    GREATER_THAN_OR_EQUAL(9, "Büyüktür veya eşittir"),
    LESS_THAN(10, "Küçüktür"),
    LESS_THAN_OR_EQUAL(11, "Küçüktür veya eşittir"),
    OR(12, "Veya (Konulduğu yeri 2 ye Böler) (key ve values önemsizdir)"),
    TOP_ASC(13, "Artan sıralamaya göre ilk (x)"),
    TOP_DESC(14, "Azalan sıralamaya göre ilk (x)"),
    PAGE_ASC(15, "Artan sıralamaya göre (y). inci (x) kadar eleman getir"),
    PAGE_DESC(16, "Azalan sıralamaya göre  (y). inci (x) kadar eleman getir"),
    SORT_ASC(17, "Artan Sıralama"),
    SORT_DESC(18, "Azalan Sıralama");

    private int value = -1;
    private String meaning;

    CriteriaType(int value, String meaning) {
        this.value = value;
        this.meaning = meaning;
    }

    public int getValue() {
        return value;
    }

    public String getMeaning() {
        return meaning;
    }
}
