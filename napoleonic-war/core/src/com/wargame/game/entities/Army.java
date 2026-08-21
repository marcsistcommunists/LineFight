package com.wargame.game.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Армия - группа полков, перемещающаяся по глобальной карте
 */
public class Army {
    
    private String name;
    private List<Regiment> regiments;
    private int ownerId;  // ID владельца (игрок или ИИ)
    
    // Позиция на глобальной карте
    private int currentProvinceId;
    
    public Army(String name, int ownerId) {
        this.name = name;
        this.ownerId = ownerId;
        this.regiments = new ArrayList<>();
        this.currentProvinceId = -1;
    }
    
    /**
     * Добавить полк в армию
     */
    public void addRegiment(Regiment regiment) {
        if (regiment != null && regiment.isAlive()) {
            regiments.add(regiment);
        }
    }
    
    /**
     * Удалить полк из армии (если разбит)
     */
    public void removeRegiment(Regiment regiment) {
        regiments.remove(regiment);
    }
    
    /**
     * Очистить армию от уничтоженных полков
     */
    public void cleanupDestroyedRegiments() {
        regiments.removeIf(Regiment::isDestroyed);
    }
    
    /**
     * Проверить, есть ли живые полки в армии
     */
    public boolean hasLivingRegiments() {
        return regiments.stream().anyMatch(Regiment::isAlive);
    }
    
    /**
     * Получить количество живых полков
     */
    public int getLivingRegimentsCount() {
        return (int) regiments.stream().filter(Regiment::isAlive).count();
    }
    
    /**
     * Получить все живые полки
     */
    public List<Regiment> getLivingRegiments() {
        List<Regiment> living = new ArrayList<>();
        for (Regiment r : regiments) {
            if (r.isAlive()) {
                living.add(r);
            }
        }
        return living;
    }
    
    /**
     * Нанести урон всем полкам (например, при отступлении)
     */
    public void damageAllRegiments(int damage) {
        for (Regiment r : regiments) {
            if (r.isAlive()) {
                r.takeDamage(damage);
            }
        }
        cleanupDestroyedRegiments();
    }
    
    // Геттеры и сеттеры
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public int getOwnerId() { return ownerId; }
    public void setOwnerId(int ownerId) { this.ownerId = ownerId; }
    
    public List<Regiment> getRegiments() { return regiments; }
    
    public int getCurrentProvinceId() { return currentProvinceId; }
    public void setCurrentProvinceId(int provinceId) { 
        this.currentProvinceId = provinceId; 
    }
    
    /**
     * Получить список поверженных полков для отображения в сайдбаре
     */
    public List<Regiment> getDestroyedRegiments() {
        List<Regiment> destroyed = new ArrayList<>();
        for (Regiment r : regiments) {
            if (r.isDestroyed()) {
                destroyed.add(r);
            }
        }
        return destroyed;
    }
}
