package com.wargame.game.entities;

/**
 * Типы предметов экипировки
 */
public enum ItemType {
    WEAPON,     // Оружие (мушкет, штык)
    BANNER,     // Знамя
    ACCESSORY   // Бонусные предметы (орден, барабанщик)
}

/**
 * Класс предмета экипировки
 */
public class Item {
    
    private String name;
    private ItemType type;
    private String description;
    
    // Бонусы
    private int damageBonus;
    private int moraleBonus;
    private int movementBonus;
    private int defenseBonus;
    
    public Item(String name, ItemType type, String description) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.damageBonus = 0;
        this.moraleBonus = 0;
        this.movementBonus = 0;
        this.defenseBonus = 0;
    }
    
    public void setBonuses(int damage, int morale, int movement, int defense) {
        this.damageBonus = damage;
        this.moraleBonus = morale;
        this.movementBonus = movement;
        this.defenseBonus = defense;
    }
    
    // Геттеры
    public String getName() { return name; }
    public ItemType getType() { return type; }
    public String getDescription() { return description; }
    public int getDamageBonus() { return damageBonus; }
    public int getMoraleBonus() { return moraleBonus; }
    public int getMovementBonus() { return movementBonus; }
    public int getDefenseBonus() { return defenseBonus; }
}
