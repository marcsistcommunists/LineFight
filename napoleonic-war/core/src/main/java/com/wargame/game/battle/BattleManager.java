package com.wargame.game.battle;

import com.wargame.game.entities.Army;
import com.wargame.game.entities.Regiment;
import com.wargame.game.world.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Менеджер боя - управляет тактическим сражением
 */
public class BattleManager {
    
    // Поле боя - сетка тайлов
    private int gridSize = 16; // 16x16 клеток
    private BattleTile[][] grid;
    
    // Армии в бою
    private Army playerArmy;
    private Army enemyArmy;
    
    // Текущий ход (0 - игрок, 1 - враг)
    private int currentTurn;
    
    // Состояние боя
    private boolean battleActive;
    private boolean battleEnded;
    private Army winner;
    
    // Список поверженных полков для отображения
    private List<Regiment> defeatedRegiments;
    
    public BattleManager(Army playerArmy, Army enemyArmy, Province.TerrainType terrain) {
        this.playerArmy = playerArmy;
        this.enemyArmy = enemyArmy;
        this.defeatedRegiments = new ArrayList<>();
        this.battleActive = false;
        this.battleEnded = false;
        
        initializeGrid(terrain);
    }
    
    /**
     * Инициализация поля боя в зависимости от местности
     */
    private void initializeGrid(Province.TerrainType terrain) {
        grid = new BattleTile[gridSize][gridSize];
        
        for (int x = 0; x < gridSize; x++) {
            for (int y = 0; y < gridSize; y++) {
                grid[x][y] = new BattleTile(x, y, terrain);
            }
        }
        
        // Добавляем особенности местности
        addTerrainFeatures(terrain);
    }
    
    /**
     * Добавить особенности местности (препятствия, укрытия)
     */
    private void addTerrainFeatures(Province.TerrainType terrain) {
        switch (terrain) {
            case FOREST:
                // Добавить деревья как препятствия
                placeObstacles(5, 3);
                break;
            case HILLS:
                // Добавить холмы как укрытия
                placeCover(4, 2);
                break;
            case RIVER:
                // Добавить реку как труднопроходимую местность
                placeRiver();
                break;
            default:
                // Поле и город - чистое поле
                break;
        }
    }
    
    private void placeObstacles(int count, int radius) {
        // Упрощенная расстановка препятствий
        for (int i = 0; i < count; i++) {
            int x = (int)(Math.random() * gridSize);
            int y = (int)(Math.random() * gridSize);
            if (grid[x][y] != null) {
                grid[x][y].setObstacle(true);
            }
        }
    }
    
    private void placeCover(int count, int radius) {
        // Упрощенная расстановка укрытий
        for (int i = 0; i < count; i++) {
            int x = (int)(Math.random() * gridSize);
            int y = (int)(Math.random() * gridSize);
            if (grid[x][y] != null) {
                grid[x][y].setCover(true);
            }
        }
    }
    
    private void placeRiver() {
        // Река посередине поля
        int riverX = gridSize / 2;
        for (int y = 0; y < gridSize; y++) {
            grid[riverX][y].setDifficult(true);
        }
    }
    
    /**
     * Начать бой - расставить полки
     */
    public void startBattle() {
        System.out.println("=== НАЧАЛО БОЯ ===");
        System.out.println("Игрок: " + playerArmy.getName());
        System.out.println("Враг: " + enemyArmy.getName());
        
        // Расстановка игрока (левая сторона)
        deployArmy(playerArmy, 0, 4);
        
        // Расстановка врага (правая сторона)
        deployArmy(enemyArmy, gridSize - 1, gridSize - 5);
        
        battleActive = true;
        currentTurn = 0; // Ход игрока
    }
    
    /**
     * Расставить армию на поле
     */
    private void deployArmy(Army army, int startX, int endX) {
        List<Regiment> regiments = army.getLivingRegiments();
        int spacing = gridSize / (regiments.size() + 1);
        
        for (int i = 0; i < regiments.size(); i++) {
            Regiment regiment = regiments.get(i);
            int x = (startX + endX) / 2;
            int y = spacing * (i + 1);
            
            if (x >= 0 && x < gridSize && y >= 0 && y < gridSize) {
                BattleUnit unit = new BattleUnit(regiment, army.getOwnerId());
                grid[x][y].setUnit(unit);
                System.out.println("Размещен полк: " + regiment.getName() + " на [" + x + "," + y + "]");
            }
        }
    }
    
    /**
     * Выполнить ход
     */
    public void executeTurn(List<BattleAction> actions) {
        if (!battleActive || battleEnded) return;
        
        System.out.println("\n--- Ход " + (currentTurn == 0 ? "ИГРОКА" : "ВРАГА") + " ---");
        
        for (BattleAction action : actions) {
            performAction(action);
        }
        
        // Проверка окончания боя
        checkBattleEnd();
        
        // Смена хода
        if (!battleEnded) {
            currentTurn = 1 - currentTurn;
        }
    }
    
    /**
     * Выполнить одно действие
     */
    private void performAction(BattleAction action) {
        BattleUnit unit = getUnitAt(action.fromX, action.fromY);
        if (unit == null) return;
        
        switch (action.type) {
            case MOVE:
                moveUnit(unit, action.toX, action.toY);
                break;
            case ATTACK:
                attackTarget(unit, action.toX, action.toY);
                break;
            case WAIT:
                System.out.println(unit.getRegiment().getName() + " ожидает");
                break;
        }
    }
    
    /**
     * Переместить юнит
     */
    private void moveUnit(BattleUnit unit, int toX, int toY) {
        if (toX < 0 || toX >= gridSize || toY < 0 || toY >= gridSize) return;
        
        BattleTile targetTile = grid[toX][toY];
        if (targetTile.hasObstacle() || targetTile.hasUnit()) {
            System.out.println("Перемещение невозможно!");
            return;
        }
        
        // Очистить старую клетку
        grid[unit.getX()][unit.getY()].setUnit(null);
        
        // Переместить юнит
        unit.setPosition(toX, toY);
        grid[toX][toY].setUnit(unit);
        
        System.out.println(unit.getRegiment().getName() + " переместился на [" + toX + "," + toY + "]");
    }
    
    /**
     * Атаковать цель
     */
    private void attackTarget(BattleUnit attacker, int targetX, int targetY) {
        BattleUnit defender = getUnitAt(targetX, targetY);
        if (defender == null) {
            System.out.println("Цель не найдена!");
            return;
        }
        
        // Проверка дистанции
        int distance = calculateDistance(attacker.getX(), attacker.getY(), targetX, targetY);
        int range = attacker.getRegiment().getRange();
        
        if (distance > range) {
            System.out.println("Цель слишком далеко! Дистанция: " + distance + ", Дальность: " + range);
            return;
        }
        
        // Расчет урона
        int damage = attacker.getRegiment().getDamage();
        
        // Бонус за укрытие
        if (grid[targetX][targetY].hasCover()) {
            damage /= 2;
        }
        
        // Нанесение урона
        defender.takeDamage(damage);
        System.out.println(attacker.getRegiment().getName() + " атакует " + 
                          defender.getRegiment().getName() + " и наносит " + damage + " урона!");
        
        // Проверка на уничтожение
        if (!defender.getRegiment().isAlive()) {
            handleRegimentDefeated(defender);
        }
    }
    
    /**
     * Обработка уничтожения полка
     */
    private void handleRegimentDefeated(BattleUnit unit) {
        Regiment regiment = unit.getRegiment();
        regiment.destroy();
        defeatedRegiments.add(regiment);
        
        // Очистить клетку
        grid[unit.getX()][unit.getY()].setUnit(null);
        
        System.out.println("!!! Полк '" + regiment.getName() + "' УНИЧТОЖЕН !!!");
        System.out.println("Показываем сцену поражения: " + regiment.getDefeatedImageId());
        
        // Удаляем из армии
        Army army = (unit.getOwnerId() == 0) ? playerArmy : enemyArmy;
        army.removeRegiment(regiment);
    }
    
    /**
     * Проверить окончание боя
     */
    private void checkBattleEnd() {
        boolean playerHasUnits = hasLivingUnits(playerArmy);
        boolean enemyHasUnits = hasLivingUnits(enemyArmy);
        
        if (!playerHasUnits && !enemyHasUnits) {
            battleEnded = true;
            battleActive = false;
            winner = null;
            System.out.println("=== НИЧЬЯ ===");
        } else if (!playerHasUnits) {
            battleEnded = true;
            battleActive = false;
            winner = enemyArmy;
            System.out.println("=== ПОРАЖЕНИЕ ===");
        } else if (!enemyHasUnits) {
            battleEnded = true;
            battleActive = false;
            winner = playerArmy;
            System.out.println("=== ПОБЕДА ===");
        }
    }
    
    private boolean hasLivingUnits(Army army) {
        for (int x = 0; x < gridSize; x++) {
            for (int y = 0; y < gridSize; y++) {
                BattleTile tile = grid[x][y];
                if (tile.hasUnit()) {
                    BattleUnit unit = tile.getUnit();
                    if ((unit.getOwnerId() == 0 && army == playerArmy) ||
                        (unit.getOwnerId() == 1 && army == enemyArmy)) {
                        if (unit.getRegiment().isAlive()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    /**
     * Получить юнита в клетке
     */
    public BattleUnit getUnitAt(int x, int y) {
        if (x < 0 || x >= gridSize || y < 0 || y >= gridSize) return null;
        return grid[x][y].getUnit();
    }
    
    /**
     * Рассчитать дистанцию между двумя точками
     */
    private int calculateDistance(int x1, int y1, int x2, int y2) {
        return Math.abs(x2 - x1) + Math.abs(y2 - y1); // Манхэттенское расстояние
    }
    
    // Геттеры
    public BattleTile[][] getGrid() { return grid; }
    public int getGridSize() { return gridSize; }
    public boolean isBattleActive() { return battleActive; }
    public boolean isBattleEnded() { return battleEnded; }
    public Army getWinner() { return winner; }
    public List<Regiment> getDefeatedRegiments() { return defeatedRegiments; }
    public int getCurrentTurn() { return currentTurn; }
}
