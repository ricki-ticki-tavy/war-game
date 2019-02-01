package api.game.map;

import api.core.Context;
import api.core.Result;
import api.entity.ability.Modifier;
import api.entity.warrior.Influencer;
import api.entity.warrior.Warrior;
import api.enums.LifeTimeUnit;
import api.game.Coords;
import api.game.Rectangle;
import api.game.map.metadata.LevelMapMetaDataXml;
import core.game.GameProcessData;
import core.system.ActiveCoords;

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
   * Вернуть данные, связанные с процессом игры
   * @return
   */
  GameProcessData getGameProcessData();

  /**
   * Выполнить приготовления, связанные с
   */
  void beginGame();

  /**
   * Получить список воинов в окружности заданным радиусом
   * @param center координата центра
   * @param radius радиус окружности. 0 - все воины
   * @return
   */
  List<Warrior> getWarriors(Coords center, int radius);

  /**
   * Добавить воина в заданные координаты заданному игроку
   * @param player
   * @param warriorBaseClassName
   * @param coords
   * @return
   */
  Result<Warrior> createWarrior(Player player, String warriorBaseClassName, Coords coords);

  /**
   * Переместить юнит на новые координаты
   * @param player
   * @param warriorId
   * @param newCoords
   * @return
   */
  Result<Coords> moveWarriorTo(Player player, String warriorId, Coords newCoords);

  /**
   * Возвращает координаты,куда можно переместить перемещаемого юнита, исходя из того, куда его хотят переместить
   * @param player
   * @param warriorId
   * @param coords
   * @return
   */
  Result<? extends Coords> whatIfMoveWarriorTo(Player player,  String warriorId, Coords coords);

  /**
   * Удалить юнит игроком
   * @param player
   * @param warriorId
   * @return
   */
  Result<Warrior> removeWarrior(Player player, String warriorId);



  /**
   * Добавить игрока в игру
   * @return
   */
  Result connectPlayer(Player player);

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
   * Проверка допустимости новых координат воина
   * @param warrior
   * @param newCoords
   * @return
   */
  Result<Context> ifNewWarriorSCoordinatesAreAvailable(Warrior warrior, Coords newCoords);

  /**
   * Возвращает цену за перемещение на единицу длины для данного юнита с учетом всех его влияний, классов брони
   * и прочего
   * @param warrior
   * @return
   */
   int getWarriorSMoveCost(Warrior warrior);

//  /**
//   * Возвращает класс брони за перемещение на единицу длины для данного юнита с учетом всех его влияний, классов брони
//   * и прочего
//   * @param warrior
//   * @return
//   */
//   int getWarriorSArmor(Warrior warrior);
//
  /**
   * Возвращает координаты для юнита. Для получения координат этой функцией используется таблица юнитов
   * которыми делались действия в этом ходу. Если движение юнита можно откатить, то возвращаются его
   * координаты на которые можно сделать откат, если нельзя откатить или юнита нет в таблице, то берется
   * оригинальное значение координат
   * @param warrior
   * @return
   */
   Result<ActiveCoords> getWarriorSOriginCoords(Warrior warrior);

  /**
   * Получить игрока, выполняющего ход в данное время
   * @return
   */
  Result<Player> getPlayerOwnsThisTurn();

  /**
   * Передача хода следующему игроку
   * @param player  сменяемый игрок. Тот, кто передает ход следующему
   * @return
   */
  Result<Player> nextTurn(Player player);

  /**
   * добавить влияние юниту
   * @param modifier
   * @param lifeTimeUnit
   * @param lifeTime
   * @return
   */
  Result<Influencer> addInfluenceToWarrior(Player player, String warriorId, Modifier modifier, Object source, LifeTimeUnit lifeTimeUnit, int lifeTime);

  /**
   * Получить список оказываемых влияний на юнит
   * @return
   */
  Result<List<Influencer>> getWarriorSInfluencers(Player player, String warriorId);

}
