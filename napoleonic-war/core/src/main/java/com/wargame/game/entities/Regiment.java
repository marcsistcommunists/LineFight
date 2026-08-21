package com.wargame.game.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс представляющий полк армии.
 * Полк имеет характеристики, инвентарь и визуальное представление (одно для всех).
 */
public class Regiment {
    
    public enum RegimentType {
        LINE_INFANTRY,    // Линейная пехота
        LIGHT_INFANTRY,   // Легкая пехота/егеря
        CAVALRY,          // Кавалерия
        ARTILLERY         // Артиллерия
    }
    
    private String name;
    private RegimentType type;
    
    // Характеристики
    private int maxHealth;
    private int currentHealth;
    private int morale;       // Мораль (0-100)
    private int experience;   // Опыт
    private int range;        // Дальность стрельбы
    private int damage;       // Урон
    private int movement;     // Очки движения за ход
    
    // Инвентарь
    private Inventory inventory;
    
    // Состояние
    private boolean isDestroyed;
    
    // ID изображения поверженного полка (NSFW сцена)
    private String defeatedImageId;

    public Regiment(String name, RegimentType type) {
        this.name = name;
        this.type = type;
        this.inventory = new Inventory();
        this.isDestroyed = false;
        this.defeatedImageId = "defeated_" + type.name().toLowerCase();
        
        // Базовые характеристики в зависимости от типа
        initializeStats();
    }
    
    private void initializeStats() {
        switch (type) {
            case LINE_INFANTRY:
                this.maxHealth = 100;
                this.morale = 70;
                this.experience = 10;
                this.range = 3;
                this.damage = 25;
                this.movement = 4;
                break;
            case LIGHT_INFANTRY:
                this.maxHealth = 80;
                this.morale = 65;
                this.experience = 15;
                this.range = 4;
                this.damage = 20;
                this.movement = 5;
                break;
            case CAVALRY:
                this.maxHealth = 120;
                this.morale = 75;
                this.experience = 20;
                this.range = 1;
                this.damage = 35;
                this.movement = 8;
                break;
            case ARTILLERY:
                this.maxHealth = 60;
                this.morale = 60;
                this.experience = 10;
                this.range = 6;
                this.damage = 40;
                this.movement = 2;
                break;
        }
        this.currentHealth = maxHealth;
    }
    
    public void takeDamage(int damage) {
        this.currentHealth = Math.max(0, this.currentHealth - damage);
        
        // Потеря морали при получении урона
        int moraleLoss = damage / 2;
        this.morale = Math.max(0, this.morale - moraleLoss);
        
        if (this.currentHealth <= 0) {
            destroy();
        }
    }
    
    public void destroy() {
        this.isDestroyed = true;
        this.currentHealth = 0;
        // Здесь можно добавить логику показа NSFW сцены
        System.out.println("Полк '" + name + "' был разбит! Показываем сцену: " + defeatedImageId);
    }
    
    public boolean isAlive() {
        return !isDestroyed && currentHealth > 0;
    }
    
    public boolean isActive() {
        return isAlive();
    }
    
    public void equipItem(Item item) {
        inventory.equipItem(item);
        recalculateStats();
    }
    
    private void recalculateStats() {
        // Пересчет характеристик с учетом экипировки
        // Базовые значения
        initializeStats();
        
        // Применяем бонусы от предметов
        for (Item item : inventory.getAllItems()) {
            if (item != null) {
                this.damage += item.getDamageBonus();
                this.morale += item.getMoraleBonus();
                this.movement += item.getMovementBonus();
            }
        }
    }
    
    // Геттеры
    public String getName() { return name; }
    public RegimentType getType() { return type; }
    public int getMaxHealth() { return maxHealth; }
    public int getCurrentHealth() { return currentHealth; }
    public int getMorale() { return morale; }
    public int getExperience() { return experience; }
    public int getRange() { return range; }
    public int getDamage() { return damage; }
    public int getMovement() { return movement; }
    public Inventory getInventory() { return inventory; }
    public boolean isDestroyed() { return isDestroyed; }
    public String getDefeatedImageId() { return defeatedImageId; }
    
    // Сеттеры
    public void setName(String name) { this.name = name; }
    public void setExperience(int experience) { 
        this.experience = experience;
        // Рост опыта может улучшать характеристики
        if (experience >= 100) {
            this.damage += 5;
            this.morale += 10;
        }
    }
}
