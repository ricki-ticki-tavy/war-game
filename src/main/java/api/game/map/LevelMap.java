package api.game.map;

import api.core.Context;
import api.entity.warrior.Warrior;
import api.game.Coords;
import api.game.Rectangle;
import api.game.map.metadata.LevelMapMetaData;

import java.util.List;

/**
 * Игровая карта c игроками и воинами
 */
public interface LevelMap<W extends Warrior> {
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
  List<W> getWarriors(Coords center, int radius);

  /**
   * Добавить воина в заданные координаты заданному игроку
   * @param playerId
   * @param coords
   * @param warrior
   * @return
   */
  W addWarrior(String playerId, Coords coords, W warrior);

  /**
   * Добавить игрока в игру
   * @return
   */
  Player connectPlayer(String playerSessionId);

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
  void init(Context gameContext, LevelMapMetaData levelMapMetaData);

}
