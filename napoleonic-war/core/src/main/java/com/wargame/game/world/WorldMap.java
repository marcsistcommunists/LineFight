package com.wargame.game.world;

import java.util.*;

/**
 * Глобальная карта игры
 */
public class WorldMap {
    
    private Map<Integer, Province> provinces;
    private int playerTurn;
    
    public WorldMap() {
        this.provinces = new HashMap<>();
        this.playerTurn = 1;
    }
    
    /**
     * Добавить область на карту
     */
    public void addProvince(Province province) {
        provinces.put(province.getId(), province);
    }
    
    /**
     * Получить область по ID
     */
    public Province getProvince(int id) {
        return provinces.get(id);
    }
    
    /**
     * Получить все области
     */
    public Collection<Province> getAllProvinces() {
        return provinces.values();
    }
    
    /**
     * Соединить две области
     */
    public void connectProvinces(int id1, int id2) {
        Province p1 = provinces.get(id1);
        Province p2 = provinces.get(id2);
        
        if (p1 != null && p2 != null) {
            p1.addConnection(id2);
            p2.addConnection(id1);
        }
    }
    
    /**
     * Переместить армию в соседнюю область
     * @return true если перемещение успешно
     */
    public boolean moveArmy(Army army, int targetProvinceId) {
        Province current = army.getCurrentLocation();
        Province target = provinces.get(targetProvinceId);
        
        if (current == null || target == null) {
            return false;
        }
        
        // Проверка соединения
        if (!current.isConnectedTo(targetProvinceId)) {
            System.out.println("Области не соединены!");
            return false;
        }
        
        // Проверка на вражескую армию - начинается бой
        if (target.hasEnemyArmy(0)) { // Пока ownerId не используется полноценно
            System.out.println("Вражеская армия обнаружена! Начинается бой...");
            // Здесь будет запуск боевой сцены
            return false; // Армия не перемещается до завершения боя
        }
        
        // Если в целевой области своя армия - объединяем? (пока просто не пускаем)
        if (target.hasArmy()) {
            System.out.println("В области уже стоит армия!");
            return false;
        }
        
        // Перемещение
        current.removeArmy();
        target.stationArmy(army);
        
        // Захват территории если ничейная
        if (target.getOwnerId() == 0) {
            target.setOwnerId(1); // Временно хардкод для игрока
        }
        
        return true;
    }
    
    /**
     * Получить доступные для перемещения области
     */
    public List<Province> getAvailableMoves(Army army) {
        List<Province> available = new ArrayList<>();
        Province current = army.getCurrentLocation();
        
        if (current == null) return available;
        
        for (int neighborId : current.getConnectedProvinceIds()) {
            Province neighbor = provinces.get(neighborId);
            if (neighbor != null && !neighbor.hasEnemyArmy(0)) {
                // Не показываем области со своей армией
                if (!neighbor.hasArmy()) {
                    available.add(neighbor);
                }
            }
        }
        
        return available;
    }
    
    /**
     * Начать ход игрока
     */
    public void startPlayerTurn(int playerId) {
        playerTurn = playerId;
        System.out.println("Ход игрока " + playerId);
    }
    
    /**
     * Завершить ход
     */
    public void endTurn() {
        System.out.println("Ход завершен");
    }
    
    /**
     * Создать тестовую карту
     */
    public void createTestMap() {
        // Создаем несколько областей
        Province city1 = new Province(1, "Париж", Province.TerrainType.CITY);
        Province field1 = new Province(2, "Шампань", Province.TerrainType.FIELD);
        Province forest1 = new Province(3, "Арденны", Province.TerrainType.FOREST);
        Province city2 = new Province(4, "Брюссель", Province.TerrainType.CITY);
        Province hills1 = new Province(5, "Ватерлоо", Province.TerrainType.HILLS);
        
        addProvince(city1);
        addProvince(field1);
        addProvince(forest1);
        addProvince(city2);
        addProvince(hills1);
        
        // Соединяем области
        connectProvinces(1, 2); // Париж - Шампань
        connectProvinces(2, 3); // Шампань - Арденны
        connectProvinces(2, 5); // Шампань - Ватерлоо
        connectProvinces(3, 4); // Арденны - Брюссель
        connectProvinces(5, 4); // Ватерлоо - Брюссель
        
        System.out.println("Тестовая карта создана: 5 областей");
    }
}
