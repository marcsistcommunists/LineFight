package com.wargame.game.entities;

import java.util.HashMap;
import java.util.Map;

/**
 * Инвентарь полка с слотами для экипировки
 */
public class Inventory {
    
    // Слоты: оружие, знамя, аксессуар
    private Map<ItemType, Item> slots;
    
    public Inventory() {
        slots = new HashMap<>();
        slots.put(ItemType.WEAPON, null);
        slots.put(ItemType.BANNER, null);
        slots.put(ItemType.ACCESSORY, null);
    }
    
    /**
     * Экипировать предмет в соответствующий слот
     */
    public void equipItem(Item item) {
        if (item == null) return;
        
        ItemType type = item.getType();
        
        // Если уже есть предмет этого типа, снимаем его
        if (slots.containsKey(type)) {
            slots.put(type, item);
        } else {
            throw new IllegalArgumentException("Неверный тип предмета: " + type);
        }
    }
    
    /**
     * Снять предмет из слота
     */
    public Item unequipItem(ItemType type) {
        Item item = slots.get(type);
        slots.put(type, null);
        return item;
    }
    
    /**
     * Получить предмет из слота
     */
    public Item getItem(ItemType type) {
        return slots.get(type);
    }
    
    /**
     * Получить все предметы
     */
    public java.util.List<Item> getAllItems() {
        java.util.List<Item> items = new java.util.ArrayList<>();
        for (Item item : slots.values()) {
            if (item != null) {
                items.add(item);
            }
        }
        return items;
    }
    
    /**
     * Проверить, есть ли предмет в слоте
     */
    public boolean hasItem(ItemType type) {
        return slots.get(type) != null;
    }
    
    /**
     * Очистить весь инвентарь
     */
    public void clear() {
        for (ItemType type : slots.keySet()) {
            slots.put(type, null);
        }
    }
}
