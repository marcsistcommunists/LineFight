package com.wargame.game.world;

import com.wargame.game.entities.Regiment;
import java.util.ArrayList;
import java.util.List;

/**
 * Армия - группа полков, перемещающаяся по глобальной карте.
 */
public class Army {
    private final List<Regiment> regiments;
    private String generalName;
    private int movementPoints;
    private Province currentLocation;

    public Army() {
        this.regiments = new ArrayList<>();
        this.generalName = "Unknown General";
        this.movementPoints = 2; // Очки движения за ход
    }

    public void addRegiment(Regiment regiment) {
        regiments.add(regiment);
    }

    public void removeRegiment(Regiment regiment) {
        regiments.remove(regiment);
    }

    public List<Regiment> getRegiments() {
        return new ArrayList<>(regiments);
    }

    public boolean isEmpty() {
        return regiments.isEmpty();
    }

    public int getRegimentCount() {
        return regiments.size();
    }

    public String getGeneralName() {
        return generalName;
    }

    public void setGeneralName(String generalName) {
        this.generalName = generalName;
    }

    public int getMovementPoints() {
        return movementPoints;
    }

    public void spendMovementPoint() {
        if (movementPoints > 0) {
            movementPoints--;
        }
    }

    public void resetMovementPoints() {
        this.movementPoints = 2;
    }

    public Province getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Province location) {
        this.currentLocation = location;
    }

    /**
     * Проверка, есть ли в армии уничтоженные полки (для удаления с карты)
     */
    public boolean hasActiveRegiments() {
        return regiments.stream().anyMatch(Regiment::isActive);
    }
    
    /**
     * Получить первый активный полк (для рендера или боя)
     */
    public Regiment getFirstActiveRegiment() {
        for (Regiment r : regiments) {
            if (r.isActive()) {
                return r;
            }
        }
        return null;
    }
}
