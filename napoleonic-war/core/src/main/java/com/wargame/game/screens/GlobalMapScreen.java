package com.wargame.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.wargame.game.NapoleonicWarGame;
import com.wargame.game.world.Army;
import com.wargame.game.entities.Regiment;
import com.wargame.game.world.Province;
import com.wargame.game.world.WorldMap;

/**
 * Экран глобальной карты
 */
public class GlobalMapScreen implements com.badlogic.gdx.Screen {
    
    private NapoleonicWarGame game;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private BitmapFont font;
    
    private WorldMap worldMap;
    private Army playerArmy;
    
    // Для отладки - выбранная область
    private Province selectedProvince;
    
    public GlobalMapScreen(NapoleonicWarGame game) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, 1280, 720);
        this.font = new BitmapFont();
        this.font.setColor(Color.BLACK);
        
        // Инициализация мира
        worldMap = new WorldMap();
        worldMap.createTestMap();
        
        // Создание армии игрока
        playerArmy = createPlayerArmy();
        
        // Размещение армии в стартовой области
        Province startProvince = worldMap.getProvince(1); // Париж
        if (startProvince != null) {
            startProvince.stationArmy(playerArmy);
            selectedProvince = startProvince;
        }
        
        System.out.println("Экран глобальной карты инициализирован");
    }
    
    /**
     * Создать тестовую армию игрока
     */
    private Army createPlayerArmy() {
        Army army = new Army();
        army.setGeneralName("Наполеон");
        
        // Добавляем полки
        Regiment infantry1 = new Regiment("1-й Линейный полк", Regiment.RegimentType.LINE_INFANTRY);
        Regiment infantry2 = new Regiment("2-й Линейный полк", Regiment.RegimentType.LINE_INFANTRY);
        Regiment lightInfantry = new Regiment("Егерский батальон", Regiment.RegimentType.LIGHT_INFANTRY);
        Regiment cavalry = new Regiment("Гусарский полк", Regiment.RegimentType.CAVALRY);
        
        army.addRegiment(infantry1);
        army.addRegiment(infantry2);
        army.addRegiment(lightInfantry);
        army.addRegiment(cavalry);
        
        System.out.println("Создана армия: " + army.getRegimentCount() + " полков");
        return army;
    }
    
    @Override
    public void show() {
    }
    
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.5f, 0.7f, 0.5f, 1); // Зеленый фон (поле)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        
        batch.begin();
        
        // Отрисовка областей
        for (Province province : worldMap.getAllProvinces()) {
            drawProvince(province);
        }
        
        // Отрисовка информации о выбранной области
        drawInfoPanel();
        
        // Подсказка
        font.draw(batch, "Нажмите 1-5 для выбора области, M для перемещения", 10, 30);
        
        batch.end();
        
        handleInput();
    }
    
    /**
     * Отрисовка области
     */
    private void drawProvince(Province province) {
        float x = 100 + province.getId() * 200;
        float y = 300;
        float size = 80;
        
        // Цвет зависит от владельца
        Color color;
        if (province.getOwnerId() == 1) {
            color = Color.BLUE; // Игрок
        } else if (province.getOwnerId() == 2) {
            color = Color.RED; // Враг
        } else {
            color = Color.GRAY; // Ничейная
        }
        
        // Рисуем круг области
        batch.setColor(color);
        batch.draw(getTerrainTexture(province.getTerrainType()), x, y, size, size);
        
        // Название
        font.setColor(Color.BLACK);
        font.draw(batch, province.getName(), x, y - 20);
        
        // Армия
        if (province.hasArmy()) {
            font.setColor(Color.WHITE);
            font.draw(batch, "*", x + size/2 - 5, y + size/2);
        }
        
        // Выделение
        if (province == selectedProvince) {
            batch.setColor(Color.YELLOW);
            // Рамка вокруг выбранной
        }
        
        batch.setColor(Color.WHITE);
    }
    
    /**
     * Заглушка текстуры местности
     */
    private com.badlogic.gdx.graphics.Texture getTerrainTexture(Province.TerrainType terrain) {
        // Пока возвращаем null, потом загрузим реальные текстуры
        return null;
    }
    
    /**
     * Отрисовка информационной панели
     */
    private void drawInfoPanel() {
        if (selectedProvince == null) return;
        
        float panelX = 10;
        float panelY = 600;
        
        font.setColor(Color.BLACK);
        font.draw(batch, "Область: " + selectedProvince.getName(), panelX, panelY);
        font.draw(batch, "Тип: " + selectedProvince.getTerrainType(), panelX, panelY - 30);
        font.draw(batch, "Владелец: " + (selectedProvince.getOwnerId() == 1 ? "Игрок" : "Враг/Ничейная"), panelX, panelY - 60);
        
        if (selectedProvince.hasArmy()) {
            Army army = selectedProvince.getStationedArmy();
            font.draw(batch, "Генерал: " + army.getGeneralName(), panelX, panelY - 100);
            font.draw(batch, "Полков: " + army.getRegimentCount(), panelX, panelY - 130);
            
            // Список полков
            int yOffset = 170;
            for (Regiment regiment : army.getRegiments()) {
                if (regiment.isActive()) {
                    font.draw(batch, "  - " + regiment.getName() + 
                             " (HP: " + regiment.getCurrentHealth() + "/" + regiment.getMaxHealth() + ")", 
                             panelX, panelY - yOffset);
                    yOffset += 30;
                }
            }
        }
    }
    
    /**
     * Обработка ввода (упрощенная)
     */
    private void handleInput() {
        // В реальной игре здесь будет обработка кликов мыши
        // Для теста используем клавиатуру
        
        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.NUM_1)) {
            selectedProvince = worldMap.getProvince(1);
        } else if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.NUM_2)) {
            selectedProvince = worldMap.getProvince(2);
        } else if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.NUM_3)) {
            selectedProvince = worldMap.getProvince(3);
        } else if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.NUM_4)) {
            selectedProvince = worldMap.getProvince(4);
        } else if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.NUM_5)) {
            selectedProvince = worldMap.getProvince(5);
        }
    }
    
    @Override
    public void resize(int width, int height) {
    }
    
    @Override
    public void pause() {
    }
    
    @Override
    public void resume() {
    }
    
    @Override
    public void hide() {
    }
    
    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
