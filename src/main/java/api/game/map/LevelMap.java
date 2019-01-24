package api.game.map;

import api.core.Context;
import api.core.Result;
import api.entity.warrior.Warrior;
import api.game.Coords;
import api.game.Rectangle;
import api.game.map.metadata.LevelMapMetaDataXml;

import java.util.List;

/**
 * Игровая карта c игроками и воинами
 */
public interface LevelMap {
  /**
   * Название карты
   * @return
   */
  String getName();

  /**
   * Описание
   * @return
   */
  String getDescription();

  /**
   * Размер базового единичного отрезка в пикселях
   * @return
   */
  int getSimpleUnitSize();

  /**
   * Количество игроков
   * @return
   */
  int getMaxPlayerCount();

  /**
   * ширина карты в базовых единицах измерения
   * @return
   */
  int getWidthInUnits();

  /**
   * высота карты в базовых единицах измерения
   * @return
   */
  int getHeightInUnits();

  /**
   * Получить список воинов в окружности заданным радиусом
   * @param center координата центра
   * @param radius радиус окружности. 0 - все воины
   * @return
   */
  List<Warrior> getWarriors(Coords center, int radius);

  /**
   * Добавить воина в заданные координаты заданному игроку
   * @param playerId
   * @param coords
   * @param warrior
   * @return
   */
  Warrior addWarrior(String playerId, Coords coords, Warrior warrior);

  /**
   * Добавить игрока в игру
   * @return
   */
  Result connectPlayer(Player player, String sessionId);

  /**
   * Отключить пользователя от игры.
   *
   * @param player
   * @return
   */
  Result disconnectPlayer(Player player);

  /**
   * Вернуть список игроков
   * @return
   */
  List<Player> getPlayers();

  /**
   * Вернуть игрока по его коду
   * @param playerId
   * @return
   */
  Player getPlayer(String playerId);

  /**
   * Зоны, в которых игроки могут расставлять фигурки на карте
   */
  List<Rectangle> getPlayerStartZone(String playerId);

  /**
   * Инициализация карты
   */
  void init(Context gameContext, LevelMapMetaDataXml levelMapMetaData);

  /**
   * Признак успешно липодготовлена карта
   * @return
   */
  boolean isLoaded();

  /**
   * Готова ликарта к игре. То естьвсе игроки есть и все фигуры
   * @return
   */
  boolean isReady();
}
